package net.eterniamc.pokebuilder.util;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.eterniamc.bridge.Bridge;
import net.eterniamc.pokebuilder.controller.ConfigController;
import net.eterniamc.pokebuilder.data.ModifierType;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

import java.text.DecimalFormat;

public class CurrencyUtils {

    public static void wrapAction(EntityPlayerMP player, ModifierType type, Pokemon pokemon, Runnable onConfirm) {
        wrapAction(player, ConfigController.INSTANCE.getPriceFor(type, pokemon), onConfirm);
    }

    public static void wrapAction(EntityPlayerMP player, double amount, Runnable onConfirm) {
        if (playerHasMoney(player, amount)) {
            removePlayerMoney(player, amount);
            onConfirm.run();
        }
    }

    public static boolean playerHasMoney(EntityPlayerMP player, double amount) {
        boolean pass = Bridge.INSTANCE.API.getCurrencyController().getPlayerBalance(ConfigController.CONFIG.getCurrency(), player) >= amount;
        if (!pass) {
            player.sendMessage(new TextComponentString(TextUtils.text(
                    ConfigController.CONFIG.getMessagePrefix() +
                        ConfigController.CONFIG.getNotEnoughMoneyMessage()
            )));
        }
        return pass;
    }

    public static void removePlayerMoney(EntityPlayerMP player, double amount) {
        Bridge.INSTANCE.API.getCurrencyController().removePlayerBalance(ConfigController.CONFIG.getCurrency(), player, amount);
        player.sendMessage(new TextComponentString(TextUtils.text(
                ConfigController.CONFIG.getMessagePrefix() +
                    String.format(ConfigController.CONFIG.getPaidMessage(), new DecimalFormat("#.##").format(amount))
        )));
    }
}
