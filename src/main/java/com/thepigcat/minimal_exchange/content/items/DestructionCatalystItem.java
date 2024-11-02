package com.thepigcat.minimal_exchange.content.items;

import com.thepigcat.minimal_exchange.data.MEDataComponents;
import com.thepigcat.minimal_exchange.data.components.MatterComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DestructionCatalystItem extends SimpleMatterItem {
    public DestructionCatalystItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getMatterCapacity(ItemStack itemStack) {
        return 100;
    }

    public static float matterAmount(ItemStack stack) {
        MatterComponent matterComponent = stack.get(MEDataComponents.MATTER);
        return (float) matterComponent.getMatter() / matterComponent.getMatterCapacity() * 4;
    }
}
