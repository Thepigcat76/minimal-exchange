package com.thepigcat.minimal_exchange.datagen.assets;

import com.thepigcat.minimal_exchange.MinimalExchange;
import com.thepigcat.minimal_exchange.registries.MEBlocks;
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
        addItem(MEItems.IRON_BAND, "Iron Band");
        addItem(MEItems.TRANSMUTATION_STONE, "Transmutation Stone");
        addItem(MEItems.DESTRUCTION_CATALYST, "Destruction Catalyst");
        addItem(MEItems.ALCHEMY_BAG, "Alchemy Bag");
        addItem(MEItems.WEAK_COVALENCE_DUST, "Weak Covalence Dust");
        addItem(MEItems.STRONG_COVALENCE_DUST, "Strong Covalence Dust");
        addBlock(MEBlocks.EXCHANGE_PYLON, "Exchange Pylon");
        add("tooltip.minimal_exchange.matter_stored", "Matter Stored: %d/%d");
        add("itemGroup.minimal_exchange.me_tab", "Minimal Exchange");
    }
}
