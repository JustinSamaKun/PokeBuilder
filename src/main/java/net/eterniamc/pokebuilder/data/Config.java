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

    private Map<ModifierType, Double> prices = Maps.newEnumMap(ModifierType.class);

    private Set<ModifierType> blacklistedModifiers = Sets.newHashSet();

    private Map<ModifierType, Map<EnumSpecies, Double>> overridePrices = Maps.newEnumMap(ModifierType.class);
}
