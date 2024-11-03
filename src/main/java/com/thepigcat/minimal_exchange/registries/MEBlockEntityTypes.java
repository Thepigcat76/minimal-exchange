package com.thepigcat.minimal_exchange.registries;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.content.blockentities.ExchangePylonBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class MEBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MinimalExchange.MODID);

    public static final Supplier<BlockEntityType<ExchangePylonBlockEntity>> EXCHANGE_PYLON =
            BLOCK_ENTITIES.register("exchange_pylon", () ->
                    BlockEntityType.Builder.of(ExchangePylonBlockEntity::new,
                            MEBlocks.EXCHANGE_PYLON.get()).build(null));
}
