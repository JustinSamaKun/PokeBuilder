package net.eterniamc.pokebuilder.controller;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import net.eterniamc.pokebuilder.data.ModifierType;
import net.eterniamc.pokebuilder.ui.GrowthModifierUI;
import net.eterniamc.pokebuilder.util.ItemUtils;
import net.minecraft.item.ItemStack;

public enum GrowthController implements ModifierController {
    INSTANCE;

    @Override
    public ModifierType getType() {
        return ModifierType.GROWTH;
    }

    @Override
    public void process(Pokemon pokemon) {
        new GrowthModifierUI(pokemon).open(pokemon.getOwnerPlayer());
    }

    @Override
    public ItemStack getDisplay(Pokemon pokemon) {
        ItemStack stack = new ItemStack(PixelmonItems.ppUp);
        ItemUtils.setDisplayName(stack, "modifier.growth.name");
        ItemUtils.setItemLore(stack, getPriceLine(pokemon));
        return stack;
    }
}
