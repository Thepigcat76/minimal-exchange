package com.thepigcat.minimal_exchange.compat;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.registries.MEItems;
import com.thepigcat.minimal_exchange.util.RenderUtils;
import it.unimi.dsi.fastutil.Pair;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4fStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EntityTransmutationCategory implements IRecipeCategory<EntityTransmutationRecipe> {
    static final ResourceLocation BURN_PROGRESS_SPRITE = ResourceLocation.fromNamespaceAndPath(MinimalExchange.MODID, "container/furnace/empty_arrow");
    public static final RecipeType<EntityTransmutationRecipe> ENTITY_TRANSMUTATION_TYPE =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(MinimalExchange.MODID, "entity_transmutations"), EntityTransmutationRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final Map<EntityTransmutationRecipe, Pair<Entity, Entity>> entities;

    public EntityTransmutationCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(100, 32);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(MEItems.TRANSMUTATION_STONE.get()));
        this.entities = new HashMap<>();
    }

    @Override
    public RecipeType<EntityTransmutationRecipe> getRecipeType() {
        return ENTITY_TRANSMUTATION_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Entity Transmutations");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    @Nullable
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, EntityTransmutationRecipe recipe, IFocusGroup focuses) {
        SpawnEggItem inputEggItem = SpawnEggItem.byId(recipe.input());
        SpawnEggItem resultEggItem = SpawnEggItem.byId(recipe.result().result());
        if (inputEggItem != null) {
            builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemLike(inputEggItem);
        }
        if (resultEggItem != null) {
            builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemLike(resultEggItem);
        }
        ClientLevel level = Minecraft.getInstance().level;
        entities.put(recipe, Pair.of(recipe.input().create(level), recipe.result().result().create(level)));
    }

    @Override
    public void draw(EntityTransmutationRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        double baseHeight = 12f;

        Pair<Entity, Entity> entityPair = entities.get(recipe);
        Entity inputEntity = entityPair.first();
        if (inputEntity instanceof LivingEntity livingEntity) {
            double inputScale = baseHeight / inputEntity.getBbHeight();
            renderEntity(guiGraphics, 24, 24, inputScale, 1.0, 1.0, livingEntity);
        }
        Entity resultEntity = entityPair.second();
        if (resultEntity instanceof LivingEntity livingEntity) {
            double resultScale = baseHeight / resultEntity.getBbHeight();
            renderEntity(guiGraphics, 100 - 24, 24, resultScale, 1.0, 1.0, livingEntity);
        }
        guiGraphics.blitSprite(BURN_PROGRESS_SPRITE, getWidth() / 2 - 12, 8, 24, 16);
        RenderUtils.drawCenteredString(
                guiGraphics,
                Minecraft.getInstance().font,
                Component.literal(recipe.result().matterCost() + " Matter"),
                getWidth() / 2,
                0,
                ChatFormatting.GRAY.getColor(),
                false
        );
    }

    public static void renderEntity(GuiGraphics guiGraphics, int x, int y, double scale, double yaw, double pitch, LivingEntity livingEntity) {
        Lighting.setupForEntityInInventory();
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.mul(guiGraphics.pose().last().pose());
        modelViewStack.translate(x, y, 50.0F);
        modelViewStack.scale((float) -scale, (float) scale, (float) scale);
        PoseStack mobPoseStack = new PoseStack();
        mobPoseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        mobPoseStack.mulPose(Axis.XN.rotationDegrees(((float) Math.atan((pitch / 40.0F))) * 20.0F));
        livingEntity.yo = (float) Math.atan(yaw / 40.0F) * 20.0F;
        float yRot = (float) Math.atan(yaw / 40.0F) * 40.0F;
        float xRot = -((float) Math.atan(pitch / 40.0F)) * 20.0F;
        livingEntity.setYRot(yRot);
        livingEntity.setYRot(yRot);
        livingEntity.setXRot(xRot);
        livingEntity.yHeadRot = yRot;
        livingEntity.yHeadRotO = yRot;
        mobPoseStack.translate(0.0F, livingEntity.getY(), 0.0F);
        RenderSystem.applyModelViewMatrix();
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        entityRenderDispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            entityRenderDispatcher.render(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, mobPoseStack, bufferSource, 15728880);
        });
        bufferSource.endBatch();
        entityRenderDispatcher.setRenderShadow(true);
        modelViewStack.popMatrix();
        RenderSystem.applyModelViewMatrix();
    }

}
