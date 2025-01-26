package org.leralix.exotictrades.market;

public interface IMarket {

        int computeNewPrice(int actualPrice, int quantityWanted, int quantitySold, int maxPrice, int minPrice, float variation);
}
