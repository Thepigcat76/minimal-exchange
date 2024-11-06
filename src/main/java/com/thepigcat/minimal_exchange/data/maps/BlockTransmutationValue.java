package com.thepigcat.minimal_exchange.data.maps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thepigcat.minimal_exchange.util.RegistryUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

public record BlockTransmutationValue(Block result, int matterCost) {
	public static final Codec<BlockTransmutationValue> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			RegistryUtils.registryCodec(BuiltInRegistries.BLOCK).fieldOf("result").forGetter(BlockTransmutationValue::result),
			Codec.INT.optionalFieldOf("matterCost", 1).forGetter(BlockTransmutationValue::matterCost)
	).apply(builder, BlockTransmutationValue::new));
}
