package com.thepigcat.minimal_exchange.data;

import com.mojang.serialization.Codec;
import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.data.maps.ItemTransmutationValue;
import com.thepigcat.minimal_exchange.util.CodecUtils;
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
    public static final DataMapType<Item, ItemTransmutationValue> ITEM_TRANSMUTATIONS = DataMapType.builder(
            ResourceLocation.fromNamespaceAndPath(MinimalExchange.MODID, "transmutations"),
            Registries.ITEM,
            ItemTransmutationValue.CODEC
    ).synced(
            ItemTransmutationValue.CODEC,
            false
    ).build();
}
