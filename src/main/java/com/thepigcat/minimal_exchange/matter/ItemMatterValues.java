package com.thepigcat.minimal_exchange.matter;


import com.google.common.base.Stopwatch;
import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thepigcat.minimal_exchange.MinimalExchange;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.fml.loading.FMLPaths;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

public final class ItemMatterValues {
    public static Map<Key, Long> ITEM_MATTER;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void writeDefaultItemMatterValues() {
        File dir = getMEDirectory();

        if (!dir.exists() && dir.mkdirs()) {
            MatterValues itemMatter = new MatterValues(true, DefaultItemMatterValues.DEFAULT_MATTER_VALUES);
            DataResult<JsonElement> json = MatterValues.CODEC.encodeStart(JsonOps.INSTANCE, itemMatter);
            if (json.isError()) {
                MinimalExchange.LOGGER.error("Failed to write item matter values to file, error: {}", json.error().get().message());
                return;
            }

            File file = new File(dir.toPath().resolve("item_matter.json").toString());

            try (FileWriter writer = new FileWriter(file)) {
                GSON.toJson(json.getOrThrow(), writer);
            } catch (Exception e) {
                MinimalExchange.LOGGER.error("An error occurred while generating the item matter value json", e);
            }
        }
    }

    public static void loadItemMatterValues() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        File dir = getMEDirectory();

        writeDefaultItemMatterValues();

        loadFromFile(dir);

        stopwatch.stop();

        MinimalExchange.LOGGER.info("Loaded {} item matter values type(s) in {} ms", ITEM_MATTER.size(), stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    private static void loadFromFile(File dir) {
        if (dir == null)
            return;

        File file = new File(dir.getPath() + "/item_matter.json");

        InputStreamReader reader = null;

        try {
            reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);

            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

            reader.close();

            DataResult<Pair<MatterValues, JsonElement>> map = MatterValues.CODEC.decode(JsonOps.INSTANCE, json);
            Optional<Pair<MatterValues, JsonElement>> itemMatter = map.resultOrPartial(err -> MinimalExchange.LOGGER.warn("Encountered error while loading item matter values, error: {}", err));
            itemMatter.map(Pair::getFirst).ifPresent(values -> {
                if (values.allowMigration()) {
                    Map<Key, Long> defaultValues = new HashMap<>(DefaultItemMatterValues.DEFAULT_MATTER_VALUES);
                    for (Map.Entry<Key, Long> entry : values.values().entrySet()) {
                        defaultValues.remove(entry.getKey(), entry.getValue());
                    }
                    HashMap<Key, Long> map1 = new HashMap<>(values.values());
                    map1.putAll(defaultValues);

                    DataResult<JsonElement> result = MatterValues.CODEC.encodeStart(JsonOps.INSTANCE, new MatterValues(values.allowMigration(), map1));

                    try (FileWriter writer = new FileWriter(file)) {
                        GSON.toJson(result.getOrThrow(), writer);

                        ITEM_MATTER = map1;
                    } catch (Exception e) {
                        MinimalExchange.LOGGER.error("An error occurred while generating the item matter value json", e);
                    }

                }
                ITEM_MATTER = values.values();
            });
        } catch (Exception e) {
            MinimalExchange.LOGGER.error("An error occurred while reading item matter json {}", file.getName(), e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    private static @NotNull File getMEDirectory() {
        return FMLPaths.CONFIGDIR.get().resolve(MinimalExchange.MODID).toFile();
    }

    public sealed interface Key permits Key.KeyItem, Key.KeyTag {
        Codec<Key> CODEC = new Codec<>() {
            @Override
            public <T> DataResult<Pair<Key, T>> decode(DynamicOps<T> ops, T input) {
                DataResult<String> stringValue = ops.getStringValue(input);
                DataResult<Key> result = stringValue.map(s -> {
                    if (s.startsWith("#")) {
                        String trimmedString = s.substring(1);
                        ResourceLocation rl = ResourceLocation.parse(trimmedString);
                        return new KeyTag(TagKey.create(Registries.ITEM, rl));
                    } else {
                        return new KeyItem(BuiltInRegistries.ITEM.get(ResourceLocation.parse(s)));
                    }
                });
                return result.map(k -> Pair.of(k, input));
            }

            @Override
            public <T> DataResult<T> encode(Key input, DynamicOps<T> ops, T prefix) {
                T string = ops.createString(input.toString());
                return DataResult.success(string);
            }
        };

        record KeyItem(Item item) implements Key {
            @Override
            public String toString() {
                return item.builtInRegistryHolder().key().location().toString();
            }
        }

        record KeyTag(TagKey<Item> tag) implements Key {
            @Override
            public String toString() {
                return "#" + tag.location();
            }
        }

    }

    record MatterValues(boolean allowMigration, Map<Key, Long> values) {
        private static final Codec<MatterValues> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Codec.BOOL.fieldOf("allow_migration").forGetter(MatterValues::allowMigration),
                Codec.unboundedMap(Key.CODEC, Codec.LONG).fieldOf("values").forGetter(MatterValues::values)
        ).apply(inst, MatterValues::new));
    }

}
