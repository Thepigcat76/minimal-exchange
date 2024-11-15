package com.thepigcat.minimal_exchange.content.items;

import com.thepigcat.minimal_exchange.content.menus.AlchemyBagMenu;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class AlchemyBagItem extends Item {
    public AlchemyBagItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        Inventory inventory = player.getInventory();

        int slot = inventory.findSlotMatchingItem(itemStack);
        player.openMenu(new SimpleMenuProvider(
                (containerId, playerInventory, player1) -> new AlchemyBagMenu(containerId, playerInventory, itemStack, slot),
                itemStack.getHoverName()),
                byteBuf -> {
                    ItemStack.STREAM_CODEC.encode(byteBuf, itemStack);
                    byteBuf.writeInt(slot);
                }
        );
        return InteractionResultHolder.success(itemStack);
    }
}
