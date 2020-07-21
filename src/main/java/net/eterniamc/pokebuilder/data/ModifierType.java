package net.eterniamc.pokebuilder.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ModifierType {
    GENDER(1500),
    GROWTH(500),
    HIDDEN_ABILITY(10000),
    INCREASE_IV(150),
    INCREASE_IV_10(1000),
    DECREASE_IV(150),
    DECREASE_IV_10(1000),
    MAX_IV(15000),
    RANDOM_IV(2500),
    NATURE(5000),
    SHINY(2500);

    private final double defaultPrice;

    public static ModifierType getFromName(String name) {
        name = name.toLowerCase().replaceAll("[_ ]", "");
        for (ModifierType type : values()) {
            if (type.toString().replaceAll("[_ ]", "").equals(name)) {
                return type;
            }
        }

        throw new Error("Could not find modifier with name " + name);
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
