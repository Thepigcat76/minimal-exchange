package com.thepigcat.minimal_exchange.content.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class InertStoneItem extends Item {
    public InertStoneItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.minimal_exchange.inert_stone").withStyle(ChatFormatting.DARK_GRAY));
    }

    public static float isVibrating(Level level) {
        return level.random.nextInt(0, 42) == 0 ? 1f : 0f;
    }

}
