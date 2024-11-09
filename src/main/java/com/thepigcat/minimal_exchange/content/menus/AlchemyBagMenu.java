package com.thepigcat.minimal_exchange.content.menus;

import com.thepigcat.minimal_exchange.api.menus.MEAbstractContainerMenu;
import com.thepigcat.minimal_exchange.registries.MEMenuTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class AlchemyBagMenu extends MEAbstractContainerMenu {
    private final ItemStack itemStack;
    private final Inventory inventory;
    private final IItemHandler itemHandler;

    public AlchemyBagMenu(int containerId, Inventory inventory, ItemStack itemStack) {
        super(MEMenuTypes.ALCHEMY_BAG.get(), containerId);
        this.itemStack = itemStack;
        this.inventory = inventory;
        this.itemHandler = itemStack.getCapability(Capabilities.ItemHandler.ITEM);

        int y = 18;
        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new SlotItemHandler(itemHandler, column + row * 9, 8 + column * 18, y + row * 18));
            }
        }

        addPlayerInventory(inventory, 140);
        addPlayerHotbar(inventory, 198);
    }

    public AlchemyBagMenu(int containerId, Inventory inventory, RegistryFriendlyByteBuf byteBuf) {
        this(containerId, inventory, ItemStack.STREAM_CODEC.decode(byteBuf));
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getMainHandItem().is(itemStack.getItem())
                || player.getOffhandItem().is(itemStack.getItem());
    }
}
