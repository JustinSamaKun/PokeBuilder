package net.eterniamc.pokebuilder.data;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class Config {

    private String messagePrefix = "&4&lPokeBuilder &7&l> &f";
    private String notEnoughMoneyMessage = "You do not have enough money to do this!";
    private String paidMessage = "$%s has been withdrawn from your account";
    private String costMessage = "&7Cost: &a$%s";

    private String currency = "Coin";

    private Set<ModifierType> blacklistedModifiers = Sets.newHashSet();
    private Set<EnumSpecies> blacklistedPokemon = Sets.newHashSet();

    private Map<ModifierType, Double> modifierPrices = Maps.newEnumMap(ModifierType.class);
    private Map<ModifierType, Map<EnumSpecies, Double>> modifierPriceOverrides = Maps.newEnumMap(ModifierType.class);

    private boolean pokemonCreationAllowed = true;
    private Map<PokemonType, Double> pokemonCreationPrices = Maps.newHashMap();
    private Map<EnumSpecies, Double> pokemonCreationPriceOverrides = Maps.newHashMap();
}
