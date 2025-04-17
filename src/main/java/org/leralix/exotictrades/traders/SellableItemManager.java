package org.leralix.exotictrades.traders;

import org.leralix.exotictrades.item.SellableItem;
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
        todaySellableItems.clear();
        for (int i = 0; i < nbDailySellableItems; i++) {
            int randomIndex = RandomUtil.getRandom().nextInt(sellableItems.size());
            todaySellableItems.add(sellableItems.get(randomIndex));
        }
    }

    public List<SellableItem> getTodaySellableItems() {
        return todaySellableItems;
    }

    public void setSellableItems(List<SellableItem> sellableItems) {
        this.sellableItems = sellableItems;
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
}
