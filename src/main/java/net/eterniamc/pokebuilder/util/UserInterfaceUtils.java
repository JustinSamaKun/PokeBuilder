package net.eterniamc.pokebuilder.util;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.eterniamc.pokebuilder.controller.ConfigController;
import net.eterniamc.pokebuilder.data.ModifierType;
import net.eterniamc.pokebuilder.ui.ChoiceConfirmationUI;
import net.minecraft.entity.player.EntityPlayerMP;

public class UserInterfaceUtils {

    public static void createConfirmation(Pokemon pokemon, ModifierType type, Runnable onAccept) {
        createConfirmation(pokemon, ConfigController.INSTANCE.getPriceFor(type, pokemon), onAccept);
    }

    public static void createConfirmation(Pokemon pokemon, double price, Runnable onAccept) {
        EntityPlayerMP owner = pokemon.getOwnerPlayer();
        if (CurrencyUtils.playerHasMoney(owner, price)) {
            new ChoiceConfirmationUI(
                    () -> {
                        CurrencyUtils.removePlayerMoney(owner, price);
                        onAccept.run();
                    },
                    pokemon
            ).open(owner);
        }
    }
}
