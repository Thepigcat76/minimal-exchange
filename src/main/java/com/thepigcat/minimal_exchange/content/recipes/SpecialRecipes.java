package com.thepigcat.minimal_exchange.content.recipes;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.data.MEDataMaps;
import com.thepigcat.minimal_exchange.data.maps.SpecialRecipeValue;
import com.thepigcat.minimal_exchange.registries.MEItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;

public class SpecialRecipes extends CustomRecipe {
    public SpecialRecipes(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        Holder<Item> expectedResult = MEItems.TRANSMUTATION_STONE.getDelegate();
        SpecialRecipeValue specialRecipe = expectedResult.getData(MEDataMaps.SPECIAL_RECIPES);
        if (specialRecipe != null) {
            int recipeSize = recipeSize(specialRecipe);
            MinimalExchange.LOGGER.debug("size: {}", recipeSize);
            if (recipeSize == craftingInput.size()) {
                List<String> pattern = specialRecipe.pattern();
                Map<String, Ingredient> definitions = specialRecipe.definitions();
                for (int i = 0; i < pattern.size(); i++) {
                    String line = pattern.get(i);
                    for (int j = 0; j < line.length(); j++) {
                        char key = line.charAt(j);
                        Ingredient ingredient = definitions.get(String.valueOf(key));
                        ItemStack inputStack = craftingInput.getItem(i, j);
                        if (!ingredient.test(inputStack)) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    private static int recipeSize(SpecialRecipeValue specialRecipeValue) {
        int len = 0;
        for (String line : specialRecipeValue.pattern()) {
            for (char key : line.toCharArray()) {
                Ingredient ingredient = specialRecipeValue.definitions().get(String.valueOf(key));
                if (!ingredient.isEmpty()) {
                    len++;
                }
            }
        }
        return len;
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
        Holder<Item> expectedResult = MEItems.TRANSMUTATION_STONE.getDelegate();
        SpecialRecipeValue specialRecipe = expectedResult.getData(MEDataMaps.SPECIAL_RECIPES);
        if (specialRecipe != null) {
            return specialRecipe.result().copy();
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MinimalExchange.SPECIAL_RECIPES.get();
    }
}
