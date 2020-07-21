package net.eterniamc.pokebuilder.data.gson;

import com.google.gson.*;
import net.eterniamc.pokebuilder.data.ModifierType;

import java.lang.reflect.Type;

public class ModifierTypeAdapter implements JsonSerializer<ModifierType>, JsonDeserializer<ModifierType> {

    @Override
    public ModifierType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return ModifierType.getFromName(json.getAsString());
    }

    @Override
    public JsonElement serialize(ModifierType src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.name().toLowerCase());
    }
}
