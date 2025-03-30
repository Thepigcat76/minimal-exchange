package com.thepigcat.minimal_exchange.datagen.data;

import com.thepigcat.minimal_exchange.data.MEDataMaps;
import com.thepigcat.minimal_exchange.data.maps.BlockTransmutationValue;
import com.thepigcat.minimal_exchange.data.maps.EntityTransmutationValue;
import com.thepigcat.minimal_exchange.data.maps.ItemTransmutationValue;
import com.thepigcat.minimal_exchange.util.RegistryUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DataMapProvider extends net.neoforged.neoforge.common.data.DataMapProvider {
    public static final Map<ResourceKey<Item>, List<ItemTransmutationValue>> ITEM_TRANSMUTATIONS = new HashMap<>();

    public DataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    private void itemTransmutation(Item input, int inputCount, ItemStack result, int matterCost) {
        ITEM_TRANSMUTATIONS.computeIfAbsent(input.builtInRegistryHolder().key(), k -> new ArrayList<>())
                .add(new ItemTransmutationValue(result, inputCount, matterCost));
    }

    private void blockTransmutation(Block input, Block output, int matterCost) {
        builder(MEDataMaps.BLOCK_TRANSMUTATIONS)
                .add(RegistryUtils.resourceKey(BuiltInRegistries.BLOCK, input), new BlockTransmutationValue(output, matterCost), false);
    }

    private void entityTransmutation(EntityType<?> input, EntityType<? extends LivingEntity> output, int matterCost) {
        builder(MEDataMaps.ENTITY_TRANSMUTATIONS)
                .add(RegistryUtils.resourceKey(BuiltInRegistries.ENTITY_TYPE, input), new EntityTransmutationValue(output, matterCost), false);
    }

    @Override
    protected void gather() {
        itemTransmutation(Items.IRON_INGOT, 4, Items.ENDER_PEARL.getDefaultInstance(), 1);
        itemTransmutation(Items.ENDER_PEARL, 1, Items.IRON_INGOT.getDefaultInstance().copyWithCount(4), 1);
        itemTransmutation(Items.IRON_INGOT, 8, Items.GOLD_INGOT.getDefaultInstance(), 1);
        itemTransmutation(Items.GOLD_INGOT, 1, Items.IRON_INGOT.getDefaultInstance().copyWithCount(8), 1);
        itemTransmutation(Items.GOLD_INGOT, 8, Items.DIAMOND.getDefaultInstance(), 1);
        itemTransmutation(Items.DIAMOND, 1, Items.GOLD_INGOT.getDefaultInstance().copyWithCount(8), 1);

        blockTransmutation(Blocks.SAND, Blocks.DIRT, 1);
        blockTransmutation(Blocks.DIRT, Blocks.COBBLESTONE, 1);
        blockTransmutation(Blocks.COBBLESTONE, Blocks.GRASS_BLOCK, 1);
        blockTransmutation(Blocks.GRASS_BLOCK, Blocks.SAND, 1);

        entityTransmutation(EntityType.CHICKEN, EntityType.PARROT, 1);
        entityTransmutation(EntityType.PARROT, EntityType.BAT, 1);
        entityTransmutation(EntityType.BAT, EntityType.CHICKEN, 1);

        entityTransmutation(EntityType.SQUID, EntityType.GLOW_SQUID, 1);
        entityTransmutation(EntityType.GLOW_SQUID, EntityType.SQUID, 1);

        entityTransmutation(EntityType.SLIME, EntityType.MAGMA_CUBE, 1);
        entityTransmutation(EntityType.MAGMA_CUBE, EntityType.SLIME, 1);

        for (Map.Entry<ResourceKey<Item>, List<ItemTransmutationValue>> entry : ITEM_TRANSMUTATIONS.entrySet()) {
            builder(MEDataMaps.ITEM_TRANSMUTATIONS)
                    .add(BuiltInRegistries.ITEM.getHolderOrThrow(entry.getKey()), entry.getValue(), false);
        }
    }
}
