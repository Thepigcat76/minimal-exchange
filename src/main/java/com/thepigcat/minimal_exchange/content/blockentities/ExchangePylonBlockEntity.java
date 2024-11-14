package com.thepigcat.minimal_exchange.content.blockentities;

import com.thepigcat.minimal_exchange.MEConfig;
import com.thepigcat.minimal_exchange.api.blockentities.ContainerBlockEntity;
import com.thepigcat.minimal_exchange.capabilities.MECapabilities;
import com.thepigcat.minimal_exchange.capabilities.matter.IMatterStorage;
import com.thepigcat.minimal_exchange.content.menus.ExchangePylonMenu;
import com.thepigcat.minimal_exchange.registries.MEBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public class ExchangePylonBlockEntity extends ContainerBlockEntity implements MenuProvider {

    public ExchangePylonBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(MEBlockEntityTypes.EXCHANGE_PYLON.get(), blockPos, blockState);
        addItemHandler(2, (slot, item) -> {
            IMatterStorage matterStorage = item.getCapability(MECapabilities.MatterStorage.ITEM);
            return (slot == 0 && matterStorage == null) || (slot == 1 && matterStorage != null);
        });
        addMatterStorage(MEConfig.exchangePylonMatterCapacity);
    }

    // TODO: Particles
    @Override
    public void commonTick() {
        IItemHandler handler = getItemHandler();
        IMatterStorage matterStorage = getMatterStorage();
        if (!handler.getStackInSlot(0).isEmpty() && matterStorage.getMatter() < matterStorage.getMatterCapacity()) {
            handler.extractItem(0, 1, false);
            matterStorage.receiveMatter(1, false);
            if (level.random.nextInt(8) == 0) {
                level.playSound(null, worldPosition, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS);
            }
        }

        ItemStack stackInChargeSlot = handler.getStackInSlot(1);
        if (!stackInChargeSlot.isEmpty() && matterStorage.getMatter() > 0) {
            IMatterStorage itemMatterStorage = stackInChargeSlot.getCapability(MECapabilities.MatterStorage.ITEM);
            if (itemMatterStorage.getMatter() < itemMatterStorage.getMatterCapacity()) {
                int extracted = matterStorage.extractMatter(1, false);
                itemMatterStorage.receiveMatter(extracted, false);
            }
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Exchange Pylon");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ExchangePylonMenu(containerId, playerInventory, this);
    }
}
