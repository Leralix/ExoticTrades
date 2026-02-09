package io.github.leralix.exotictrades.storage;

import io.github.leralix.exotictrades.market.StockMarketManager;

public record StorageForGui(TraderStorage traderStorage, MarketItemStorage marketItemStorage, StockMarketManager stockMarketManager) {

}
