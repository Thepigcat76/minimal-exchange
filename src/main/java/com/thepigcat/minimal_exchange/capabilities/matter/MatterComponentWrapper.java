package com.thepigcat.minimal_exchange.capabilities.matter;

import com.thepigcat.minimal_exchange.data.MEDataComponents;
import com.thepigcat.minimal_exchange.data.components.MatterComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class MatterComponentWrapper implements IMatterStorage {
    private final ItemStack itemStack;
    private final int capacity;

    public MatterComponentWrapper(ItemStack itemStack, int capacity) {
        this.itemStack = itemStack;
        this.capacity = capacity;
    }

    @Override
    public int getMatter() {
        return getMatterComponent().matter();
    }

    private @Nullable MatterComponent getMatterComponent() {
        return itemStack.get(MEDataComponents.MATTER);
    }

    @Override
    public int getMatterCapacity() {
        return capacity;
    }

    @Override
    public void setMatter(int matter) {
        itemStack.set(MEDataComponents.MATTER, new MatterComponent(matter));
    }

    @Override
    public void setMatterCapacity(int matterCapacity) {
    }
}
