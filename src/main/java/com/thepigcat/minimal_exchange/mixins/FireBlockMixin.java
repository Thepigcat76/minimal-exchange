package com.thepigcat.minimal_exchange.mixins;

import com.thepigcat.minimal_exchange.registries.MEBlocks;
import com.thepigcat.minimal_exchange.registries.MEItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FireBlock.class)
public abstract class FireBlockMixin extends BaseFireBlock {
    private FireBlockMixin() {
        super(null, 0F);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        // TODO: Make this a json based recipe
        if (entity instanceof ItemEntity itemEntity && itemEntity.getItem().is(MEItems.WEAK_COVALENCE_DUST)) {
            level.setBlockAndUpdate(pos, MEBlocks.ALCHEMICAL_FIRE.get().defaultBlockState());
        }

        super.entityInside(state, level, pos, entity);
    }
}
