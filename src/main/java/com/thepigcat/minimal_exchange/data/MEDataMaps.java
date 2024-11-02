package com.thepigcat.minimal_exchange.data;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.data.maps.SpecialRecipeValue;
import com.thepigcat.minimal_exchange.util.CodecUtils;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

public final class MEDataMaps {
    private static final Codec<Block> BLOCK_CODEC = CodecUtils.registryCodec(BuiltInRegistries.BLOCK);
    public static final DataMapType<Block, Block> BLOCK_TRANSMUTATIONS = DataMapType.builder(
            ResourceLocation.fromNamespaceAndPath(MinimalExchange.MODID, "transmutations"),
            Registries.BLOCK,
            BLOCK_CODEC
    ).synced(
            BLOCK_CODEC,
            false
    ).build();
    public static final DataMapType<Item, SpecialRecipeValue> SPECIAL_RECIPES = DataMapType.builder(
            ResourceLocation.fromNamespaceAndPath(MinimalExchange.MODID, "special_recipes"),
            Registries.ITEM,
            SpecialRecipeValue.CODEC
    ).synced(
            SpecialRecipeValue.CODEC,
            false
    ).build();
}
