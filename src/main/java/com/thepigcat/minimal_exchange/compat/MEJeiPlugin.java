package com.thepigcat.minimal_exchange.compat;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.data.MEDataMaps;
import com.thepigcat.minimal_exchange.data.maps.ItemTransmutationValue;
import com.thepigcat.minimal_exchange.registries.MEItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class MEJeiPlugin implements IModPlugin {
    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(MinimalExchange.MODID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(RecipeTypes.CRAFTING, getRecipes());
    }

    private static List<RecipeHolder<CraftingRecipe>> getRecipes() {
        Map<ResourceKey<Item>, ItemTransmutationValue> transmutations = BuiltInRegistries.ITEM.getDataMap(MEDataMaps.ITEM_TRANSMUTATIONS);
        List<RecipeHolder<CraftingRecipe>> recipes = new ArrayList<>();

        Ingredient transmutationStone = getTransmutationStone();

        for (Map.Entry<ResourceKey<Item>, ItemTransmutationValue> transmutation : transmutations.entrySet()) {
            ResourceKey<Item> key = transmutation.getKey();
            ItemTransmutationValue transmutationValue = transmutation.getValue();

            List<Ingredient> ingredients = getIngredients(transmutation, transmutationStone, transmutationValue);
            CraftingRecipe craftingRecipe = new ShapelessRecipe("transmutations", CraftingBookCategory.MISC, transmutationValue.result().copy(), NonNullList.copyOf(ingredients));
            recipes.add(new RecipeHolder<>(ResourceLocation.fromNamespaceAndPath(key.location().getNamespace(), "item_transmutations/"+ key.location().getPath()), craftingRecipe));
        }

        return recipes;
    }

    private static @NotNull List<Ingredient> getIngredients(Map.Entry<ResourceKey<Item>, ItemTransmutationValue> transmutation, Ingredient transmutationStone, ItemTransmutationValue transmutationValue) {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(transmutationStone);
        for (int i = 0; i < transmutationValue.inputCount(); i++) {
            ingredients.add(Ingredient.of(BuiltInRegistries.ITEM.get(transmutation.getKey())));
        }
        return ingredients;
    }

    private static @NotNull Ingredient getTransmutationStone() {
        ItemStack stack = MEItems.TRANSMUTATION_STONE.toStack();
        stack.set(DataComponents.LORE, new ItemLore(List.of(Component.literal("Requires at least 1 matter").withStyle(ChatFormatting.RESET).withStyle(ChatFormatting.GRAY))));
        return Ingredient.of(stack);
    }
}
