package net.eterniamc.pokebuilder.controller;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.eterniamc.pokebuilder.data.ModifierType;
import net.minecraft.item.ItemStack;

public interface ModifierController {

    ModifierType getType();

    default boolean canApply(Pokemon pokemon) {
        return true;
    }

    void process(Pokemon pokemon);

    ItemStack getDisplay(Pokemon pokemon);

    default String getPriceLine(Pokemon pokemon) {
        return "&7Cost: &a" + ConfigController.INSTANCE.getPriceFor(getType(), pokemon);
    }
}
