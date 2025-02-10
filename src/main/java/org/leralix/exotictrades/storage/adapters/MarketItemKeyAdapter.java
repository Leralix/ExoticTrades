package org.leralix.exotictrades.storage.adapters;

import com.google.gson.*;
import org.leralix.exotictrades.storage.MarketItemKey;

import java.lang.reflect.Type;

public class MarketItemKeyAdapter implements JsonDeserializer<MarketItemKey> {

    @Override
    public MarketItemKey deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        String[] split = json.getAsString().split(":");
        String material = split[0];
        int modelData = Integer.parseInt(split[1]);
        return MarketItemKey.of(material, modelData);
    }
}
