package org.leralix.exotictrades.market;


import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class StockMarket {
    // Initialization parameters
    private final int timeLength;
    private final float maxPrice;
    private final float minPrice;
    private final float midPrice;
    private final float Kprice; // Coefficient for the price increase after sells
    private final float KexpectedSells; // Coefficient for 

    // Internal private variables
    private int cursor;
    private int expectedSells;
    private float currentPrice;
    private float lastPrice;
    private final List<Integer> amountOfSells;



    // Constructor
    public StockMarket(int timeLength, float maxPrice, float minPrice, float midPrice, float Kprice, float KexpectedSells) {
        this.timeLength = timeLength;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.midPrice = midPrice;
        this.Kprice = Kprice;
        this.KexpectedSells = KexpectedSells;

        // Initialize internal variables
        this.cursor = 0;
        this.expectedSells = 0;
        this.currentPrice = 0;
        this.lastPrice = 0;
        this.amountOfSells = new ArrayList<>(timeLength);

        // Initialize the amountOfSells list with zeros
        for (int i = 0; i < timeLength; i++) {
            this.amountOfSells.add(0);
        }
    }

    public void updateToNextCursor() {
        cursor = updateCursor();
        amountOfSells.set(cursor, 0);
    }

    private int updateCursor() {
        return cursor + 1 >= timeLength ? 0 : cursor + 1;
    }

    // Method to add an amount to the amount of sells at the current cursor position
    public void addAmountOfSells(int sells) {
        if (cursor >= 0 && cursor < timeLength) {
            amountOfSells.set(cursor, amountOfSells.get(cursor) + sells);
        } else {
            throw new IndexOutOfBoundsException("Cursor is out of bounds.");
        }
    }

    // Updating the expected selling price mainly based on the amount of players that joined the server
    public void updateExpectedSells() {
        int uniquePlayerCount = StockMarketManager.getNumberOfConnections();
        this.expectedSells = (int) (uniquePlayerCount * midPrice * KexpectedSells);
    }
    
    // Core price update logic
    public void updatePrice() {
        // Get sells data from current cursor position
        int soldAmount = amountOfSells.get(cursor);
        // Update the las price to the old current price
        lastPrice = currentPrice;
        // Update the expected sells value
        updateExpectedSells();

        // 1. Calculate normalized price
        float Cprice;
        float normalizedPrice;

        if (currentPrice <= midPrice) {
            // Normalize between minPrice and midPrice
            normalizedPrice = (currentPrice - minPrice) / (midPrice - minPrice);
            // Scale to range [0.5, 1]
            Cprice = 0.5f + 0.5f * normalizedPrice;
        } else {
            // Normalize between midPrice and maxPrice
            normalizedPrice = (currentPrice - midPrice) / (maxPrice - midPrice);
            // Scale to range [1, 0.5]
            Cprice = 1.0f - 0.5f * normalizedPrice;
        }

        // 3. Calculate sales difference
        int difference = Math.abs(expectedSells - soldAmount);

        // 4. Determine price change direction
        float deltaPrice = 0;
        if (soldAmount != expectedSells) {
            if (soldAmount < expectedSells) {
                deltaPrice = currentPrice > midPrice ?
                        (difference / (float) expectedSells) * Cprice * Kprice :
                        (difference / (float) expectedSells) * (2 - Cprice) * Kprice;
                currentPrice = Math.min(currentPrice + deltaPrice, maxPrice);
            } else {
                deltaPrice = currentPrice > midPrice ?
                        (difference / (float) expectedSells) * (2 - Cprice) * Kprice :
                        (difference / (float) expectedSells) * Cprice * Kprice;
                currentPrice = Math.max(currentPrice - deltaPrice, minPrice);
            }
        }
    }

    public double sell(int amount){
        double total = 0;
        for(int i = 0; i < amount; i++){
            total += currentPrice;
        }

        addAmountOfSells(amount);

        return total;
    }


    public void updateMovingAverage() {
        updatePrice();
        updateToNextCursor();
    }

    public int getNumberOfSoldItems() {
        int total = 0;
        for(int i : amountOfSells){
            total += i;
        }
        return total;
    }
}
