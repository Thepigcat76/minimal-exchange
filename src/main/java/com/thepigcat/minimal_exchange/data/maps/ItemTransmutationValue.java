package com.thepigcat.minimal_exchange.data.maps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public record ItemTransmutationValue(ItemStack result, int inputCount, int matterCost) {
    public static final Codec<ItemTransmutationValue> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            ItemStack.CODEC.fieldOf("result").forGetter(ItemTransmutationValue::result),
            Codec.INT.fieldOf("inputCount").forGetter(ItemTransmutationValue::inputCount),
            Codec.INT.optionalFieldOf("matterCost", 1).forGetter(ItemTransmutationValue::matterCost)
    ).apply(builder, ItemTransmutationValue::new));
}
