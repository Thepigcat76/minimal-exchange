package com.thepigcat.minimal_exchange.datagen.data;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.content.items.TransmutationStoneItem;
import com.thepigcat.minimal_exchange.content.recipes.SpecialRecipes;
import com.thepigcat.minimal_exchange.data.MEDataComponents;
import com.thepigcat.minimal_exchange.data.components.MatterComponent;
import com.thepigcat.minimal_exchange.registries.MEItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {
    public RecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        SpecialRecipeBuilder.special(SpecialRecipes::new)
                        .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MinimalExchange.MODID, "transmutation_stone"));

        transmutationRecipe(recipeOutput, Tags.Items.OBSIDIANS, 4, Items.IRON_INGOT);
        transmutationRecipe(recipeOutput, Tags.Items.INGOTS_IRON, 4, Items.ENDER_PEARL);
        transmutationRecipe(recipeOutput, Tags.Items.INGOTS_IRON, 8, Items.GOLD_INGOT);
    }

    private void transmutationRecipe(RecipeOutput recipeOutput, TagKey<Item> ingredient, int ingredientCount, ItemLike result) {
        ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result);
        builder.requires(MEItems.TRANSMUTATION_STONE);
        for (int i = 0; i < ingredientCount; i++) {
            builder.requires(ingredient);
        }

        builder.unlockedBy("has_transmutation_stone", has(MEItems.TRANSMUTATION_STONE))
                .save(recipeOutput);
    }

    private void transmutationRecipe(RecipeOutput recipeOutput, ItemLike ingredient, int ingredientCount, ItemLike result) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result)
                .requires(ingredient, ingredientCount)
                .requires(MEItems.TRANSMUTATION_STONE)
                .unlockedBy("has_transmutation_stone", has(MEItems.TRANSMUTATION_STONE))
                .save(recipeOutput);
    }
}
