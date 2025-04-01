package com.thepigcat.minimal_exchange.matter;

import com.thepigcat.minimal_exchange.MEConfig;
import com.thepigcat.minimal_exchange.data.MEDataMaps;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.*;

// Responsible for calculating matter
public final class MatterHelper {
    private static final Map<Item, List<Recipe<?>>> RECIPES = new HashMap<>();

    public static void calculateMatter(Level level) {
        collectRecipes(level);

        // Default matter values
        Map<ItemMatterValues.Key, Long> dataMap = ItemMatterValues.ITEM_MATTER;
        for (Map.Entry<ItemMatterValues.Key, Long> entry : dataMap.entrySet()) {
            ItemMatterValues.Key key = entry.getKey();
            if (key instanceof ItemMatterValues.Key.KeyItem(Item item)) {
                MatterManager.putMatterForItem(item, entry.getValue());
            } else if (key instanceof ItemMatterValues.Key.KeyTag(TagKey<Item> tag)) {
                Optional<HolderSet.Named<Item>> named = BuiltInRegistries.ITEM.getTag(tag);
                if (named.isPresent()) {
                    HolderSet.Named<Item> holders = named.get();
                    for (Holder<Item> item : holders) {
                        MatterManager.putMatterForItem(item.value(), entry.getValue());
                    }
                }
            }
        }

        // Calculate matter values through recipes
        // Goes through all items and will calculate the matter values for all ingredients as well
        // Will save items for which we cant calculate a matter value to a set in MatterManager
        for (Item item : BuiltInRegistries.ITEM) {
            if (!MatterManager.containsItem(item)) {
                getMatterThroughRecipe(item, level, new HashSet<>());
            }
        }
    }

    private static final int RECURSION_LIMIT = 20;
    private static int recursionLevel = 0;

    // FIXME: Stack overflow :p
    // Cache recipes by putting them in a map
    // Map<Itemstack, List<Recipe<?>>>
    private static OptionalLong getMatterThroughRecipe(Item item, Level level, Set<Recipe<?>> currentPath) {
        if (MatterManager.containsItem(item)) {
            recursionLevel--;
            return OptionalLong.of(MatterManager.getMatterForItem(item.asItem()));
        }

        if (!MatterManager.hasMatter(item)) {
            recursionLevel--;
            return OptionalLong.empty();
        }

        recursionLevel++;

        List<Recipe<?>> recipes = RECIPES.get(item);
        if (recipes != null) {
            recipeLoop:
            for (Recipe<?> recipe : recipes) {
                // Prevent infinite recursion by checking cycles only within the current call stack
                if (!currentPath.add(recipe)) {
                    recursionLevel--;
                    return OptionalLong.empty();
                }

                if (recursionLevel > RECURSION_LIMIT) {
                    recursionLevel--;
                    currentPath.remove(recipe);
                    return OptionalLong.empty();
                }

                NonNullList<Ingredient> ingredients = recipe.getIngredients();
                long totalMatter = -1;

                ItemStack resultItem = recipe.getResultItem(level.registryAccess());

                for (Ingredient ingredient : ingredients) {
                    if (ingredient.isEmpty()) continue;

                    long ingredientMatter = -1;
                    for (ItemStack ingredientStack : ingredient.getItems()) {
                        if (!ingredientStack.is(item)) {
                            OptionalLong _matter = getMatterThroughRecipe(ingredientStack.getItem(), level, new HashSet<>(currentPath));

                            if (_matter.isEmpty()) continue;

                            long matter = _matter.getAsLong() * ingredientStack.getCount();
                            if (matter != -1) {
                                if (ingredientMatter == -1) {
                                    ingredientMatter = matter;
                                } else {
                                    ingredientMatter = Math.min(ingredientMatter, matter);
                                }
                            }
                        }
                    }
                    if (ingredientMatter != -1) {
                        if (totalMatter == -1) {
                            totalMatter = 0;
                        }
                        totalMatter += ingredientMatter;
                    } else {
                        recursionLevel--;
                        currentPath.remove(recipe);
                        continue recipeLoop;
                    }
                }

                recursionLevel--;
                long count = Math.max(1, resultItem.getCount());
                long value = totalMatter / count;
                MatterManager.putMatterForItem(item, value);
                currentPath.remove(recipe);
                return OptionalLong.of(value);
            }

        }

        recursionLevel--;
        if (recipes != null) {
            for (Recipe<?> recipe : recipes) {
                currentPath.remove(recipe);
            }
        }
        return OptionalLong.empty();
    }

    private static void collectRecipes(Level level) {
        RecipeManager manager = level.getRecipeManager();
        for (RecipeHolder<?> holder : manager.getRecipes()) {
            Recipe<?> recipe = holder.value();
            ItemStack resultItem = recipe.getResultItem(level.registryAccess());
            RECIPES.computeIfAbsent(resultItem.getItem(), k -> new ArrayList<>()).add(recipe);
        }
    }
}
