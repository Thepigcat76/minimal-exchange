package com.thepigcat.minimal_exchange.datagen.assets;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.registries.MEItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class EnUSLangProvider extends LanguageProvider {
    public EnUSLangProvider(PackOutput output) {
        super(output, MinimalExchange.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addItem(MEItems.MINIUM_SHARD, "Minium Shard");
        addItem(MEItems.TRANSMUTATION_STONE, "Transmutation Stone");
    }
}
