package com.thepigcat.minimal_exchange.client.screen;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.api.client.screens.MEAbstractContainerScreen;
import com.thepigcat.minimal_exchange.content.menus.AlchemyBagMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class AlchemyBagScreen extends MEAbstractContainerScreen<AlchemyBagMenu> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(MinimalExchange.MODID, "textures/gui/alchemy_bag.png");

    public AlchemyBagScreen(AlchemyBagMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 209;
        this.inventoryLabelY = this.imageHeight - 94 + 3;
        this.titleLabelY = 1;
    }

    @Override
    protected void init() {
        super.init();
        this.topPos = (this.height - this.imageHeight) / 2;
    }

    @Override
    public @NotNull ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }
}
