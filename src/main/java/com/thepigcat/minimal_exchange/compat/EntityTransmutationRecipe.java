package com.thepigcat.minimal_exchange.compat;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.data.maps.EntityTransmutationValue;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record EntityTransmutationRecipe(EntityType<?> input, EntityTransmutationValue result) implements Recipe<EntityRecipeInput> {
    @Override
    public boolean matches(EntityRecipeInput input, Level level) {
        return input.entity() == this.input;
    }

    @Override
    public ItemStack assemble(EntityRecipeInput input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.simple(ResourceLocation.fromNamespaceAndPath(MinimalExchange.MODID, "entity_transmutations"));
    }
}
