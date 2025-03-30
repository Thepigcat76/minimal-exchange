package com.thepigcat.minimal_exchange.data;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.data.maps.BlockTransmutationValue;
import com.thepigcat.minimal_exchange.data.maps.EntityTransmutationValue;
import com.thepigcat.minimal_exchange.data.maps.ItemTransmutationValue;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import java.util.List;

public final class MEDataMaps {
    public static final DataMapType<Block, BlockTransmutationValue> BLOCK_TRANSMUTATIONS = DataMapType.builder(
            ResourceLocation.fromNamespaceAndPath(MinimalExchange.MODID, "transmutations"),
            Registries.BLOCK,
            BlockTransmutationValue.CODEC
    ).synced(
            BlockTransmutationValue.CODEC,
            false
    ).build();

    public static final DataMapType<Item, List<ItemTransmutationValue>> ITEM_TRANSMUTATIONS = DataMapType.builder(
            ResourceLocation.fromNamespaceAndPath(MinimalExchange.MODID, "transmutations"),
            Registries.ITEM,
            ItemTransmutationValue.CODEC.listOf()
    ).synced(
            ItemTransmutationValue.CODEC.listOf(),
            false
    ).build();

    public static final DataMapType<EntityType<?>, EntityTransmutationValue> ENTITY_TRANSMUTATIONS = DataMapType.builder(
            ResourceLocation.fromNamespaceAndPath(MinimalExchange.MODID, "transmutations"),
            Registries.ENTITY_TYPE,
            EntityTransmutationValue.CODEC
    ).synced(
            EntityTransmutationValue.CODEC,
            false
    ).build();
}
