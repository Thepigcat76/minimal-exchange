package com.thepigcat.minimal_exchange.registries;

import com.thepigcat.minimal_exchange.MinimalExchange;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class MESoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, MinimalExchange.MODID);
    public static final Supplier<SoundEvent> TRANSMUTE = registerSoundEvent("transmute");
    public static final Supplier<SoundEvent> DESTROY = registerSoundEvent("destruction_catalyst_destroy");

    private static Supplier<SoundEvent> registerSoundEvent(String name){
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MinimalExchange.MODID, name)));
    }
}
