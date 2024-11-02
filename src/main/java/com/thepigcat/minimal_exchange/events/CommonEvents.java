package com.thepigcat.minimal_exchange.events;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.registries.MEItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber(modid = MinimalExchange.MODID)
public final class CommonEvents {
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Monster
                && !entity.level().isClientSide()
                && event.getSource().getEntity() instanceof Player
                && entity.level().random.nextInt(25) == 0) {
            entity.spawnAtLocation(MEItems.MINIUM_SHARD, 1);
        }
    }
}
