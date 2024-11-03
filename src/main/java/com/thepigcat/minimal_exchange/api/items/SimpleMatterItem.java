package com.thepigcat.minimal_exchange.api.items;

import com.thepigcat.minimal_exchange.capabilities.MECapabilities;
import com.thepigcat.minimal_exchange.capabilities.matter.IMatterStorage;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public abstract class SimpleMatterItem extends Item implements IMatterItem {
    public SimpleMatterItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return FastColor.ARGB32.color(237, 183, 72);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return matterForDurabilityBar(stack);
    }

    private static int matterForDurabilityBar(ItemStack itemStack) {
        IMatterStorage matterStorage = itemStack.getCapability(MECapabilities.MatterStorage.ITEM);
        return Math.round(13.0F - ((1 - ((float) matterStorage.getMatter() / matterStorage.getMatterCapacity())) * 13.0F));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        IMatterStorage matterComponent = stack.getCapability(MECapabilities.MatterStorage.ITEM);
        tooltipComponents.add(Component.translatable("tooltip.minimal_exchange.matter_stored", matterComponent.getMatter(), matterComponent.getMatterCapacity())
                .withColor(getBarColor(stack)));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
