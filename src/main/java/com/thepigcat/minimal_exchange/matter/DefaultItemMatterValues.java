package com.thepigcat.minimal_exchange.matter;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.HashMap;
import java.util.Map;

public final class DefaultItemMatterValues {
    public static final Map<ItemMatterValues.Key, Long> DEFAULT_MATTER_VALUES = new HashMap<>();

    private static void put(TagKey<Item> key, long value) {
        DEFAULT_MATTER_VALUES.put(new ItemMatterValues.Key.KeyTag(key), value);
    }

    private static void put(ItemLike key, long value) {
        DEFAULT_MATTER_VALUES.put(new ItemMatterValues.Key.KeyItem(key.asItem()), value);
    }

    static {
        put(ItemTags.LOGS, 32);
        put(Tags.Items.STRIPPED_LOGS, 32);
        put(Blocks.COBBLESTONE, 2);
        put(Blocks.DEEPSLATE, 2);
    }
}
