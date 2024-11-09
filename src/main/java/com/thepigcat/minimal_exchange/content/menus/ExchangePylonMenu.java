package com.thepigcat.minimal_exchange.content.menus;

import com.thepigcat.minimal_exchange.api.menus.MEAbstractContainerMenu;
import com.thepigcat.minimal_exchange.capabilities.MECapabilities;
import com.thepigcat.minimal_exchange.content.blockentities.ExchangePylonBlockEntity;
import com.thepigcat.minimal_exchange.registries.MEBlocks;
import com.thepigcat.minimal_exchange.registries.MEMenuTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;

public class ExchangePylonMenu extends MEAbstractContainerMenu {
    private final ContainerLevelAccess containerLevelAccess;
    private final ExchangePylonBlockEntity blockEntity;

    public ExchangePylonMenu(int containerId, Inventory inventory, ExchangePylonBlockEntity blockEntity) {
        super(MEMenuTypes.EXCHANGE_PYLON.get(), containerId);
        this.containerLevelAccess = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
        this.blockEntity = blockEntity;

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

        addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 0, 8 + 4 * 18, 54));
        addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 1, 8 + 4 * 18, 18));
    }

    public ExchangePylonMenu(int containerId, Inventory inventory, RegistryFriendlyByteBuf friendlyByteBuf) {
        this(containerId, inventory, (ExchangePylonBlockEntity) inventory.player.level().getBlockEntity(friendlyByteBuf.readBlockPos()));
    }

    public ExchangePylonBlockEntity getBlockEntity() {
        return blockEntity;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(containerLevelAccess, player, MEBlocks.EXCHANGE_PYLON.get());
    }
}
