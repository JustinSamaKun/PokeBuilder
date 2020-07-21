package net.eterniamc.pokebuilder.data.gson;

import com.google.gson.*;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.eterniamc.pokebuilder.data.PokemonType;

import java.lang.reflect.Type;

public class PokemonTypeAdapter implements JsonSerializer<PokemonType>, JsonDeserializer<PokemonType> {
    @Override
    public PokemonType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return PokemonType.valueOf(json.getAsString().toUpperCase());
    }

    @Override
    public JsonElement serialize(PokemonType src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}
