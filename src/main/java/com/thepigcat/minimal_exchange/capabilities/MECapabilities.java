package com.thepigcat.minimal_exchange.capabilities;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.capabilities.matter.IMatterStorage;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import org.jetbrains.annotations.Nullable;

public final class MECapabilities {
    public static final class MatterStorage {
        private static final ResourceLocation MATTER_STORAGE = ResourceLocation.fromNamespaceAndPath(MinimalExchange.MODID, "matter_storage");

        public static final BlockCapability<IMatterStorage, @Nullable Direction> BLOCK = BlockCapability.createSided(MATTER_STORAGE, IMatterStorage.class);
        public static final EntityCapability<IMatterStorage, @Nullable Direction> ENTITY = EntityCapability.createSided(MATTER_STORAGE, IMatterStorage.class);
        public static final ItemCapability<IMatterStorage, Void> ITEM = ItemCapability.createVoid(MATTER_STORAGE, IMatterStorage.class);
    }
}
