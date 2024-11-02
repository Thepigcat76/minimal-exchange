package com.thepigcat.minimal_exchange.datagen.data;

import com.thepigcat.minimal_exchange.data.MEDataComponents;
import com.thepigcat.minimal_exchange.data.MEDataMaps;
import com.thepigcat.minimal_exchange.data.components.MatterComponent;
import com.thepigcat.minimal_exchange.data.maps.SpecialRecipeValue;
import com.thepigcat.minimal_exchange.registries.MEItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DataMapProvider extends net.neoforged.neoforge.common.data.DataMapProvider {
    public DataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather() {
//        ItemStack result = MEItems.TRANSMUTATION_STONE.toStack();
//        result.set(MEDataComponents.MATTER, new MatterComponent(100, 100));
//        builder(MEDataMaps.SPECIAL_RECIPES)
//                .add(MEItems.TRANSMUTATION_STONE.getDelegate(),
//                        new SpecialRecipeValue(
//                                List.of("SSS", "SDS", "SSS"),
//                                Map.of("S", Ingredient.of(MEItems.MINIUM_SHARD),
//                                        "D", Ingredient.of(Tags.Items.GEMS_DIAMOND)),
//                                MEItems.TRANSMUTATION_STONE.toStack()
//                        ), false);
    }
}
