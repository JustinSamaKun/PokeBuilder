package net.eterniamc.pokebuilder.data;

import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import lombok.Getter;
import net.eterniamc.pokebuilder.controller.ConfigController;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
public enum PokemonType {
    NORMAL(1000, pokemon -> !pokemon.isLegendary()),
    LEGENDARY(100000, EnumSpecies::isLegendary);

    private final double defaultPrice;
    private final Predicate<EnumSpecies> filter;
    private final List<EnumSpecies> species;

    PokemonType(double defaultPrice, Predicate<EnumSpecies> filter) {
        this.defaultPrice = defaultPrice;
        this.filter = filter;
        this.species = Arrays.stream(EnumSpecies.values())
                .filter(filter)
                .filter(species1 -> !ConfigController.INSTANCE.isBlacklisted(species1))
                .collect(Collectors.toList());
    }

    public static PokemonType of(EnumSpecies species) {
        return LEGENDARY.getFilter().test(species) ? LEGENDARY : NORMAL;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
