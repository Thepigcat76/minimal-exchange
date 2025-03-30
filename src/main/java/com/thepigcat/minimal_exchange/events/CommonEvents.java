package com.thepigcat.minimal_exchange.events;

import com.thepigcat.minimal_exchange.MEConfig;
import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.registries.MEItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.stream.Stream;

@EventBusSubscriber(modid = MinimalExchange.MODID)
public final class CommonEvents {
    private static final AABB BOX = new AABB(-1.5, -1, -1.5, 1.5, 3, 1.5);

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Monster
                && !entity.level().isClientSide()
                && event.getSource().getEntity() instanceof Player
                && entity.level().random.nextInt(1, 100) < MEConfig.miniumShardDropChance) {
            entity.spawnAtLocation(MEItems.MINIUM_SHARD, 1);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (false) {
            Stream<BlockPos> blockPosStream = BlockPos.betweenClosedStream(BOX.move(event.getEntity().position()));
            Level level = event.getEntity().level();
            blockPosStream.forEach(pos -> {
                if (level.getBlockState(pos).getBlock().defaultDestroyTime() != -1) {
                    level.removeBlock(pos, false);
                }
            });
        }
    }

}
