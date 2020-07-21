package net.eterniamc.pokebuilder.controller;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.eterniamc.pokebuilder.data.ModifierType;
import net.eterniamc.pokebuilder.util.ItemUtils;
import net.eterniamc.pokebuilder.util.UserInterfaceUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public enum ShinyController implements ModifierController {
    INSTANCE;

    @Override
    public ModifierType getType() {
        return ModifierType.SHINY;
    }

    @Override
    public void process(Pokemon pokemon) {
        UserInterfaceUtils.createConfirmation(pokemon, ModifierType.SHINY, () -> pokemon.setShiny(!pokemon.isShiny()));
    }

    @Override
    public ItemStack getDisplay(Pokemon pokemon) {
        ItemStack stack = new ItemStack(Items.NETHER_STAR);
        ItemUtils.setDisplayName(stack, "modifier.shiny.name");
        ItemUtils.setItemLore(stack,getPriceLine(pokemon));
        return stack;
    }
}
