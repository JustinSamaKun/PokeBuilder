package net.eterniamc.pokebuilder.util;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.text.TextFormatting;

public class TextUtils {

    public static String text(String str) {
        return str.replaceAll("&", "\u00a7");
    }

    public static TextFormatting getFormattingForDyeColor(EnumDyeColor color) {
        switch (color) {
            case RED: return TextFormatting.DARK_RED;
            case BLUE: return TextFormatting.DARK_BLUE;
            case CYAN: return TextFormatting.AQUA;
            case GRAY: return TextFormatting.DARK_GRAY;
            case SILVER: return TextFormatting.GRAY;
            case LIME: return TextFormatting.GREEN;
            case MAGENTA:
            case PINK: return TextFormatting.LIGHT_PURPLE;
            case BLACK: return TextFormatting.BLACK;
            case BROWN: return TextFormatting.GOLD;
            case GREEN: return TextFormatting.DARK_GREEN;
            case ORANGE: return TextFormatting.RED;
            case PURPLE: return TextFormatting.DARK_PURPLE;
            case YELLOW: return TextFormatting.YELLOW;
            case LIGHT_BLUE: return TextFormatting.BLUE;
            case WHITE: return TextFormatting.WHITE;
            default: return TextFormatting.RESET;
        }
    }
}
