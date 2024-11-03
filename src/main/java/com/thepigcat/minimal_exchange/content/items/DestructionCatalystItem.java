package com.thepigcat.minimal_exchange.content.items;

import com.thepigcat.minimal_exchange.api.items.SimpleMatterItem;
import com.thepigcat.minimal_exchange.capabilities.MECapabilities;
import com.thepigcat.minimal_exchange.data.MEDataComponents;
import com.thepigcat.minimal_exchange.capabilities.matter.IMatterStorage;
import com.thepigcat.minimal_exchange.data.components.MatterComponent;
import com.thepigcat.minimal_exchange.registries.MESoundEvents;
import com.thepigcat.minimal_exchange.util.LevelUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.*;

public class DestructionCatalystItem extends SimpleMatterItem {
    public DestructionCatalystItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getMatterCapacity(ItemStack itemStack) {
        return 2000;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack itemStack = context.getItemInHand();
        Direction facing = context.getClickedFace();
        BlockPos clickedPos = context.getClickedPos();
        Level level = context.getLevel();

        Iterator<BlockPos> blockPositions = LevelUtils.getPositionsFromBox(LevelUtils.getAABBbox(clickedPos, facing, 3)).iterator();

        List<ItemStack> items = new ArrayList<>();

        IMatterStorage matterStorage = itemStack.getCapability(MECapabilities.MatterStorage.ITEM);

        if (matterStorage.getMatter() > 0) {
            level.playSound(null, clickedPos, MESoundEvents.DESTROY.get(), SoundSource.BLOCKS);

            int blocks = 0;
            if (level instanceof ServerLevel serverLevel) {
                BlockPos curPos = blockPositions.next();
                for (int i = 0; i < matterStorage.getMatter(); i++) {
                    BlockState blockState = level.getBlockState(curPos);
                    if (!blockState.isEmpty() && blockState.getBlock().defaultDestroyTime() > 0) {
                        level.removeBlock(curPos, false);
                        level.sendBlockUpdated(curPos, blockState, blockState, 11);
                        List<ItemStack> drops = blockState.getDrops(new LootParams.Builder(serverLevel)
                                .withParameter(LootContextParams.ORIGIN, clickedPos.getCenter())
                                .withParameter(LootContextParams.TOOL, Items.NETHERITE_PICKAXE.getDefaultInstance()));
                        items.addAll(drops);
                        blocks++;
                    }

                    if (blockPositions.hasNext()) {
                        curPos = blockPositions.next();
                    } else {
                        break;
                    }
                }

                List<ItemStack> mergedList = mergeStacks(items);
                for (ItemStack item : mergedList) {
                    level.addFreshEntity(new ItemEntity(level, curPos.getX(), curPos.getY(), curPos.getZ(), item));
                }
            }

            matterStorage.extractMatter(blocks, false);
            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }

    public static List<ItemStack> mergeStacks(List<ItemStack> inputStacks) {
        // Use a map to group ItemStacks by their item and components
        Map<ItemStack, Integer> stackMap = new HashMap<>();

        // Loop over input stacks and accumulate counts in the map
        for (ItemStack stack : inputStacks) {
            boolean found = false;

            // Check if an equivalent stack is already in the map
            for (ItemStack keyStack : stackMap.keySet()) {
                if (ItemStack.isSameItemSameComponents(stack, keyStack)) {
                    stackMap.put(keyStack, stackMap.get(keyStack) + stack.getCount());
                    found = true;
                    break;
                }
            }

            // If no equivalent stack found, add it as a new entry
            if (!found) {
                stackMap.put(stack.copy(), stack.getCount());
            }
        }

        // Convert the map into a result list of merged ItemStacks
        List<ItemStack> result = new ArrayList<>();

        for (Map.Entry<ItemStack, Integer> entry : stackMap.entrySet()) {
            ItemStack stack = entry.getKey();
            int totalCount = entry.getValue();

            // Split stacks into groups of MAX_STACK_SIZE
            while (totalCount > 0) {
                int countToAdd = Math.min(totalCount, stack.getMaxStackSize());
                ItemStack newStack = stack.copy();
                newStack.setCount(countToAdd);
                result.add(newStack);
                totalCount -= countToAdd;
            }
        }

        return result;
    }

    public static float matterAmount(ItemStack stack) {
        IMatterStorage matterComponent = stack.getCapability(MECapabilities.MatterStorage.ITEM);
        return (float) matterComponent.getMatter() / matterComponent.getMatterCapacity() * 4;
    }
}
