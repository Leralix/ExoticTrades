package org.leralix.exotictrades.market;

import org.bukkit.entity.Player;

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
    private List<Integer> amountOfSells;
    private List<Set<UUID>> playersConnections;

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
        this.playersConnections = new ArrayList<>(timeLength);

        // Initialize the amountOfSells list with zeros
        for (int i = 0; i < timeLength; i++) {
            this.amountOfSells.add(0);
        }

        // Initialize the playersConnections list with zeros
        for (int i = 0; i < timeLength; i++) {
            Set<UUID> playerList = new HashSet<>();
            this.playersConnections.add(playerList);
        }
    }

    // Getters and Setters (optional, depending on your needs)
    public int getTimeLength() {
        return timeLength;
    }

    public float getMaxPrice() {
        return maxPrice;
    }

    public float getMinPrice() {
        return minPrice;
    }

    public float getMidPrice() {
        return midPrice;
    }

    public float getKprice() {
        return Kprice;
    }

    public float getKexpectedSells() {
        return KexpectedSells;
    }

    public int getCursor() {
        return cursor;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public int getExpectedSells() {
        return expectedSells;
    }

    public void setExpectedSells(int expectedSells) {
        this.expectedSells = expectedSells;
    }

    public float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
    }

    public float getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(float lastPrice) {
        this.lastPrice = lastPrice;
    }

    public List<Integer> getAmountOfSells() {
        return amountOfSells;
    }

    public void setAmountOfSells(List<Integer> amountOfSells) {
        this.amountOfSells = amountOfSells;
    }

    public List<Set<UUID>> getPlayersConnections() {
        return playersConnections;
    }

    public void setPlayersConnections(List<Set<UUID>> playersConnections) {
        this.playersConnections = playersConnections;
    }

    // Method to update the amount of sells at the current cursor position
    public void updateAmountOfSells(int sells) {
        if (cursor >= 0 && cursor < timeLength) {
            amountOfSells.set(cursor, sells);
        } else {
            throw new IndexOutOfBoundsException("Cursor is out of bounds.");
        }
    }

    // Method to add an amount to the amount of sells at the current cursor position
    public void addAmountOfSells(int sells) {
        if (cursor >= 0 && cursor < timeLength) {
            amountOfSells.set(cursor, amountOfSells.get(cursor) + sells);
        } else {
            throw new IndexOutOfBoundsException("Cursor is out of bounds.");
        }
    }

    // Method to add a player to the player list at the current cursor position
    public void addPlayersConnections(UUID playerUuid) {
        if (cursor >= 0 && cursor < timeLength) {
            playersConnections.get(cursor).add(playerUuid);
        } else {
            throw new IndexOutOfBoundsException("Cursor is out of bounds.");
        }
    }

    // Updating the expected selling price mainly based on the amount of players that joined the server
    public void updateExpectedSells() {
        Set<UUID> uniquePlayers = new HashSet<>();

        // Collect unique players
        for (Set<UUID> connectionGroup : playersConnections) {
            if (connectionGroup != null) {
                uniquePlayers.addAll(connectionGroup);
            }
        }

        // Calculate expected sells using unique player count, midPrice, and KexpectedSells
        int uniquePlayerCount = uniquePlayers.size();
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

        // 5. Update tracking variables
        cursor = (cursor + 1) % timeLength; // Circular buffer behavior
    }

//    // Full iteration processor
//    public List<Float> processPriceUpdates(int iterations) {
//        List<Float> priceHistory = new ArrayList<>();
//        for (int i = 0; i < iterations; i++) {
//            updatePrice();
//            priceHistory.add(currentPrice);
//        }
//        return priceHistory;
//    }
}
