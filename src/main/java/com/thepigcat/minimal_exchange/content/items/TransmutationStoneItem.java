package com.thepigcat.minimal_exchange.content.items;

import com.thepigcat.minimal_exchange.api.items.SimpleMatterItem;
import com.thepigcat.minimal_exchange.capabilities.MECapabilities;
import com.thepigcat.minimal_exchange.capabilities.matter.IMatterStorage;
import com.thepigcat.minimal_exchange.data.MEDataMaps;
import com.thepigcat.minimal_exchange.data.maps.BlockTransmutationValue;
import com.thepigcat.minimal_exchange.data.maps.EntityTransmutationValue;
import com.thepigcat.minimal_exchange.registries.MESoundEvents;
import com.thepigcat.minimal_exchange.util.RegistryUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TransmutationStoneItem extends SimpleMatterItem {
    public TransmutationStoneItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        BlockState blockState = level.getBlockState(clickedPos);
        ItemStack stack = context.getItemInHand();

        Holder<Block> blockHolder = blockState.getBlockHolder();
        BlockTransmutationValue transmutation = blockHolder.getData(MEDataMaps.BLOCK_TRANSMUTATIONS);
        if (transmutation != null) {
            int matterCost = transmutation.matterCost();
            IMatterStorage matterStorage = stack.getCapability(MECapabilities.MatterStorage.ITEM);
            if (matterStorage.getMatter() >= matterCost) {
                level.setBlockAndUpdate(context.getClickedPos(), transmutation.result().defaultBlockState());
                level.playSound(null, clickedPos, MESoundEvents.TRANSMUTE.get(), SoundSource.PLAYERS, 0.8f, 1);
                matterStorage.extractMatter(matterCost, false);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        Level level = player.level();
        EntityTransmutationValue transmutation = RegistryUtils.holder(BuiltInRegistries.ENTITY_TYPE, target.getType()).getData(MEDataMaps.ENTITY_TRANSMUTATIONS);
        if (transmutation != null) {
            int matterCost = transmutation.matterCost();
            IMatterStorage matterStorage = stack.getCapability(MECapabilities.MatterStorage.ITEM);
            if (target instanceof Mob targetMob && matterStorage.getMatter() >= matterCost) {
                EntityType<?> transmutatedEntityType = transmutation.result();
                Entity transmutatedEntity = transmutatedEntityType.create(level);
                if (transmutatedEntity instanceof Mob) {
                    if (!level.isClientSide()) {
                        targetMob.convertTo((EntityType<? extends Mob>) transmutatedEntityType, true);
                        target.level().playSound(null, target.blockPosition(), MESoundEvents.TRANSMUTE.get(), SoundSource.PLAYERS, 0.8f, 1);
                        matterStorage.extractMatter(matterCost, false);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.FAIL;
    }

    // TODO: Config
    @Override
    public int getMatterCapacity(ItemStack itemStack) {
        return 3000;
    }
}
