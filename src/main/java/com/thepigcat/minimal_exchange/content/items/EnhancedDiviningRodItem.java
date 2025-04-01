package com.thepigcat.minimal_exchange.content.items;

import com.thepigcat.minimal_exchange.MEConfig;
import com.thepigcat.minimal_exchange.api.items.IMatterItem;
import com.thepigcat.minimal_exchange.capabilities.MECapabilities;
import com.thepigcat.minimal_exchange.capabilities.matter.IMatterStorage;
import com.thepigcat.minimal_exchange.data.MEDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class EnhancedDiviningRodItem extends DiviningRodItem implements IMatterItem {
    public EnhancedDiviningRodItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getMatterCapacity(ItemStack itemStack) {
        return MEConfig.enhancedDiviningRodMatterCapacity;
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

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        super.onUseTick(level, livingEntity, stack, remainingUseDuration);

        IMatterStorage matterStorage = stack.getCapability(MECapabilities.MatterStorage.ITEM);
        matterStorage.extractMatter(1, false);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.is(newStack.getItem());
    }

    @Override
    public boolean canUse(Player player, ItemStack itemStack) {
        return itemStack.getOrDefault(MEDataComponents.MATTER, 0) >= getUseDuration(itemStack, player);
    }
}
