package com.thepigcat.minimal_exchange.compat;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.data.maps.BlockTransmutationValue;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public record BlockTransmutationRecipe(Block input, BlockTransmutationValue result) implements Recipe<SingleRecipeInput> {
    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return input.item().is(this.input.asItem());
    }

    @Override
    public @NotNull ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) {
        return new ItemStack(this.input);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return new ItemStack(result.result());
    }

    @Override
    public RecipeSerializer<BlockTransmutationRecipe> getSerializer() {
        return null;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeType.simple(ResourceLocation.fromNamespaceAndPath(MinimalExchange.MODID, "block_transmutations"));
    }

}