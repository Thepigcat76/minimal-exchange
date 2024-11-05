package com.thepigcat.minimal_exchange.content.items;

import com.thepigcat.minimal_exchange.api.items.SimpleMatterItem;
import com.thepigcat.minimal_exchange.capabilities.MECapabilities;
import com.thepigcat.minimal_exchange.capabilities.matter.IMatterStorage;
import com.thepigcat.minimal_exchange.data.MEDataComponents;
import com.thepigcat.minimal_exchange.data.MEDataMaps;
import com.thepigcat.minimal_exchange.data.components.MatterComponent;
import com.thepigcat.minimal_exchange.registries.MESoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

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

        IMatterStorage matterStorage = stack.getCapability(MECapabilities.MatterStorage.ITEM);
        if (matterStorage.getMatter() > 0) {
            Holder<Block> blockHolder = blockState.getBlockHolder();
            Block transmutatedBlock = blockHolder.getData(MEDataMaps.BLOCK_TRANSMUTATIONS);
            if (transmutatedBlock != null) {
                level.setBlockAndUpdate(context.getClickedPos(), transmutatedBlock.defaultBlockState());
                level.playSound(null, clickedPos, MESoundEvents.TRANSMUTE.get(), SoundSource.PLAYERS, 0.8f, 1);
                // TODO: CONFIG
                matterStorage.extractMatter(1, false);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public @NotNull ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        ItemStack itemStack1 = itemStack.copy();
        IMatterStorage matterStorage = itemStack1.getCapability(MECapabilities.MatterStorage.ITEM);
        itemStack1.set(MEDataComponents.MATTER, new MatterComponent(matterStorage.getMatter() - 1, matterStorage.getMatterCapacity()));
        return itemStack1;
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);
    }

    // TODO: Config
    @Override
    public int getMatterCapacity(ItemStack itemStack) {
        return 3000;
    }
}
