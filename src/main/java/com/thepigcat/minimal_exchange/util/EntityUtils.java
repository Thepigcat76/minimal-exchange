package com.thepigcat.minimal_exchange.util;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;

public final class EntityUtils {
	/**
	 * Returns the resource key for an entity type.
	 */
	public static ResourceKey<EntityType<?>> resourceKey(EntityType<?> type) {
		return BuiltInRegistries.ENTITY_TYPE.getResourceKey(type).orElseThrow();
	}

	/**
	 * Returns the holder reference for an entity type.
	 */
	public static Holder.Reference<EntityType<?>> holder(EntityType<?> type) {
		return BuiltInRegistries.ENTITY_TYPE.getHolderOrThrow(resourceKey(type));
	}
}
