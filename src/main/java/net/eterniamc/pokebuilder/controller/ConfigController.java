package net.eterniamc.pokebuilder.controller;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import lombok.Getter;
import lombok.SneakyThrows;
import net.eterniamc.pokebuilder.data.Config;
import net.eterniamc.pokebuilder.data.ModifierType;
import net.eterniamc.pokebuilder.data.PokemonType;
import net.eterniamc.pokebuilder.data.gson.EnumSpeciesAdapter;
import net.eterniamc.pokebuilder.data.gson.ModifierTypeAdapter;
import net.eterniamc.pokebuilder.data.gson.PokemonTypeAdapter;
import net.eterniamc.pokebuilder.util.LangUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public enum ConfigController {
    INSTANCE;

    private static final File CONFIG_FILE = new File("./config/PokeBuilder/config.json");
    private static final File LANG_FILE = new File("./config/PokeBuilder/language.properties");

    public static Config CONFIG;

    private final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(ModifierType.class, new ModifierTypeAdapter())
            .registerTypeAdapter(EnumSpecies.class, new EnumSpeciesAdapter())
            .registerTypeAdapter(PokemonType.class, new PokemonTypeAdapter())
            .create();

    @Getter
    public Properties languageProperties;


    @SneakyThrows
    public void initialize() {
        File directory = CONFIG_FILE.getParentFile();
        if (!(directory.exists() || directory.mkdir())) {
            throw new Error("Could not create config directory PokeBuilder");
        }

        if (CONFIG_FILE.exists()) {
            CONFIG = GSON.fromJson(new FileReader(CONFIG_FILE), Config.class);
        } else {
            CONFIG = new Config();
            for (ModifierType type : ModifierType.values()) {
                if (type != ModifierType.IVS) {
                    CONFIG.getModifierPrices().put(type, type.getDefaultPrice());
                    CONFIG.getModifierPriceOverrides().put(type, Maps.newHashMap());
                }
            }
            CONFIG.getBlacklistedModifiers().add(ModifierType.IVS);
            CONFIG.getModifierPriceOverrides().get(ModifierType.MAX_IV).put(EnumSpecies.Ditto.toString(), 10000.0);
            CONFIG.getModifierPriceOverrides().get(ModifierType.SHINY).put(PokemonType.LEGENDARY.toString(), 500000.0);
            for (PokemonType pokemonType : PokemonType.values()) {
                CONFIG.getPokemonCreationPrices().put(pokemonType, pokemonType.getDefaultPrice());
            }
        }
        writeToFile();

        Properties defaultProperties = new Properties();
        defaultProperties.load(getLanguageInputStream());
        if (!LANG_FILE.exists()) {
            Files.copy(getLanguageInputStream(), LANG_FILE.toPath());
        }
        languageProperties = new Properties(defaultProperties);
        languageProperties.load(new FileReader(LANG_FILE));
    }

    @SneakyThrows
    private void writeToFile() {
        FileWriter writer = new FileWriter(CONFIG_FILE);
        writer.write(GSON.toJson(CONFIG));
        writer.flush();
        writer.close();
    }

    private InputStream getLanguageInputStream() {
        return Objects.requireNonNull(ConfigController.class.getClassLoader().getResourceAsStream("language.properties"));
    }

    public String createPriceLine(double price) {
        return LangUtils.format("message.cost", LangUtils.formatNumber(price));
    }

    public double getPriceFor(ModifierType type, Pokemon pokemon) {
        Map<String, Double> overrides = CONFIG.getModifierPriceOverrides().getOrDefault(type, Maps.newHashMap());
        if (overrides.containsKey(pokemon.getSpecies().toString())) {
            return CONFIG.getModifierPriceOverrides().get(type).get(pokemon.getSpecies().toString());
        } else if (overrides.containsKey(PokemonType.of(pokemon.getSpecies()).toString())) {
            return CONFIG.getModifierPriceOverrides().get(type).get(PokemonType.of(pokemon.getSpecies()).toString());
        }

        double multiplier = pokemon.isLegendary() ? CONFIG.getLegendaryPriceMultiplier() : 1;
        return CONFIG.getModifierPrices().getOrDefault(type, type.getDefaultPrice()) * multiplier;
    }

    public boolean isBlacklisted(ModifierType type) {
        return CONFIG.getBlacklistedModifiers().contains(type);
    }

    public boolean isBlacklisted(EnumSpecies type) {
        return CONFIG.getBlacklistedPokemon().contains(type);
    }

    public double getPokemonCreationPrice(PokemonType type) {
        return CONFIG.getPokemonCreationPrices().getOrDefault(type, type.getDefaultPrice());
    }

    public double getPokemonCreationPrice(EnumSpecies species) {
        return CONFIG.getPokemonCreationPriceOverrides().getOrDefault(species, getPokemonCreationPrice(PokemonType.of(species)));
    }
}
