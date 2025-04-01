package com.thepigcat.minimal_exchange.datagen.assets;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.content.items.DiviningRodItem;
import com.thepigcat.minimal_exchange.registries.MEItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

public class MEItemModelProvider extends ItemModelProvider {
    public MEItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MinimalExchange.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(MEItems.WEAK_COVALENCE_DUST.get());
        basicItem(MEItems.STRONG_COVALENCE_DUST.get());
        basicItem(MEItems.MINIUM_SHARD.get());
        basicItem(MEItems.IRON_BAND.get());
        basicItem(MEItems.INERT_STONE.get());
        basicItem(MEItems.TRANSMUTATION_STONE.get());
        basicItem(MEItems.DESTRUCTIVE_AURA_TALISMAN.get());
        basicItem(MEItems.GROWTH_RING.get());
        basicItem(MEItems.ALCHEMY_BAG.get())
                .texture("layer1", modLoc("item/alchemy_bag_overlay"));
        destructionCatalystItem(MEItems.DESTRUCTION_CATALYST.get());
        overrideItemModel(4, basicItem(MEItems.DESTRUCTION_CATALYST.get(), extend(itemTexture(MEItems.DESTRUCTION_CATALYST.get()), "_0")), MinimalExchange.rl("matter"),
                i -> basicItem(MEItems.DESTRUCTION_CATALYST.get(), "_" + i));
        overrideItemModel(2, basicItem(MEItems.INERT_STONE.get()), MinimalExchange.rl("vibrating"),
                i -> i == 1 ? basicItem(MEItems.INERT_STONE.get(), "_vibrating") : basicItem(MEItems.INERT_STONE.get()));

        diviningRodItem(MEItems.DIVINING_ROD);
        diviningRodItem(MEItems.ENHANCED_DIVING_ROD);

        blockItems();
    }

    private void diviningRodItem(ItemLike item) {
        ItemModelBuilder diviningRodModelHandheld = basicItem(item.asItem(), "_handheld");
        ItemModelBuilder diviningRodModel = basicItem(item.asItem(), "_gui");
        getBuilder(name(item.asItem()))
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .customLoader(SeparateTransformsModelBuilder::begin)
                .base(diviningRodModelHandheld)
                .perspective(ItemDisplayContext.GUI, diviningRodModel)
                .perspective(ItemDisplayContext.FIXED, diviningRodModel)
                .perspective(ItemDisplayContext.GROUND, diviningRodModel);
    }

    private void blockItems() {
        for (DeferredItem<BlockItem> blockItem : MEItems.BLOCK_ITEMS) {
            parentItemBlock(blockItem.get());
        }
    }

    private void overrideItemModel(int variants, ItemModelBuilder defaultModel, ResourceLocation key, Function<Integer, ItemModelBuilder> overrideFunction) {
        for (int i = 0; i < variants; i++) {
            ItemModelBuilder model = overrideFunction.apply(i);
            defaultModel.override()
                    .model(model)
                    .predicate(key, i)
                    .end();
        }
    }

    public void destructionCatalystItem(Item item) {
        int variants = 4;

        ResourceLocation matter = modLoc("matter");

        ItemModelBuilder builder = getBuilder(name(item))
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", extend(itemTexture(item), "_0"));
        for (int i = 0; i < variants; i++) {
            builder.override()
                    .model(basicItem(item, "_" + i))
                    .predicate(matter, i)
                    .end();
        }
    }

    public ItemModelBuilder basicItem(Item item, ResourceLocation texture, String suffix) {
        ResourceLocation location = BuiltInRegistries.ITEM.getKey(item);
        return getBuilder(location + suffix)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", extend(texture, suffix));
    }

    public ItemModelBuilder basicItem(Item item, ResourceLocation texture) {
        return basicItem(item, texture, "");
    }

    public ItemModelBuilder basicItem(Item item, String suffix) {
        return basicItem(item, itemTexture(item), suffix);
    }

    public String name(Item item) {
        return key(item).getPath();
    }

    private static @NotNull ResourceLocation key(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    public void parentItemBlock(Item item) {
        parentItemBlock(item, "");
    }

    public void parentItemBlock(Item item, String suffix) {
        ResourceLocation name = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
        getBuilder(name.toString())
                .parent(new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(name.getNamespace(), "block/" + name.getPath() + suffix)));
    }

    public ItemModelBuilder handHeldItem(Item item) {
        return handHeldItem(item, "");
    }

    public ItemModelBuilder handHeldItem(Item item, String suffix) {
        ResourceLocation location = key(item);
        return getBuilder(location + suffix)
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(location.getNamespace(), "item/" + location.getPath() + suffix));
    }

    public ResourceLocation itemTexture(Item item) {
        ResourceLocation name = key(item);
        return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), ModelProvider.ITEM_FOLDER + "/" + name.getPath());
    }

    private ResourceLocation extend(ResourceLocation rl, String suffix) {
        return ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), rl.getPath() + suffix);
    }

}
