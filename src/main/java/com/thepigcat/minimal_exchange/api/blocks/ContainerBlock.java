package com.thepigcat.minimal_exchange.api.blocks;

import com.thepigcat.minimal_exchange.api.blockentities.ContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ContainerBlock extends BaseEntityBlock {
    public ContainerBlock(Properties properties) {
        super(properties);
    }

    public abstract boolean tickingEnabled();

    public abstract BlockEntityType<? extends ContainerBlockEntity> getBlockEntityType();

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return getBlockEntityType().create(blockPos, blockState);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (!tickingEnabled()) return null;

        return createTickerHelper(blockEntityType, getBlockEntityType(), (level1, pos1, state1, entity1) -> entity1.commonTick());
    }

    @Override
    public void onRemove(BlockState p_60515_, Level level, BlockPos pos, BlockState p_60518_, boolean p_60519_) {
        if (!p_60515_.is(p_60518_.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ContainerBlockEntity containerBlockEntity && containerBlockEntity.getItemHandler() != null) {
                containerBlockEntity.drop();
            }
        }
        super.onRemove(p_60515_, level, pos, p_60518_, p_60519_);
    }
}