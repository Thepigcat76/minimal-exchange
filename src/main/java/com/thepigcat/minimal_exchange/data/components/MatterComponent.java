package com.thepigcat.minimal_exchange.data.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

public record MatterComponent(int matter, int matterCapacity){
    public static final Codec<MatterComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.INT.fieldOf("matter").forGetter(MatterComponent::matter),
            Codec.INT.fieldOf("matterCapacity").forGetter(MatterComponent::matterCapacity)
    ).apply(builder, MatterComponent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, MatterComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            MatterComponent::matter,
            ByteBufCodecs.INT,
            MatterComponent::matterCapacity,
            MatterComponent::new
    );

    public static MatterComponent withCapacity(int capacity) {
        return new MatterComponent(0, capacity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MatterComponent that)) return false;
        return matter == that.matter;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(matter);
    }

    @Override
    public String toString() {
        return "MatterComponent{" +
                "matter=" + matter +
                ", matterCapacity=" + matterCapacity +
                '}';
    }
}
