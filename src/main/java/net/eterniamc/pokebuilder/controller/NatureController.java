package net.eterniamc.pokebuilder.controller;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import net.eterniamc.pokebuilder.data.ModifierType;
import net.eterniamc.pokebuilder.ui.NatureModifierUI;
import net.eterniamc.pokebuilder.util.ItemUtils;
import net.minecraft.item.ItemStack;

public enum NatureController implements ModifierController {
    INSTANCE;

    @Override
    public ModifierType getType() {
        return ModifierType.NATURE;
    }

    @Override
    public void process(Pokemon pokemon) {
        new NatureModifierUI(pokemon).open(pokemon.getOwnerPlayer());
    }

    @Override
    public ItemStack getDisplay(Pokemon pokemon) {
        ItemStack stack = new ItemStack(PixelmonItemsHeld.airBalloon);
        ItemUtils.setDisplayName(stack, "&bNature Modifier");
        return stack;
    }
}
