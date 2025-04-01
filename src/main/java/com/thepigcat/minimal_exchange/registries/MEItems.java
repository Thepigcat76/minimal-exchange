package com.thepigcat.minimal_exchange.registries;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.content.items.*;
import com.thepigcat.minimal_exchange.data.MEDataComponents;
import com.thepigcat.minimal_exchange.data.components.MatterComponent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class MEItems {
    public static final List<ItemLike> TAB_ITEMS = new ArrayList<>();
    public static final List<DeferredItem<BlockItem>> BLOCK_ITEMS = new ArrayList<>();

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MinimalExchange.MODID);

    public static final DeferredItem<Item> MINIUM_SHARD = register("minium_shard",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> IRON_BAND = register("iron_band",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<InertStoneItem> INERT_STONE = register("inert_stone",
            () -> new InertStoneItem(new Item.Properties()));
    public static final DeferredItem<Item> WEAK_COVALENCE_DUST = register("weak_covalence_dust",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> STRONG_COVALENCE_DUST = register("strong_covalence_dust",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<TransmutationStoneItem> TRANSMUTATION_STONE = register("transmutation_stone",
            () -> new TransmutationStoneItem(new Item.Properties()
                    .component(MEDataComponents.MATTER, 0)
                    .stacksTo(1)));
    public static final DeferredItem<DiviningRodItem> DIVINING_ROD = register("divining_rod",
            () -> new DiviningRodItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<EnhancedDiviningRodItem> ENHANCED_DIVING_ROD = register("enhanced_divining_rod",
            () -> new EnhancedDiviningRodItem(new Item.Properties()
                    .component(MEDataComponents.MATTER, 0)
                    .stacksTo(1)));
    public static final DeferredItem<AlchemyBagItem> ALCHEMY_BAG = register("alchemy_bag",
            () -> new AlchemyBagItem(new Item.Properties()
                    .component(DataComponents.DYED_COLOR, new DyedItemColor(FastColor.ARGB32.color(255, 255, 255), false))
                    .component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
                    .stacksTo(1)), false);
    public static final DeferredItem<Item> DESTRUCTIVE_AURA_TALISMAN = register("destructive_aura_talisman",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> GROWTH_RING = register("growth_ring",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<DestructionCatalystItem> DESTRUCTION_CATALYST = register("destruction_catalyst",
            () -> new DestructionCatalystItem(new Item.Properties()
                    .component(MEDataComponents.MATTER, 0)
                    .stacksTo(1)));

    private static <T extends Item> DeferredItem<T> register(String name, Supplier<T> itemSupplier, boolean addToTab) {
        DeferredItem<T> item = ITEMS.register(name, itemSupplier);
        if (addToTab) {
            TAB_ITEMS.add(item);
        }
        return item;
    }

    private static <T extends Item> DeferredItem<T> register(String name, Supplier<T> itemSupplier) {
        return register(name, itemSupplier, true);
    }
}
