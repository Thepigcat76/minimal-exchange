package com.thepigcat.minimal_exchange.datagen.data;

import com.thepigcat.minimal_exchange.data.MEDataMaps;
import com.thepigcat.minimal_exchange.data.maps.ItemTransmutationValue;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class DataMapProvider extends net.neoforged.neoforge.common.data.DataMapProvider {
    public DataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather() {
        builder(MEDataMaps.ITEM_TRANSMUTATIONS)
                .add(Items.IRON_INGOT.getDefaultInstance().getItemHolder(),
                        new ItemTransmutationValue(
                                Items.ENDER_PEARL.getDefaultInstance(),
                                4
                        ), false);
    }
}
