package net.eterniamc.pokebuilder.controller;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import lombok.SneakyThrows;
import net.eterniamc.pokebuilder.data.Config;
import net.eterniamc.pokebuilder.data.ModifierType;
import net.eterniamc.pokebuilder.data.PokemonType;
import net.eterniamc.pokebuilder.data.gson.EnumSpeciesAdapter;
import net.eterniamc.pokebuilder.data.gson.ModifierTypeAdapter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public enum ConfigController {
    INSTANCE;

    private static final File FILE = new File("./config/pokebuilder.json");
    public static Config CONFIG;

    public Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(ModifierType.class, new ModifierTypeAdapter())
            .registerTypeAdapter(EnumSpecies.class, new EnumSpeciesAdapter())
            .create();

    @SneakyThrows
    public void initialize() {
        if (FILE.exists()) {
            CONFIG = GSON.fromJson(new FileReader(FILE), Config.class);
        } else {
            CONFIG = new Config();
            for (ModifierType type : ModifierType.values()) {
                CONFIG.getModifierPrices().put(type, type.getDefaultPrice());
                CONFIG.getModifierPriceOverrides().put(type, Maps.newEnumMap(EnumSpecies.class));
            }
            CONFIG.getModifierPriceOverrides().get(ModifierType.MAX_IV).put(EnumSpecies.Ditto, 100000.0);
            for (PokemonType pokemonType : PokemonType.values()) {
                CONFIG.getPokemonCreationPrices().put(pokemonType, pokemonType.getDefaultPrice());
            }
        }
        writeToFile();
    }

    @SneakyThrows
    private void writeToFile() {
        FileWriter writer = new FileWriter(FILE);
        writer.write(GSON.toJson(CONFIG));
        writer.flush();
        writer.close();
    }

    public double getPriceFor(ModifierType type, Pokemon pokemon) {
        if (CONFIG.getModifierPriceOverrides().getOrDefault(type, Maps.newHashMap()).containsKey(pokemon.getSpecies())) {
            return CONFIG.getModifierPriceOverrides().get(type).get(pokemon.getSpecies());
        }

        return CONFIG.getModifierPrices().getOrDefault(type, type.getDefaultPrice());
    }

    public boolean isBlacklisted(ModifierType type) {
        return CONFIG.getBlacklistedModifiers().contains(type);
    }

    public boolean isBlacklisted(EnumSpecies type) {
        return CONFIG.getBlacklistedPokemon().contains(type);
    }

    public double getPriceToCreate(EnumSpecies species) {
        return CONFIG.getPokemonCreationPriceOverrides().getOrDefault(species, CONFIG.getPokemonCreationPrices().get(PokemonType.of(species)));
    }
}
