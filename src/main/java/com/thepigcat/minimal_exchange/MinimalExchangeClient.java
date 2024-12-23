package com.thepigcat.minimal_exchange;

import com.thepigcat.minimal_exchange.client.blockentities.ExchangePylonBERenderer;
import com.thepigcat.minimal_exchange.client.screen.AlchemyBagScreen;
import com.thepigcat.minimal_exchange.client.screen.ExchangePylonScreen;
import com.thepigcat.minimal_exchange.content.items.DestructionCatalystItem;
import com.thepigcat.minimal_exchange.registries.MEBlockEntityTypes;
import com.thepigcat.minimal_exchange.registries.MEItems;
import com.thepigcat.minimal_exchange.registries.MEMenuTypes;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = MinimalExchangeClient.MODID, dist = Dist.CLIENT)
public final class MinimalExchangeClient {
    public static final String MODID = "minimal_exchange";

    public MinimalExchangeClient(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerColorHandlers);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::registerMenuScreen);
        modEventBus.addListener(this::registerBlockEntityRenderers);

        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
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
        });
    }
}
