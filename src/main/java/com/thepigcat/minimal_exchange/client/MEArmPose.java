package com.thepigcat.minimal_exchange.client;

import com.thepigcat.minimal_exchange.MinimalExchange;
import net.minecraft.client.model.HumanoidModel;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;

public final class MEArmPose {
    public static final EnumProxy<HumanoidModel.ArmPose> DIVINING_ROD = new EnumProxy<>(
            HumanoidModel.ArmPose.class, true, MEArmPose.DIVINING_ROD_ARM_POS_TRANSFORMER
    );

    public static final IArmPoseTransformer DIVINING_ROD_ARM_POS_TRANSFORMER = (model, entity, arm) -> {
        MinimalExchange.LOGGER.debug("RENDERING DIVINING_ROD");
    };
}
