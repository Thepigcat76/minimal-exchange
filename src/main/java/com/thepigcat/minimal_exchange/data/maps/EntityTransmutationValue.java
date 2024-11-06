package com.thepigcat.minimal_exchange.data.maps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thepigcat.minimal_exchange.util.RegistryUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

public record EntityTransmutationValue(EntityType<?> result, int matterCost) {
	public static final Codec<EntityTransmutationValue> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			RegistryUtils.registryCodec(BuiltInRegistries.ENTITY_TYPE).fieldOf("result").forGetter(EntityTransmutationValue::result),
			Codec.INT.optionalFieldOf("matterCost", 1).forGetter(EntityTransmutationValue::matterCost)
	).apply(builder, EntityTransmutationValue::new));
}
