package org.leralix.exotictrades.storage.adapters;

import com.google.gson.*;
import org.leralix.exotictrades.traders.position.FixedPosition;
import org.leralix.exotictrades.traders.position.RandomPosition;
import org.leralix.exotictrades.traders.position.TraderPosition;

import java.lang.reflect.Type;

public class TraderPositionTypeAdapter implements JsonSerializer<TraderPosition>, JsonDeserializer<TraderPosition> {

    @Override
    public JsonElement serialize(TraderPosition src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src).getAsJsonObject();
        jsonObject.addProperty("type", src.getClass().getSimpleName()); // Stocke le type
        return jsonObject;
    }

    @Override
    public TraderPosition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString(); // Récupère le type

        try {
            Class<?> clazz = getClassForType(type);
            return context.deserialize(jsonObject, clazz);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Type inconnu : " + type, e);
        }
    }

    private Class<?> getClassForType(String type) throws ClassNotFoundException {
        switch (type) {
            case "FixedPosition":
                return FixedPosition.class;
            case "RandomPosition":
                return RandomPosition.class;
            default:
                throw new ClassNotFoundException("Type de TraderPosition inconnu : " + type);
        }
    }
}
