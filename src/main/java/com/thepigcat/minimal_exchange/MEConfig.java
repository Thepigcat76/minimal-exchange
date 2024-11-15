package com.thepigcat.minimal_exchange;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = MinimalExchange.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class MEConfig {
	private static final ModConfigSpec.Builder BUILDER;

	private static final ModConfigSpec.IntValue TRANSMUTATION_STONE_MATTER_CAPACITY;
	private static final ModConfigSpec.IntValue DESTRUCTION_CATALYST_MATTER_CAPACITY;
	private static final ModConfigSpec.IntValue EXCHANGE_PYLON_MATTER_CAPACITY;

	static final ModConfigSpec SPEC;

	static {
		BUILDER = new ModConfigSpec.Builder();

		{
			BUILDER.push("matter_capacity");

			TRANSMUTATION_STONE_MATTER_CAPACITY = BUILDER
					.comment("The matter capacity for the Transmutation Stone")
					.defineInRange("transmutation_stone", 3000, 1, Integer.MAX_VALUE);

			DESTRUCTION_CATALYST_MATTER_CAPACITY = BUILDER
					.comment("The matter capacity for the Destruction Catalyst")
					.defineInRange("destruction_catalyst", 2000, 1, Integer.MAX_VALUE);

			EXCHANGE_PYLON_MATTER_CAPACITY = BUILDER
					.comment("The matter capacity for the Exchange Pylon")
					.defineInRange("exchange_pylon", 100, 1, Integer.MAX_VALUE);

			BUILDER.pop();
		}

		SPEC = BUILDER.build();
	}

	public static int transmutationStoneMatterCapacity;
	public static int destructionCatalystMatterCapacity;
	public static int exchangePylonMatterCapacity;

	@SubscribeEvent
	private static void onLoad(ModConfigEvent event) {
		transmutationStoneMatterCapacity = TRANSMUTATION_STONE_MATTER_CAPACITY.get();
		destructionCatalystMatterCapacity = DESTRUCTION_CATALYST_MATTER_CAPACITY.get();
		exchangePylonMatterCapacity = EXCHANGE_PYLON_MATTER_CAPACITY.get();
	}
}
