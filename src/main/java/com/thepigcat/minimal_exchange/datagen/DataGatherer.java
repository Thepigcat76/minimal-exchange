package com.thepigcat.minimal_exchange.datagen;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.datagen.assets.MEEnUSLangProvider;
import com.thepigcat.minimal_exchange.datagen.assets.MEBlockStateProvider;
import com.thepigcat.minimal_exchange.datagen.assets.MEItemModelProvider;
import com.thepigcat.minimal_exchange.datagen.data.DataMapProvider;
import com.thepigcat.minimal_exchange.datagen.data.RecipeProvider;
import com.thepigcat.minimal_exchange.datagen.data.TagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = MinimalExchange.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGatherer {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new MEItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new MEEnUSLangProvider(packOutput));
        generator.addProvider(event.includeClient(), new MEBlockStateProvider(packOutput, existingFileHelper));

        TagProvider.createTagProviders(generator, packOutput, lookupProvider, existingFileHelper, event.includeServer());
        generator.addProvider(event.includeServer(), new RecipeProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new DataMapProvider(packOutput, lookupProvider));
//        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(), List.of(
//                new LootTableProvider.SubProviderEntry(BlockLootTableProvider::new, LootContextParamSets.BLOCK)
//        ), lookupProvider));
    }
}