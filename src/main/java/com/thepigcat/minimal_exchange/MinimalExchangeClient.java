package com.thepigcat.minimal_exchange;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.thepigcat.minimal_exchange.client.MEArmPose;
import com.thepigcat.minimal_exchange.client.blockentities.ExchangePylonBERenderer;
import com.thepigcat.minimal_exchange.client.screen.AlchemyBagScreen;
import com.thepigcat.minimal_exchange.client.screen.ExchangePylonScreen;
import com.thepigcat.minimal_exchange.content.items.DestructionCatalystItem;
import com.thepigcat.minimal_exchange.content.items.DiviningRodItem;
import com.thepigcat.minimal_exchange.content.items.InertStoneItem;
import com.thepigcat.minimal_exchange.matter.MatterManager;
import com.thepigcat.minimal_exchange.registries.MEBlockEntityTypes;
import com.thepigcat.minimal_exchange.registries.MEItems;
import com.thepigcat.minimal_exchange.registries.MEMenuTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod(value = MinimalExchangeClient.MODID, dist = Dist.CLIENT)
public final class MinimalExchangeClient {
    public static final String MODID = "minimal_exchange";

    public MinimalExchangeClient(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerColorHandlers);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::registerMenuScreen);
        modEventBus.addListener(this::registerBlockEntityRenderers);
        modEventBus.addListener(this::registerClientExtensions);

        NeoForge.EVENT_BUS.addListener(this::addMatterTooltip);
        NeoForge.EVENT_BUS.addListener(this::renderOffHand);

        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    private void addMatterTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (MatterManager.containsItem(itemStack.getItem())) {
            long matter = MatterManager.getMatterForItem(itemStack.getItem());
            if (Screen.hasShiftDown()) {
                event.getToolTip().add(Component.literal("Stack: ")
                        .withStyle(ChatFormatting.GRAY)
                        .append(Component.literal(String.valueOf(matter * itemStack.getCount()))
                                .withStyle(ChatFormatting.GRAY))
                        .append(Component.literal(" Matter")
                                .withColor(FastColor.ARGB32.color(255, 245, 192, 89))));
                event.getToolTip().add(Component.literal("Item: ")
                        .withStyle(ChatFormatting.GRAY)
                        .append(Component.literal(String.valueOf(matter))
                                .withStyle(ChatFormatting.GRAY))
                        .append(Component.literal(" Matter")
                                .withColor(FastColor.ARGB32.color(255, 245, 192, 89))));
            } else {
                event.getToolTip().add(Component.literal("<Hold SHIFT to view matter>").withStyle(ChatFormatting.GRAY));
            }
        }
    }

    private void renderOffHand(RenderHandEvent event) {
//        if (event.getHand() == InteractionHand.MAIN_HAND && Minecraft.getInstance().player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof DiviningRodItem) {
//            event.setCanceled(true);
//        }
    }

    private void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel.@Nullable ArmPose getArmPose(@NotNull LivingEntity entityLiving, @NotNull InteractionHand hand, @NotNull ItemStack itemStack) {
                return MEArmPose.DIVINING_ROD.getValue();
            }

            @Override
            public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
                if (arm == player.getMainArm() && player.getMainHandItem() == itemInHand) {
                    float f7;
                    if (player.isUsingItem()) {
                        float f = (float) (player.getUseItemRemainingTicks() % 200);
                        float f1 = f - partialTick + 1.0F;
                        float f2 = 1.0F - f1 / 75F;
                        f7 = 0F + 15.0F * Mth.cos(f2 * 2.0F * (float) Math.PI);
                    } else {
                        f7 = 0;
                    }
                    if (arm == HumanoidArm.RIGHT) {
                        poseStack.translate(-0.85, -0.25, -1.3);
                        poseStack.mulPose(Axis.ZP.rotationDegrees(270));
                        poseStack.mulPose(Axis.YP.rotationDegrees(5f));

                        poseStack.translate(0, 0, 0.5);
                        poseStack.mulPose(Axis.YP.rotationDegrees(f7));
                        poseStack.translate(0, 0, -0.5);

                        poseStack.mulPose(Axis.XP.rotationDegrees(65f));
                    } else {
                        poseStack.translate(-1.05, -2.275, -3.25);
                        poseStack.mulPose(Axis.ZP.rotationDegrees(90));
                        poseStack.mulPose(Axis.YP.rotationDegrees(175f));

                        poseStack.translate(-2, 0, -2);
                        poseStack.mulPose(Axis.YP.rotationDegrees(-f7));
                        poseStack.translate(2, 0, 2);

                        poseStack.mulPose(Axis.XP.rotationDegrees(-25f));
                    }
                    poseStack.scale(1.75f, 1.75f, 1.75f);
                }
                return IClientItemExtensions.super.applyForgeHandTransform(poseStack, player, arm, itemInHand, partialTick, equipProcess, swingProcess);
            }
        }, MEItems.DIVINING_ROD, MEItems.ENHANCED_DIVING_ROD);
    }

    private void registerColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register((itemStack, i) -> i == 0 ? FastColor.ARGB32.color(255, itemStack.get(DataComponents.DYED_COLOR).rgb()) : -1, MEItems.ALCHEMY_BAG.get());
    }

    private void registerMenuScreen(RegisterMenuScreensEvent event) {
        event.register(MEMenuTypes.ALCHEMY_BAG.get(), AlchemyBagScreen::new);
        event.register(MEMenuTypes.EXCHANGE_PYLON.get(), ExchangePylonScreen::new);
    }

    private void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(MEBlockEntityTypes.EXCHANGE_PYLON.get(), ExchangePylonBERenderer::new);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(MEItems.DESTRUCTION_CATALYST.get(), ResourceLocation.fromNamespaceAndPath(MODID, "matter"),
                    (stack, level, living, id) -> DestructionCatalystItem.matterAmount(stack));
            ItemProperties.register(MEItems.INERT_STONE.get(), MinimalExchange.rl("vibrating"),
                    (stack, level, living, id) -> InertStoneItem.isVibrating(level));
        });
    }
}
