package com.thepigcat.minimal_exchange.capabilities.matter;

import net.minecraft.util.Mth;

public interface IMatterStorage {
    int getMatter();

    int getMatterCapacity();

    void setMatter(int matter);

    void setMatterCapacity(int matterCapacity);

    default int receiveMatter(int toReceive, boolean simulate) {
        if (!canReceive() || toReceive <= 0) {
            return 0;
        }

        int matterReceived = Mth.clamp(this.getMatterCapacity() - this.getMatter(), 0, toReceive);
        if (!simulate)
            setMatter(getMatter() + matterReceived);
        return matterReceived;
    }

    default int extractMatter(int toExtract, boolean simulate) {
        if (!canExtract() || toExtract <= 0) {
            return 0;
        }

        int matterExtracted = Math.min(this.getMatter(), toExtract);
        if (!simulate)
            setMatter(getMatter() - matterExtracted);
        return matterExtracted;
    }

    default boolean canReceive() {
        return true;
    }

    default boolean canExtract() {
        return true;
    }
}
