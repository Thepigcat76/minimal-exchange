package com.thepigcat.minimal_exchange.content.blocks;

import com.mojang.serialization.MapCodec;
import com.thepigcat.minimal_exchange.api.blockentities.ContainerBlockEntity;
import com.thepigcat.minimal_exchange.api.blocks.ContainerBlock;
import com.thepigcat.minimal_exchange.registries.MEBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class ExchangePylonBlock extends ContainerBlock {
    public static final VoxelShape SHAPE = Stream.of(
            Block.box(5, 0, 5, 11, 2, 11),
            Block.box(7, 2, 7, 9, 14, 9),
            Block.box(6, 14, 6, 10, 16, 10),
            Block.box(6, 9, 6, 10, 10, 10),
            Block.box(6, 5, 6, 10, 6, 10)
    ).reduce(Shapes::or).get();

    public ExchangePylonBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean tickingEnabled() {
        return true;
    }

    @Override
    public BlockEntityType<? extends ContainerBlockEntity> getBlockEntityType() {
        return MEBlockEntityTypes.EXCHANGE_PYLON.get();
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(ExchangePylonBlock::new);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, BlockHitResult p_60508_) {
        BlockEntity blockEntity = p_60504_.getBlockEntity(p_60505_);
        if (blockEntity instanceof MenuProvider menuProvider) {
            p_60506_.openMenu(menuProvider, p_60505_);
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(p_60503_, p_60504_, p_60505_, p_60506_, p_60508_);
    }
}
