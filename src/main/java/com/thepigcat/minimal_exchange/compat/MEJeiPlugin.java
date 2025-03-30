package com.thepigcat.minimal_exchange.compat;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.data.MEDataMaps;
import com.thepigcat.minimal_exchange.data.maps.BlockTransmutationValue;
import com.thepigcat.minimal_exchange.data.maps.EntityTransmutationValue;
import com.thepigcat.minimal_exchange.data.maps.ItemTransmutationValue;
import com.thepigcat.minimal_exchange.registries.MEItems;
import it.unimi.dsi.fastutil.Pair;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class MEJeiPlugin implements IModPlugin {
    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(MinimalExchange.MODID, "jei_plugin");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(RecipeTypes.CRAFTING, getItemTransmutations());
        registration.addRecipes(BlockTransmutationCategory.BLOCK_TRANSMUTATION_TYPE, getBlockTransmutations());
        registration.addRecipes(EntityTransmutationCategory.ENTITY_TRANSMUTATION_TYPE, getEntityTransmutations());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(MEItems.TRANSMUTATION_STONE, BlockTransmutationCategory.BLOCK_TRANSMUTATION_TYPE);
        registration.addRecipeCatalyst(MEItems.TRANSMUTATION_STONE, EntityTransmutationCategory.ENTITY_TRANSMUTATION_TYPE);
    }

    private static List<EntityTransmutationRecipe> getEntityTransmutations() {
        Map<ResourceKey<EntityType<?>>, EntityTransmutationValue> transmutations = BuiltInRegistries.ENTITY_TYPE.getDataMap(MEDataMaps.ENTITY_TRANSMUTATIONS);
        List<EntityTransmutationRecipe> recipes = new ArrayList<>();
        for (Map.Entry<ResourceKey<EntityType<?>>, EntityTransmutationValue> transmutation : transmutations.entrySet()) {
            recipes.add(new EntityTransmutationRecipe(BuiltInRegistries.ENTITY_TYPE.get(transmutation.getKey()), transmutation.getValue()));
        }
        return recipes;
    }

    private static List<BlockTransmutationRecipe> getBlockTransmutations() {
        Map<ResourceKey<Block>, BlockTransmutationValue> transmutations = BuiltInRegistries.BLOCK.getDataMap(MEDataMaps.BLOCK_TRANSMUTATIONS);
        List<BlockTransmutationRecipe> recipes = new ArrayList<>();
        for (Map.Entry<ResourceKey<Block>, BlockTransmutationValue> transmutation : transmutations.entrySet()) {
            recipes.add(new BlockTransmutationRecipe(BuiltInRegistries.BLOCK.get(transmutation.getKey()), transmutation.getValue()));
        }
        return recipes;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new BlockTransmutationCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new EntityTransmutationCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    private static List<RecipeHolder<CraftingRecipe>> getItemTransmutations() {
        Map<ResourceKey<Item>, List<ItemTransmutationValue>> transmutationsMap = BuiltInRegistries.ITEM.getDataMap(MEDataMaps.ITEM_TRANSMUTATIONS);
        List<RecipeHolder<CraftingRecipe>> recipes = new ArrayList<>();

        Ingredient transmutationStone = getTransmutationStone();
        RegistryAccess lookup = Minecraft.getInstance().level.registryAccess();

        for (Map.Entry<ResourceKey<Item>, List<ItemTransmutationValue>> transmutation : transmutationsMap.entrySet()) {
            ResourceKey<Item> key = transmutation.getKey();
            List<ItemTransmutationValue> transmutations = transmutation.getValue();

            for (ItemTransmutationValue transmutationValue : transmutations) {
                List<Ingredient> ingredients = getIngredients(key, transmutationStone, transmutationValue);
                CraftingRecipe craftingRecipe = new ShapelessRecipe("transmutations", CraftingBookCategory.MISC, transmutationValue.result().copy(), NonNullList.copyOf(ingredients));
                recipes.add(new RecipeHolder<>(key.location().withPath("item_transmutations/" + getRecipeName(craftingRecipe, lookup)), craftingRecipe));
            }
        }

        return recipes;
    }

    private static String getRecipeName(CraftingRecipe recipe, HolderLookup.Provider lookup) {
        StringBuilder builder = new StringBuilder();
        for (Ingredient ingredient : recipe.getIngredients()) {
            for (Ingredient.Value value : ingredient.getValues()) {
                if (value instanceof Ingredient.ItemValue(ItemStack item)) {
                    ResourceLocation itemLocation = BuiltInRegistries.ITEM.getKey(item.getItem());
                    builder.append(itemLocation.getPath()).append("_");
                } else if (value instanceof Ingredient.TagValue(TagKey<Item> tag)) {
                    builder.append(tag.location().getPath()).append("_");
                }
            }
        }
        Item result = recipe.getResultItem(lookup).getItem();
        if (result != Items.AIR) {
            builder.append("to_").append(BuiltInRegistries.ITEM.getKey(result).getPath());
        } else {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    private static @NotNull List<Ingredient> getIngredients(ResourceKey<Item> key, Ingredient transmutationStone, ItemTransmutationValue transmutationValue) {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(transmutationStone);
        for (int i = 0; i < transmutationValue.inputCount(); i++) {
            ingredients.add(Ingredient.of(BuiltInRegistries.ITEM.get(key)));
        }
        return ingredients;
    }

    private static @NotNull Ingredient getTransmutationStone() {
        ItemStack stack = MEItems.TRANSMUTATION_STONE.toStack();
        stack.set(DataComponents.LORE, new ItemLore(List.of(Component.literal("Requires at least 1 matter").withStyle(ChatFormatting.RESET).withStyle(ChatFormatting.GRAY))));
        return Ingredient.of(stack);
    }
}