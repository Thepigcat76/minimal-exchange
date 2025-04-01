package com.thepigcat.minimal_exchange.data;

import com.mojang.serialization.Codec;
import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.data.components.MatterComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class MEDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, MinimalExchange.MODID);

    public static final Supplier<DataComponentType<Integer>> MATTER = DATA_COMPONENTS.register("matter",
            () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
}
