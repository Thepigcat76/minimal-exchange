package com.thepigcat.minimal_exchange.data.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

public record MatterComponent(int matter) {
    public static final Codec<MatterComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.INT.fieldOf("matter").forGetter(MatterComponent::matter)
    ).apply(builder, MatterComponent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, MatterComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            MatterComponent::matter,
            MatterComponent::new
    );

    public MatterComponent() {
        this(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MatterComponent(int matter1))) return false;
        return matter == matter1;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(matter);
    }
}
