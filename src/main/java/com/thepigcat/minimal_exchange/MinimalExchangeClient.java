package com.thepigcat.minimal_exchange;

import com.thepigcat.minimal_exchange.content.items.DestructionCatalystItem;
import com.thepigcat.minimal_exchange.registries.MEItems;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = MinimalExchangeClient.MODID)
public final class MinimalExchangeClient {
    public static final String MODID = "minimal_exchange";

    public MinimalExchangeClient(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerColorHandlers);
        modEventBus.addListener(this::clientSetup);

        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    private void registerColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register((itemStack, i) -> FastColor.ARGB32.color(255, itemStack.get(DataComponents.DYED_COLOR).rgb()), MEItems.ALCHEMY_BAG.get());
    }

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(MEItems.DESTRUCTION_CATALYST.get(), ResourceLocation.fromNamespaceAndPath(MODID, "matter"),
                    (stack, level, living, id) -> DestructionCatalystItem.matterAmount(stack));
        });
    }
}
