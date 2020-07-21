package net.eterniamc.pokebuilder.controller;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import net.eterniamc.pokebuilder.data.ModifierType;
import net.eterniamc.pokebuilder.util.ItemUtils;
import net.eterniamc.pokebuilder.util.UserInterfaceUtils;
import net.minecraft.item.ItemStack;

public enum HiddenAbilityController implements ModifierController {
    INSTANCE;

    @Override
    public ModifierType getType() {
        return ModifierType.HIDDEN_ABILITY;
    }

    @Override
    public boolean canApply(Pokemon pokemon) {
        return pokemon.getAbilitySlot() < 2 && pokemon.getBaseStats().abilities.length == 3 && pokemon.getBaseStats().abilities[2] != null;
    }

    @Override
    public void process(Pokemon pokemon) {
        UserInterfaceUtils.createConfirmation(pokemon, getType(), () -> pokemon.setAbilitySlot(2));
    }

    @Override
    public ItemStack getDisplay(Pokemon pokemon) {
        ItemStack stack = new ItemStack(PixelmonItems.abilityCapsule);
        ItemUtils.setDisplayName(stack, "&5Hidden Ability Modifier");
        ItemUtils.setItemLore(stack, !canApply(pokemon) ?
                "&cThis Pokemon already has its hidden ability" :
                getPriceLine(pokemon)
        );
        return stack;
    }
}
