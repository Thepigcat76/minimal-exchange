package com.thepigcat.minimal_exchange.data;

import com.mojang.serialization.Codec;
import com.thepigcat.minimal_exchange.MinimalExchange;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class MEAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MinimalExchange.MODID);

    public static final Supplier<AttachmentType<Integer>> MATTER = ATTACHMENTS.register("matter", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).copyOnDeath().build());
}
