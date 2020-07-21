package net.eterniamc.pokebuilder.controller;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import net.eterniamc.pokebuilder.data.ModifierType;
import net.eterniamc.pokebuilder.ui.IVModifierUI;
import net.eterniamc.pokebuilder.util.ItemUtils;
import net.minecraft.item.ItemStack;

public enum IVController implements ModifierController {
    INSTANCE;

    @Override
    public ModifierType getType() {
        return ModifierType.MAX_IV;
    }

    @Override
    public void process(Pokemon pokemon) {
        new IVModifierUI(pokemon).open(pokemon.getOwnerPlayer());
    }

    @Override
    public ItemStack getDisplay(Pokemon pokemon) {
        ItemStack stack = new ItemStack(PixelmonItems.energyRoot);
        ItemUtils.setDisplayName(stack, "&6IV Modifier");
        return stack;
    }
}
