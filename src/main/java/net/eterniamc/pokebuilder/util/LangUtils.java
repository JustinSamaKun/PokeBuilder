package net.eterniamc.pokebuilder.util;

import net.eterniamc.pokebuilder.controller.ConfigController;

import java.text.DecimalFormat;

public class LangUtils {

    public static String get(String message) {
        String out = ConfigController.INSTANCE.getLanguageProperties().getProperty(message);
        if (out == null) {
            out = message;
        }
        return TextUtils.text(out);
    }

    public static String format(String message, Object parameter) {
        return String.format(get(message), parameter.toString());
    }

    public static String formatNumber(Number number) {
        return new DecimalFormat(get("general.decimal-format")).format(number);
    }
}
