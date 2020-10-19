package net.eterniamc.pokebuilder.data.gson;

import com.google.gson.*;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;

import java.lang.reflect.Type;

public class EnumSpeciesAdapter implements JsonSerializer<EnumSpecies>, JsonDeserializer<EnumSpecies> {
    @Override
    public EnumSpecies deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return EnumSpecies.getFromName(json.getAsString()).orElse(null);
    }

    @Override
    public JsonElement serialize(EnumSpecies src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.name);
    }
}
