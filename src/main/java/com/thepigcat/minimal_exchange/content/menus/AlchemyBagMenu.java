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

        addPlayerInventory(inventory, 83 + 44);
        addPlayerHotbar(inventory, 141 + 44);

        int y = 10;
        for (int i = 0; i < 6; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new SlotItemHandler(itemHandler, l + i * 9, 8 + l * 18, y + i * 18));
            }
        }
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
