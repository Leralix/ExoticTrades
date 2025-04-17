package org.leralix.exotictrades.storage.adapters;

import com.google.gson.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Base64;

public class ItemStackAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {


    @Override
    public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        String encodedItemStack = jsonElement.getAsString();

        try {
            byte[] bytes = Base64.getDecoder().decode(encodedItemStack);
            ByteArrayInputStream io = new ByteArrayInputStream(bytes);
            BukkitObjectInputStream is = new BukkitObjectInputStream(io);
            ItemStack itemStack = (ItemStack) is.readObject();
            is.close();
            return itemStack;
        } catch (IOException | ClassNotFoundException e) {
            throw new JsonParseException("Failed to deserialize ItemStack", e);
        }
    }

    @Override
    public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext jsonSerializationContext) {


        String encodedItemStack;
        try {
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);
            os.writeObject(itemStack);
            os.close();

            byte[] bytes = io.toByteArray();
            encodedItemStack = Base64.getEncoder().encodeToString(bytes);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return new JsonPrimitive(encodedItemStack);
    }
}
