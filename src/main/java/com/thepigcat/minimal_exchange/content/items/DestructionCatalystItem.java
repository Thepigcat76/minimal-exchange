package com.thepigcat.minimal_exchange.content.items;

import com.thepigcat.minimal_exchange.api.items.SimpleMatterItem;
import com.thepigcat.minimal_exchange.data.MECapabilities;
import com.thepigcat.minimal_exchange.data.MEDataComponents;
import com.thepigcat.minimal_exchange.data.capabilities.IMatterStorage;
import com.thepigcat.minimal_exchange.data.components.MatterComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class DestructionCatalystItem extends SimpleMatterItem {
    public DestructionCatalystItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getMatterCapacity(ItemStack itemStack) {
        return 100;
    }

    public static float matterAmount(ItemStack stack) {
        IMatterStorage matterComponent = stack.getCapability(MECapabilities.MatterStorage.ITEM);
        return (float) matterComponent.getMatter() / matterComponent.getMatterCapacity() * 4;
    }
}
