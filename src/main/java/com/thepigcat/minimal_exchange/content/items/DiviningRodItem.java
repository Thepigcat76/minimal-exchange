package com.thepigcat.minimal_exchange.content.items;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.Tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DiviningRodItem extends Item {
    private static final AABB BOX = new AABB(-3, -64, -3, 3, 3, 3);

    public DiviningRodItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (canUse(player, player.getItemInHand(usedHand))) {
            player.startUsingItem(usedHand);
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (livingEntity instanceof Player player) {
            player.getCooldowns().addCooldown(stack.getItem(), 100);
        }
        Map<BlockPos, BlockState> ores = new HashMap<>();
        Stream<BlockPos> blockPosStream = BlockPos.betweenClosedStream(BOX.move(livingEntity.position()));
        blockPosStream.forEach(pos -> {
            BlockState blockState = level.getBlockState(pos);
            if (blockState.is(Tags.Blocks.ORES)) {
                ores.put(pos, blockState);
            }
        });
        // TODO: Do something with this
        for (Map.Entry<BlockPos, BlockState> entry : ores.entrySet()) {
            BlockPos pos = entry.getKey();
            livingEntity.sendSystemMessage(entry.getValue().getBlock().getName().append(" at ").append(Component.literal("%d %d %d".formatted(pos.getX(), pos.getY(), pos.getZ()))));
        }
        return super.finishUsingItem(stack, level, livingEntity);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 100;
    }

    public boolean canUse(Player player, ItemStack itemStack) {
        return true;
    }

}
