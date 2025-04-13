package org.leralix.exotictrades.market;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StockMarketTest {


    @Test
    void testGetDemand() {

        PlayerConnectionStorage.newConnection(UUID.randomUUID());

        double basePrice = 50;
        StockMarket stockMarket = new StockMarket(
                null,
                100,
                0.2,
                10,
                5.0,
                1,
                0.5,
                basePrice,
                7);

        assertEquals(1.0, stockMarket.getDemand());
        assertEquals(50, stockMarket.getCurrentPrice());
        assertEquals(0.0, stockMarket.getPercentSold());
        assertEquals(75.0, stockMarket.getPriceNextHour());

        stockMarket.updateHistory(1);

        assertEquals(basePrice, stockMarket.getPriceNextHour());

        stockMarket.updateHistory(1);

        assertEquals(45, stockMarket.getPriceNextHour());
    }

}