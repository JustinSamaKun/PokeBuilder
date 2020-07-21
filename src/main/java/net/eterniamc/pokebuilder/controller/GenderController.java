package net.eterniamc.pokebuilder.controller;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import net.eterniamc.pokebuilder.data.ModifierType;
import net.eterniamc.pokebuilder.util.ItemUtils;
import net.eterniamc.pokebuilder.util.UserInterfaceUtils;
import net.minecraft.item.ItemStack;

public enum GenderController implements ModifierController {
    INSTANCE;

    @Override
    public ModifierType getType() {
        return ModifierType.GENDER;
    }

    @Override
    public boolean canApply(Pokemon pokemon) {
        return Math.abs(pokemon.getBaseStats().malePercent - 0.5) < 0.5;
    }

    @Override
    public void process(Pokemon pokemon) {
        UserInterfaceUtils.createConfirmation(
                pokemon,
                getType(),
                () -> pokemon.setGender(pokemon.getGender() == Gender.Male ? Gender.Female : Gender.Male)
        );
    }

    @Override
    public ItemStack getDisplay(Pokemon pokemon) {
        ItemStack stack = new ItemStack(PixelmonItems.awakening);
        ItemUtils.setDisplayName(stack, (pokemon.getGender() == Gender.Male ? "&b" : "&d") + "Gender Modifier");
        ItemUtils.setItemLore(stack, !canApply(pokemon) ?
                "&cThis Pokemon only has one gender" :
                getPriceLine(pokemon)
        );
        return stack;
    }
}
