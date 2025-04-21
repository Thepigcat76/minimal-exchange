package com.thepigcat.minimal_exchange.registries;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.content.blocks.AlchemicalFireBlock;
import com.thepigcat.minimal_exchange.content.blocks.ExchangePylonBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class MEBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MinimalExchange.MODID);

    public static final DeferredBlock<ExchangePylonBlock> EXCHANGE_PYLON = registerBlockAndItem("exchange_pylon",
            () -> new ExchangePylonBlock(BlockBehaviour.Properties.of()));

    public static final DeferredBlock<Block> ALCHEMICAL_STONE = registerBlockAndItem("alchemical_stone",
            () -> new Block(BlockBehaviour.Properties.of()));

    public static final DeferredBlock<AlchemicalFireBlock> ALCHEMICAL_FIRE = BLOCKS.register("alchemical_fire",
            () -> new AlchemicalFireBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FIRE)));

    private static <T extends Block> DeferredBlock<T> registerBlockAndItem(String name, Supplier<T> blockSup) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, blockSup);
        DeferredItem<BlockItem> blockItem = MEItems.ITEMS.registerSimpleBlockItem(toReturn);
        MEItems.TAB_ITEMS.add(toReturn);
        MEItems.BLOCK_ITEMS.add(blockItem);
        return toReturn;
    }
}
