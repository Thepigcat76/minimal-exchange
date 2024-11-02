package com.thepigcat.minimal_exchange.data;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.data.components.MatterComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class MEDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, MinimalExchange.MODID);

    public static final Supplier<DataComponentType<MatterComponent>> MATTER = DATA_COMPONENTS.register("matter",
            () -> DataComponentType.<MatterComponent>builder().persistent(MatterComponent.CODEC).networkSynchronized(MatterComponent.STREAM_CODEC).build());
}
