package com.thepigcat.minimal_exchange.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class ClientUtils {
    public static void renderDiviningRodInHand(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext displayContext, HumanoidArm arm, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.translate(-0.025, 0, -0.255);
        poseStack.mulPose(Axis.XP.rotationDegrees(95));
        poseStack.mulPose(Axis.YP.rotationDegrees(45));
        poseStack.mulPose(Axis.ZP.rotationDegrees(60));

        if (arm == HumanoidArm.LEFT) {
            poseStack.translate(0, 0.065, 0.05);
            poseStack.mulPose(Axis.XP.rotationDegrees(75));
            poseStack.mulPose(Axis.YP.rotationDegrees(33.5f));
            poseStack.mulPose(Axis.ZP.rotationDegrees(-25f));
        }
    }
}
