package com.thepigcat.minimal_exchange.client.screen;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.api.client.screens.MEAbstractContainerScreen;
import com.thepigcat.minimal_exchange.capabilities.matter.IMatterStorage;
import com.thepigcat.minimal_exchange.content.menus.ExchangePylonMenu;
import com.thepigcat.minimal_exchange.util.RenderUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class ExchangePylonScreen extends MEAbstractContainerScreen<ExchangePylonMenu> {
    public ExchangePylonScreen(ExchangePylonMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, delta, mouseX, mouseY);
        IMatterStorage matterStorage = menu.getBlockEntity().getMatterStorage();
        int matter = matterStorage.getMatter();
        int matterCapacity = matterStorage.getMatterCapacity();
        RenderUtils.drawCenteredString(guiGraphics, Minecraft.getInstance().font, Component.literal(matter + "/" + matterCapacity + " Matter").withStyle(ChatFormatting.DARK_GRAY), this.width / 2, this.topPos + imageHeight / 4, 0, false);
    }

    @Override
    public @NotNull ResourceLocation getBackgroundTexture() {
        return ResourceLocation.fromNamespaceAndPath(MinimalExchange.MODID, "textures/gui/exchange_pylon.png");
    }
}
