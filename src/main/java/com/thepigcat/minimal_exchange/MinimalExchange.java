package com.thepigcat.minimal_exchange;

import com.thepigcat.minimal_exchange.content.items.IMatterItem;
import com.thepigcat.minimal_exchange.data.MECapabilities;
import com.thepigcat.minimal_exchange.data.MEDataComponents;
import com.thepigcat.minimal_exchange.data.MEDataMaps;
import com.thepigcat.minimal_exchange.data.components.MatterComponent;
import com.thepigcat.minimal_exchange.registries.MEItems;
import com.thepigcat.minimal_exchange.registries.MESoundEvents;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(MinimalExchange.MODID)
public final class MinimalExchange {
    public static final String MODID = "minimal_exchange";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public MinimalExchange(IEventBus modEventBus, ModContainer modContainer) {
        MEDataComponents.DATA_COMPONENTS.register(modEventBus);
        MEItems.ITEMS.register(modEventBus);
        MESoundEvents.SOUND_EVENTS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        modEventBus.addListener(this::registerDataMaps);
        modEventBus.addListener(this::registerCapabilities);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void registerDataMaps(RegisterDataMapTypesEvent event) {
        event.register(MEDataMaps.BLOCK_TRANSMUTATIONS);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof IMatterItem matterItem) {
                event.registerItem(MECapabilities.MatterStorage.ITEM, (itemstack, ctx) -> {
                    if (itemstack.has(MEDataComponents.MATTER)) {
                        MatterComponent matterComponent = itemstack.get(MEDataComponents.MATTER);
                        itemstack.set(MEDataComponents.MATTER, new MatterComponent(matterComponent.matter(), matterItem.getMatterCapacity(itemstack)));
                        return matterComponent;
                    }
                    throw new RuntimeException("Item that implement IMatterItem interface needs the MATTER DataComponent, affected item: " + item);
                }, item);
            }
        }
    }

    static {
        CREATIVE_MODE_TABS.register("me_tab", () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.minimal_exchange"))
                .withTabsBefore(CreativeModeTabs.COMBAT)
                .icon(() -> MEItems.TRANSMUTATION_STONE.get().getDefaultInstance())
                .displayItems((parameters, output) -> {
                    for (ItemLike item : MEItems.TAB_ITEMS) {
                        output.accept(item);
                        if (item.asItem() instanceof IMatterItem matterItem) {
                            ItemStack itemStack = new ItemStack(item);
                            int matterCapacity = matterItem.getMatterCapacity(itemStack);
                            itemStack.set(MEDataComponents.MATTER, new MatterComponent(matterCapacity, matterCapacity));
                            output.accept(itemStack);
                        }
                    }

                    for (DyeColor color : DyeColor.values()) {
                        ItemStack itemStack = new ItemStack(MEItems.ALCHEMY_BAG.get());
                        int textureDiffuseColor = color.getTextureDiffuseColor();
                        Vec3i rgb = new Vec3i((int) Math.min(FastColor.ARGB32.red(textureDiffuseColor) * 1.4, 255),
                                (int) Math.min(FastColor.ARGB32.blue(textureDiffuseColor) * 1.4, 255),
                                (int) Math.min(FastColor.ARGB32.green(textureDiffuseColor) * 1.4, 255));
                        itemStack.set(DataComponents.DYED_COLOR, new DyedItemColor(FastColor.ARGB32.color(rgb.getX(), rgb.getY(), rgb.getZ()), false));
                        output.accept(itemStack);
                    }
                }).build());
    }
}
