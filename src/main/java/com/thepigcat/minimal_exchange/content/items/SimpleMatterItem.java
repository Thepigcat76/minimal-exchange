package com.thepigcat.minimal_exchange.content.items;

import com.thepigcat.minimal_exchange.data.MECapabilities;
import com.thepigcat.minimal_exchange.data.capabilities.IMatterStorage;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class SimpleMatterItem extends Item implements IMatterItem{
    public SimpleMatterItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return FastColor.ARGB32.color(227, 167, 43);
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
}
