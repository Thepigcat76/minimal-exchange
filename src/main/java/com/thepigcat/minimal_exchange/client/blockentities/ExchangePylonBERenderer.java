package com.thepigcat.minimal_exchange.client.blockentities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.thepigcat.minimal_exchange.content.blockentities.ExchangePylonBlockEntity;
import com.thepigcat.minimal_exchange.util.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class ExchangePylonBERenderer implements BlockEntityRenderer<ExchangePylonBlockEntity> {
    public ExchangePylonBERenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ExchangePylonBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        RenderUtils.renderFloatingItem(blockEntity.getItemHandler().getStackInSlot(1), poseStack, bufferSource, packedLight, packedOverlay, 1.2F);
    }
}
