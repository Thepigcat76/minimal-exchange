package com.thepigcat.minimal_exchange.capabilities.matter;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.data.components.MatterComponent;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

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
       CompoundTag tag = new CompoundTag();
       tag.putInt("matter", this.matter);
       return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.matter = nbt.getInt("matter");
    }
}
