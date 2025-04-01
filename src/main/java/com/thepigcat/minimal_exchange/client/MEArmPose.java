package com.thepigcat.minimal_exchange.client;

import com.thepigcat.minimal_exchange.MinimalExchange;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;

public final class MEArmPose {
    public static final EnumProxy<HumanoidModel.ArmPose> DIVINING_ROD = new EnumProxy<>(
            HumanoidModel.ArmPose.class, true, (IArmPoseTransformer) MEArmPose::divingRodArmPoseTransformer
    );

    public static void divingRodArmPoseTransformer(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
        float partialTick = DeltaTracker.ONE.getGameTimeDeltaPartialTick(true);
        float f7;
        if (entity.isUsingItem()) {
            float f = (float) (entity.getUseItemRemainingTicks() % 200);
            float f1 = f - partialTick + 1.0F;
            float f2 = 1.0F - f1 / 75F;
            f7 = 0F + 15.0F * Mth.cos(f2 * 2.0F * (float) Math.PI);
        } else {
            f7 = 0;
        }
        model.rightArm.zRot = (float) Math.toRadians(-45);
        model.leftArm.zRot = (float) Math.toRadians(45);
        model.rightArm.yRot = (float) Math.toRadians(25 + f7);
        model.leftArm.yRot = (float) Math.toRadians(-25 - f7);
        model.rightArm.xRot = (float) Math.toRadians(-40);
        model.leftArm.xRot = (float) Math.toRadians(-40);
    }
}
