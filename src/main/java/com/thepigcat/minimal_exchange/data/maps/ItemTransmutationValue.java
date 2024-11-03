package com.thepigcat.minimal_exchange.data.maps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public record ItemTransmutationValue(ItemStack result, int inputCount) {
    public static final Codec<ItemTransmutationValue> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            ItemStack.CODEC.fieldOf("result").forGetter(ItemTransmutationValue::result),
            Codec.INT.fieldOf("inputCount").forGetter(ItemTransmutationValue::inputCount)
    ).apply(builder, ItemTransmutationValue::new));
}
