package com.thepigcat.minimal_exchange.networking;

import com.mojang.serialization.Codec;
import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.matter.MatterManager;
import com.thepigcat.minimal_exchange.util.RegistryUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.Map;

public record SyncItemMatterValuesPayload(Map<Item, Integer> map) implements CustomPacketPayload {
    public static final Type<SyncItemMatterValuesPayload> TYPE = new Type<>(MinimalExchange.rl("sync_item_matter_values"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncItemMatterValuesPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, RegistryUtils.registryStreamCodec(BuiltInRegistries.ITEM), ByteBufCodecs.INT),
            SyncItemMatterValuesPayload::map,
            SyncItemMatterValuesPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            for (Map.Entry<Item, Integer> entry : map.entrySet()) {
                MatterManager.putMatterForItem(entry.getKey(), entry.getValue());
            }
        }).exceptionally(e -> {
            MinimalExchange.LOGGER.warn("Failed to handle sync matter values payload");
           return null;
        });
    }
}
