package com.thepigcat.minimal_exchange.data.components;

import com.mojang.serialization.Codec;
import com.thepigcat.minimal_exchange.data.capabilities.IMatterStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

public class MatterComponent implements IMatterStorage {
    public static final MatterComponent EMPTY = new MatterComponent(0);

    public static final Codec<MatterComponent> CODEC = Codec.INT.xmap(MatterComponent::new, MatterComponent::matter);
    public static final StreamCodec<ByteBuf, MatterComponent> STREAM_CODEC = ByteBufCodecs.INT.map(MatterComponent::new, MatterComponent::matter);
    private final int matter;

    public MatterComponent(int matter) {
        this.matter = matter;
    }

    @Override
    public int getMatter() {
        return matter;
    }

    @Override
    public int getMatterCapacity() {
        return 0;
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

    public int matter() {
        return matter;
    }

    @Override
    public String toString() {
        return "MatterComponent[" +
                "matter=" + matter + ']';
    }

}
