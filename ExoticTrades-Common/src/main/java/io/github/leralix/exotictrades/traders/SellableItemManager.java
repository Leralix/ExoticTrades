package io.github.leralix.exotictrades.traders;

import io.github.leralix.exotictrades.item.SellableItem;
import org.leralix.lib.utils.RandomUtil;

import java.util.ArrayList;
import java.util.List;

public class SellableItemManager {

    private List<SellableItem> sellableItems;
    private final List<SellableItem> todaySellableItems;
    private int nbDailySellableItems;


    public SellableItemManager(int nbDailySellableItems) {
        this.sellableItems = new ArrayList<>();
        this.todaySellableItems = new ArrayList<>();
        this.nbDailySellableItems = nbDailySellableItems;

    }

    public void updateSellableItems() {
        if(sellableItems.isEmpty()){ // No items to sell
            return;
        }

        todaySellableItems.clear();
        for (int i = 0; i < nbDailySellableItems; i++) {
            int randomIndex = RandomUtil.getRandom().nextInt(sellableItems.size());
            todaySellableItems.add(sellableItems.get(randomIndex));
        }
    }

    public List<SellableItem> getTodaySellableItems() {
        return todaySellableItems;
    }

    public void setNbDailySellableItems(int nbDailySellableItems) {
        this.nbDailySellableItems = nbDailySellableItems;
    }


    public List<SellableItem> getAllSellableItems() {
        return sellableItems;
    }

    public void removeTodaySellableItem(SellableItem item) {
        todaySellableItems.remove(item);
    }

    public void removeSellableItem(SellableItem sellableItem) {
        sellableItems.remove(sellableItem);
        todaySellableItems.remove(sellableItem);
    }

    public void addSellableItem(SellableItem sellableItem) {
        sellableItems.add(sellableItem);
    }

    public int getNbDailySellableItems() {
        return nbDailySellableItems;
    }
}
