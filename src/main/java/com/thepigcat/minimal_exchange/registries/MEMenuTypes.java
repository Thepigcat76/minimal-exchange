package com.thepigcat.minimal_exchange.registries;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.content.menus.AlchemyBagMenu;
import com.thepigcat.minimal_exchange.content.menus.ExchangePylonMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class MEMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, MinimalExchange.MODID);

    public static final Supplier<MenuType<AlchemyBagMenu>> ALCHEMY_BAG =
            registerMenuType("alchemy_bag", AlchemyBagMenu::new);
    public static final Supplier<MenuType<ExchangePylonMenu>> EXCHANGE_PYLON =
            registerMenuType("exchange_pylon", ExchangePylonMenu::new);

    private static <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }
}
