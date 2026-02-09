package io.github.leralix.exotictrades.api.getters;

import io.github.leralix.exotictrades.market.StockMarketManager;
import io.github.leralix.exotictrades.storage.MarketItemStorage;
import io.github.leralix.exotictrades.storage.TraderStorage;
import io.github.leralix.getters.ExTraderManager;
import io.github.leralix.interfaces.ExTrader;

import java.util.List;

public class ExTraderManagerImpl implements ExTraderManager {

    private final TraderStorage traderStorage;
    private final StockMarketManager stockMarketManager;
    private final MarketItemStorage marketItemStorage;


    public ExTraderManagerImpl(TraderStorage traderStorage, StockMarketManager stockMarketManager, MarketItemStorage marketItemStorage){
        this.traderStorage = traderStorage;
        this.stockMarketManager = stockMarketManager;
        this.marketItemStorage = marketItemStorage;
    }

    @Override
    public ExTrader getTrader(String s) {
        return null;
    }

    @Override
    public List<ExTrader> getTraders() {
        return traderStorage.getAll().stream()
                .map(trader -> ExTraderWrapper.of(trader, stockMarketManager, marketItemStorage))
                .map(ExTrader.class::cast)
                .toList();

    }
}
