package com.thepigcat.minimal_exchange.api.items;

import net.minecraft.world.item.ItemStack;

public interface IMatterItem {
    int getMatterCapacity(ItemStack itemStack);
}
