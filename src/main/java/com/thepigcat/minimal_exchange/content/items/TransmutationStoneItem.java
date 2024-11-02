package com.thepigcat.minimal_exchange.content.items;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.data.MEDataMaps;
import com.thepigcat.minimal_exchange.registries.MESoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
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

        Holder<Block> blockHolder = blockState.getBlockHolder();
        Block transmutatedBlock = blockHolder.getData(MEDataMaps.BLOCK_TRANSMUTATIONS);
        if (transmutatedBlock != null) {
            level.setBlockAndUpdate(context.getClickedPos(), transmutatedBlock.defaultBlockState());
            level.playSound(null, clickedPos, MESoundEvents.TRANSMUTE.get(), SoundSource.PLAYERS, 0.8f, 1);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        ItemStack itemStack1 = itemStack.copy();
        int damageValue = itemStack1.getDamageValue();
        itemStack1.setDamageValue(damageValue + 1);
        return itemStack1.getDamageValue() < itemStack1.getMaxDamage() ? itemStack1 : ItemStack.EMPTY;
    }

    // TODO: Config
    @Override
    public int getMatterCapacity(ItemStack itemStack) {
        return 3000;
    }
}
