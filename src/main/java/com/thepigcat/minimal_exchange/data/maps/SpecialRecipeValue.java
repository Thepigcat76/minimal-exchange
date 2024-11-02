package com.thepigcat.minimal_exchange.data.maps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Map;

public record SpecialRecipeValue(List<String> pattern, Map<String, Ingredient> definitions, ItemStack result) {
    public static final Codec<SpecialRecipeValue> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.STRING.listOf().fieldOf("pattern").forGetter(SpecialRecipeValue::pattern),
            Codec.unboundedMap(Codec.STRING, Ingredient.CODEC).fieldOf("definitions").forGetter(SpecialRecipeValue::definitions),
            ItemStack.CODEC.fieldOf("result").forGetter(SpecialRecipeValue::result)
    ).apply(builder, SpecialRecipeValue::new));
}
