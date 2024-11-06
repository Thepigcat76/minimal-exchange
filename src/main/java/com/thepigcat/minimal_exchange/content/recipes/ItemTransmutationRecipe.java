package com.thepigcat.minimal_exchange.content.recipes;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.capabilities.MECapabilities;
import com.thepigcat.minimal_exchange.capabilities.matter.IMatterStorage;
import com.thepigcat.minimal_exchange.data.MEDataMaps;
import com.thepigcat.minimal_exchange.data.maps.ItemTransmutationValue;
import com.thepigcat.minimal_exchange.registries.MEItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class ItemTransmutationRecipe extends CustomRecipe {
    public ItemTransmutationRecipe(CraftingBookCategory category) {
        super(category);
    }

    private ItemTransmutationValue getTransmutation(CraftingInput craftingInput) {
        ItemStack ingredient = ItemStack.EMPTY;
        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack item = craftingInput.getItem(i);
            if (!item.isEmpty() && !item.is(MEItems.TRANSMUTATION_STONE.get())) {
                ingredient = item;
                break;
            }
        }
        return ingredient.getItemHolder().getData(MEDataMaps.ITEM_TRANSMUTATIONS);
    }
    
    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        ItemStack transmutationStone = ItemStack.EMPTY;
        ItemStack ingredient = ItemStack.EMPTY;
        int ingredientCount = 0;
        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack item = craftingInput.getItem(i);
            if (!item.isEmpty()) {
                if (item.is(MEItems.TRANSMUTATION_STONE)) {
                    transmutationStone = item.copy();
                } else if (ingredient.isEmpty() || ingredient.is(item.getItem())) {
                    ingredient = item.copy();
                    ingredientCount++;
                } else {
                    return false;
                }
            }
        }

        if (transmutationStone.isEmpty() || ingredient.isEmpty()) return false;

        ItemTransmutationValue transmutation = ingredient.getItemHolder().getData(MEDataMaps.ITEM_TRANSMUTATIONS);
        return transmutation != null
                && transmutation.inputCount() == ingredientCount
                && transmutationStone.getCapability(MECapabilities.MatterStorage.ITEM).getMatter() >= transmutation.matterCost();
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
        return this.getTransmutation(craftingInput).result().copy();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput craftingInput) {
        NonNullList<ItemStack> remainingItems = NonNullList.withSize(craftingInput.size(), ItemStack.EMPTY);

        ItemTransmutationValue transmutation = this.getTransmutation(craftingInput);

        for (int i = 0; i < remainingItems.size(); i++) {
            ItemStack input = craftingInput.getItem(i);
            if (input.is(MEItems.TRANSMUTATION_STONE)) {
                ItemStack copy = input.copy();
                IMatterStorage matterStorage = copy.getCapability(MECapabilities.MatterStorage.ITEM);
                matterStorage.extractMatter(transmutation.matterCost(), false);
                remainingItems.set(i, copy);
            }
        }

        return remainingItems;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MinimalExchange.ITEM_TRANSMUTATION.get();
    }
}
