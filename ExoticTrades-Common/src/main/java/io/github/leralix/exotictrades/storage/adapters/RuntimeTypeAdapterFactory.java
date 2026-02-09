package io.github.leralix.exotictrades.storage.adapters;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.util.HashMap;
import java.util.Map;

public class RuntimeTypeAdapterFactory<T> implements TypeAdapterFactory {
    private final Class<T> baseType;
    private final Map<String, Class<? extends T>> subTypes = new HashMap<>();

    private RuntimeTypeAdapterFactory(Class<T> baseType) {
        this.baseType = baseType;
    }

    public static <T> RuntimeTypeAdapterFactory<T> of(Class<T> baseType) {
        return new RuntimeTypeAdapterFactory<>(baseType);
    }

    public RuntimeTypeAdapterFactory<T> registerSubtype(Class<? extends T> subType, String typeName) {
        subTypes.put(typeName, subType);
        return this;
    }

    @Override
    public <R> TypeAdapter<R> create(Gson gson, TypeToken<R> type) {
        if (!baseType.isAssignableFrom(type.getRawType())) {
            return null;
        }

        Gson customGson = new GsonBuilder().create();
        return (TypeAdapter<R>) new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws java.io.IOException {
                JsonObject jsonObject = customGson.toJsonTree(value).getAsJsonObject();
                jsonObject.addProperty("type", value.getClass().getSimpleName()); // Ajoute le champ "type"
                customGson.toJson(jsonObject, out);
            }

            @Override
            public T read(JsonReader in) throws java.io.IOException {
                JsonObject jsonObject = JsonParser.parseReader(in).getAsJsonObject();
                String type = jsonObject.get("type").getAsString();
                Class<? extends T> subType = subTypes.get(type);

                if (subType == null) {
                    throw new JsonParseException("Type inconnu : " + type);
                }

                return customGson.fromJson(jsonObject, subType);
            }
        };
    }
}
