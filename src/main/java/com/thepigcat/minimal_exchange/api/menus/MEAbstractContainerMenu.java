package com.thepigcat.minimal_exchange.api.menus;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class MEAbstractContainerMenu extends AbstractContainerMenu {
    protected MEAbstractContainerMenu(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    protected void addPlayerInventory(Inventory playerInventory) {
        addPlayerInventory(playerInventory, 83);
    }

    protected void addPlayerHotbar(Inventory playerInventory) {
        addPlayerHotbar(playerInventory, 141);
    }

    protected void addPlayerInventory(Inventory playerInventory, int y) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, y + i * 18));
            }
        }
    }

    protected void addPlayerHotbar(Inventory playerInventory, int y) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, y));
        }
    }

    /**
     * Implementation taken from {@link net.minecraft.world.inventory.ChestMenu#quickMoveStack(Player, int)}
     */
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        int containerRows = 6;
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < containerRows * 9) {
                if (!this.moveItemStackTo(itemstack1, containerRows * 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, containerRows * 9, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }
}
