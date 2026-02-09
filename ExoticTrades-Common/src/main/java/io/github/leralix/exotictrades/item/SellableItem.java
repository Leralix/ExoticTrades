package io.github.leralix.exotictrades.item;

import com.google.gson.JsonElement;
import org.bukkit.inventory.ItemStack;
import io.github.leralix.exotictrades.storage.adapters.ItemStackAdapter;

public class SellableItem {
    private final JsonElement itemJsonElement;
    private int price;

    public SellableItem(ItemStack itemJsonElement, int price) {

        ItemStackAdapter adapter = new ItemStackAdapter();
        this.itemJsonElement = adapter.serialize(itemJsonElement, ItemStack.class, null);
        this.price = price;
    }

    public ItemStack getItemStack() {
        ItemStackAdapter adapter = new ItemStackAdapter();
        return adapter.deserialize(itemJsonElement, ItemStack.class, null);
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
