package com.thepigcat.minimal_exchange.capabilities.matter;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.data.components.MatterComponent;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

public class MatterStorage implements IMatterStorage, INBTSerializable<CompoundTag> {
    private int matter;
    private int matterCapacity;

    public MatterStorage(int initialCapacity) {
        this.matterCapacity = initialCapacity;
    }

    protected void onChanged() {

    }

    @Override
    public int getMatter() {
        return this.matter;
    }

    @Override
    public int getMatterCapacity() {
        return this.matterCapacity;
    }

    @Override
    public void setMatter(int matter) {
        this.matter = matter;
        onChanged();
    }

    @Override
    public void setMatterCapacity(int matterCapacity) {
        this.matterCapacity = matterCapacity;
    }

    @Override
    public @NotNull CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag matterStorageTag = new CompoundTag();
        DataResult<Tag> tagDataResult = MatterComponent.CODEC.encodeStart(NbtOps.INSTANCE, new MatterComponent(matter, matterCapacity));
        if (tagDataResult.isSuccess()) {
            matterStorageTag.put("matter_storage", tagDataResult.getOrThrow());
        } else {
            DataResult.Error<Tag> tagError = tagDataResult.error().get();
            MinimalExchange.LOGGER.error("Error encoding matter storage: {}", tagError.message());
        }
        return matterStorageTag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        CompoundTag matterStorageTag = nbt.getCompound("matter_storage");
        DataResult<Pair<MatterComponent, Tag>> dataResult = MatterComponent.CODEC.decode(NbtOps.INSTANCE, matterStorageTag);
        if (dataResult.isSuccess()) {
            MatterComponent matterComponent = dataResult.getOrThrow().getFirst();
            this.matter = matterComponent.matter();
            this.matterCapacity = matterComponent.matterCapacity();
        } else {
            DataResult.Error<Pair<MatterComponent, Tag>> error = dataResult.error().get();
            MinimalExchange.LOGGER.error("Error decoding matter storage: {}", error.message());
        }
    }
}
