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
    private static final ModConfigSpec.IntValue ENHANCED_DIVINING_ROD_MATTER_CAPACITY;
    private static final ModConfigSpec.IntValue EXCHANGE_PYLON_MATTER_CAPACITY;
    private static final ModConfigSpec.IntValue MINIUM_SHARD_DROP_CHANCE;

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

            ENHANCED_DIVINING_ROD_MATTER_CAPACITY = BUILDER
                    .comment("The matter capacity for the Enhanced Divining Rod")
                    .defineInRange("enhanced_divining", 2000, 1, Integer.MAX_VALUE);

            EXCHANGE_PYLON_MATTER_CAPACITY = BUILDER
                    .comment("The matter capacity for the Exchange Pylon")
                    .defineInRange("exchange_pylon", 100, 1, Integer.MAX_VALUE);

            BUILDER.pop();
        }

        {
            BUILDER.push("misc");

            MINIUM_SHARD_DROP_CHANCE = BUILDER
                    .comment("The chance for mobs to drop a minium shard. Set to 0 to disable minium shard dropping")
                    .defineInRange("minium_shard_drop_chance", 5, 0, 100);

            BUILDER.pop();
        }

        SPEC = BUILDER.build();
    }

    public static int transmutationStoneMatterCapacity;
    public static int destructionCatalystMatterCapacity;
    public static int enhancedDiviningRodMatterCapacity;
    public static int exchangePylonMatterCapacity;
    public static int miniumShardDropChance;

    @SubscribeEvent
    private static void onLoad(ModConfigEvent event) {
        transmutationStoneMatterCapacity = TRANSMUTATION_STONE_MATTER_CAPACITY.get();
        destructionCatalystMatterCapacity = DESTRUCTION_CATALYST_MATTER_CAPACITY.get();
        enhancedDiviningRodMatterCapacity = ENHANCED_DIVINING_ROD_MATTER_CAPACITY.get();
        exchangePylonMatterCapacity = EXCHANGE_PYLON_MATTER_CAPACITY.get();
        miniumShardDropChance = MINIUM_SHARD_DROP_CHANCE.get();
    }
}
