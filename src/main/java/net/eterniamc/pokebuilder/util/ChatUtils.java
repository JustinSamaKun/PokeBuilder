package net.eterniamc.pokebuilder.util;

import net.eterniamc.pokebuilder.controller.ConfigController;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

public class ChatUtils {

    public static void sendMessage(EntityPlayerMP player, String message) {
        String toSend = TextUtils.text(ConfigController.CONFIG.getMessagePrefix() + message);
        player.sendMessage(new TextComponentString(toSend));
    }
}
