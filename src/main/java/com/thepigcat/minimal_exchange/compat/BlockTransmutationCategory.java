package com.thepigcat.minimal_exchange.compat;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.registries.MEItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockTransmutationCategory implements IRecipeCategory<BlockTransmutationRecipe> {
    static final ResourceLocation BURN_PROGRESS_SPRITE = ResourceLocation.fromNamespaceAndPath(MinimalExchange.MODID, "container/furnace/empty_arrow");
    public static final RecipeType<BlockTransmutationRecipe> BLOCK_TRANSMUTATION_TYPE =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(MinimalExchange.MODID, "block_transmutations"), BlockTransmutationRecipe.class);

    private final IDrawable icon;
    private final IDrawable background;

    public BlockTransmutationCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(80, 18);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(MEItems.TRANSMUTATION_STONE.get()));
    }

    @Override
    public @NotNull RecipeType<BlockTransmutationRecipe> getRecipeType() {
        return BLOCK_TRANSMUTATION_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.literal("Block Transmutations");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    @Nullable
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BlockTransmutationRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 0, 0)
                .addIngredients(Ingredient.of(recipe.input()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 64, 0)
                .addIngredients(Ingredient.of(recipe.result().result()));
    }

    @Override
    public void draw(BlockTransmutationRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blitSprite(BURN_PROGRESS_SPRITE, 28, 0, 24, 16);
    }
}