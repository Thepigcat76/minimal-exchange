package com.thepigcat.minimal_exchange.api.blockentities;

import com.thepigcat.minimal_exchange.capabilities.matter.IMatterStorage;
import com.thepigcat.minimal_exchange.capabilities.matter.MatterStorage;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class ContainerBlockEntity extends BlockEntity {
    private @Nullable ItemStackHandler itemHandler;
    private @Nullable FluidTank fluidTank;
    private @Nullable MatterStorage matterStorage;

    public ContainerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public void commonTick() {
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    public IFluidHandler getFluidHandler() {
        return fluidTank;
    }

    public @Nullable IMatterStorage getMatterStorage() {
        return matterStorage;
    }

    protected ItemStackHandler getItemStackHandler() {
        return itemHandler;
    }

    protected FluidTank getFluidTank() {
        return fluidTank;
    }

    protected MatterStorage getMatterStorageImpl() {
        return matterStorage;
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(nbt, provider);
        if (this.getFluidTank() != null)
            this.getFluidTank().readFromNBT(provider, nbt);
        if (this.getItemStackHandler() != null)
            this.getItemStackHandler().deserializeNBT(provider, nbt.getCompound("itemhandler"));
        if (this.getMatterStorageImpl() != null)
            this.getMatterStorageImpl().deserializeNBT(provider, nbt.getCompound("matter_storage"));
        loadData(nbt, provider);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(nbt, provider);
        if (getFluidTank() != null)
            getFluidTank().writeToNBT(provider, nbt);
        if (getItemStackHandler() != null)
            nbt.put("itemhandler", getItemStackHandler().serializeNBT(provider));
        if (getMatterStorageImpl() != null)
            nbt.put("matter_storage", getMatterStorageImpl().serializeNBT(provider));
        saveData(nbt, provider);
    }

    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
    }

    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
    }

    protected final void addItemHandler(int slots) {
        addItemHandler(slots, (slot, itemStack) -> true);
    }

    protected final void addItemHandler(int slots, int slotLimit) {
        addItemHandler(slots, slotLimit, (slot, itemStack) -> true);
    }

    protected final void addItemHandler(int slots, BiPredicate<Integer, ItemStack> validation) {
        addItemHandler(slots, 64, validation);
    }

    protected final void addItemHandler(int slots, int slotLimit, BiPredicate<Integer, ItemStack> validation) {
        this.itemHandler = new ItemStackHandler(slots) {
            @Override
            protected void onContentsChanged(int slot) {
                update();
                setChanged();
                onItemsChanged(slot);
                invalidateCapabilities();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return validation.test(slot, stack);
            }

            @Override
            public int getSlotLimit(int slot) {
                return slotLimit;
            }

            @Override
            public int getSlots() {
                return slots;
            }
        };
    }

    protected final void addFluidTank(int capacityInMb) {
        addFluidTank(capacityInMb, ignored -> true);
    }

    protected final void addFluidTank(int capacityInMb, Predicate<FluidStack> validation) {
        this.fluidTank = new FluidTank(capacityInMb) {
            @Override
            protected void onContentsChanged() {
                update();
                setChanged();
                onFluidChanged();
            }

            @Override
            public boolean isFluidValid(FluidStack stack) {
                return validation.test(stack);
            }
        };
    }

    protected final void addMatterStorage(int matterCapacity) {
        this.matterStorage = new MatterStorage(matterCapacity) {
            @Override
            protected void onChanged() {
                update();
                setChanged();
                onMatterChanged();
            }
        };
    }

    private void update() {
        if (!level.isClientSide()) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    protected void onItemsChanged(int slot) {
    }

    protected void onFluidChanged() {
    }

    protected void onMatterChanged() {
    }

    public void drop() {
        ItemStack[] stacks = getItemHandlerStacks();
        if (stacks != null) {
            SimpleContainer inventory = new SimpleContainer(stacks);
            Containers.dropContents(this.level, this.worldPosition, inventory);
        }
    }

    public @Nullable ItemStack[] getItemHandlerStacks() {
        IItemHandler itemStackHandler = getItemHandler();

        if (itemStackHandler == null) return null;

        ItemStack[] itemStacks = new ItemStack[itemStackHandler.getSlots()];
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            itemStacks[i] = itemStackHandler.getStackInSlot(i);
        }
        return itemStacks;
    }

    public List<ItemStack> getItemHandlerStacksList() {
        IItemHandler itemStackHandler = getItemHandler();

        if (itemStackHandler == null) return null;

        int slots = itemStackHandler.getSlots();
        ObjectList<ItemStack> itemStacks = new ObjectArrayList<>(slots);
        for (int i = 0; i < slots; i++) {
            ItemStack stack = itemStackHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                itemStacks.add(stack);
            }
        }
        return itemStacks;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveWithoutMetadata(provider);
    }
}
