package com.thepigcat.minimal_exchange.data.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thepigcat.minimal_exchange.data.capabilities.IMatterStorage;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

public record MatterComponent(int matter, int matterCapacity) implements IMatterStorage {
    public static final MatterComponent EMPTY = new MatterComponent(0, 0);

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

    @Override
    public int getMatter() {
        return matter;
    }

    @Override
    public int getMatterCapacity() {
        return matterCapacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MatterComponent that)) return false;
        return matter == that.matter && matterCapacity == that.matterCapacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(matter, matterCapacity);
    }
}
