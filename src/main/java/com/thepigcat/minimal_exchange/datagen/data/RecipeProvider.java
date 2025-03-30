package com.thepigcat.minimal_exchange.datagen.data;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.content.recipes.ItemTransmutationRecipe;
import com.thepigcat.minimal_exchange.registries.MEBlocks;
import com.thepigcat.minimal_exchange.registries.MEItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {
    public RecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MEItems.TRANSMUTATION_STONE)
                .pattern("SSS")
                .pattern("SIS")
                .pattern("SSS")
                .define('S', MEItems.MINIUM_SHARD.get())
                .define('I', MEItems.INERT_STONE.get())
                .unlockedBy("has_shard", has(MEItems.MINIUM_SHARD.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MEItems.DESTRUCTION_CATALYST)
                .pattern("WMS")
                .pattern("MFM")
                .pattern("SMW")
                .define('W', MEItems.WEAK_COVALENCE_DUST.get())
                .define('S', MEItems.STRONG_COVALENCE_DUST.get())
                .define('M', MEItems.MINIUM_SHARD)
                .define('F', Items.FIRE_CHARGE)
                .unlockedBy("has_shard", has(MEItems.MINIUM_SHARD.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MEBlocks.EXCHANGE_PYLON)
                .pattern(" O ")
                .pattern(" S ")
                .pattern("OWO")
                .define('S', MEItems.STRONG_COVALENCE_DUST.get())
                .define('W', MEItems.WEAK_COVALENCE_DUST.get())
                .define('O', Tags.Items.OBSIDIANS)
                .unlockedBy("has_obsidian", has(Tags.Items.OBSIDIANS))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, MEItems.WEAK_COVALENCE_DUST)
                .requires(Tags.Items.DUSTS_REDSTONE)
                .requires(Tags.Items.INGOTS_IRON)
                .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, MEItems.STRONG_COVALENCE_DUST)
                .requires(Tags.Items.DUSTS_GLOWSTONE)
                .requires(Tags.Items.INGOTS_GOLD)
                .unlockedBy("has_glowstone", has(Tags.Items.DUSTS_GLOWSTONE))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MEItems.ALCHEMY_BAG)
                .pattern(" S ")
                .pattern("WCW")
                .pattern("WWW")
                .define('S', Tags.Items.STRINGS)
                .define('W', ItemTags.WOOL)
                .define('C', MEItems.WEAK_COVALENCE_DUST.get())
                .unlockedBy("has_obsidian", has(Tags.Items.OBSIDIANS))
                .save(recipeOutput);


        SpecialRecipeBuilder.special(ItemTransmutationRecipe::new)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MinimalExchange.MODID, "item_transmutation"));
    }
}
