package com.thepigcat.minimal_exchange.matter;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// Responsible for exposing matter
public final class MatterManager {
    private static final Map<Item, Long> MATTER_VALUES = new HashMap<>();
    private static final Set<Item> ITEMS_WITHOUT_MATTER = new HashSet<>();

    public static long getMatterForItem(ItemLike item) {
        return MATTER_VALUES.getOrDefault(item.asItem(), -1L);
    }

    public static void putMatterForItem(ItemLike item, long matter) {
        MATTER_VALUES.put(item.asItem(), matter);
    }

    public static boolean containsItem(ItemLike item) {
        return MATTER_VALUES.containsKey(item.asItem());
    }

    public static void addItemWithoutMatter(ItemLike item) {
        ITEMS_WITHOUT_MATTER.add(item.asItem());
    }

    public static boolean hasMatter(ItemLike item) {
        return !ITEMS_WITHOUT_MATTER.contains(item.asItem());
    }

}
