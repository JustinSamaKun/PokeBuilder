package net.eterniamc.pokebuilder.util;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

public class ChatUtils {

    public static void sendMessage(EntityPlayerMP player, String message) {
        player.sendMessage(new TextComponentString(LangUtils.format("general.prefix", LangUtils.get(message))));
    }
}
