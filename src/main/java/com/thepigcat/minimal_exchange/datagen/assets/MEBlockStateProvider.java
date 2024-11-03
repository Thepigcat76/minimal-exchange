package com.thepigcat.minimal_exchange.datagen.assets;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.registries.MEBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MEBlockStateProvider extends BlockStateProvider {
    public MEBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MinimalExchange.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(MEBlocks.EXCHANGE_PYLON.get(), new ModelFile.UncheckedModelFile(modLoc("block/exchange_pylon")));
    }

    private ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    private String name(Block block) {
        return key(block).getPath();
    }

    public ResourceLocation blockTexture(Block block) {
        ResourceLocation name = key(block);
        return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + name.getPath());
    }

    private ResourceLocation extend(ResourceLocation rl, String suffix) {
        return ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), rl.getPath() + suffix);
    }

}
