package io.github.leralix.exotictrades.api.getters;

import io.github.leralix.exotictrades.item.MarketItem;
import io.github.leralix.exotictrades.market.StockMarket;
import io.github.leralix.exotictrades.market.StockMarketManager;
import io.github.leralix.exotictrades.storage.MarketItemKey;
import io.github.leralix.exotictrades.storage.MarketItemStorage;
import io.github.leralix.exotictrades.traders.Trader;
import io.github.leralix.interfaces.ExRareItem;
import io.github.leralix.interfaces.ExTrader;
import org.leralix.lib.position.Vector3D;

import java.util.ArrayList;
import java.util.List;

public class ExTraderWrapper implements ExTrader {

    private final Trader trader;
    private final StockMarketManager stockMarketManager;
    private final MarketItemStorage marketItemStorage;

    private ExTraderWrapper(Trader trader, StockMarketManager stockMarketManager, MarketItemStorage marketItemStorage){
        this.trader = trader;
        this.stockMarketManager = stockMarketManager;
        this.marketItemStorage = marketItemStorage;
    }

    public static Object of(Trader trader, StockMarketManager stockMarketManager, MarketItemStorage marketItemStorage) {
        return new ExTraderWrapper(trader, stockMarketManager, marketItemStorage);
    }

    @Override
    public String getID() {
        return trader.getID();
    }

    @Override
    public String getName() {
        return trader.getName();
    }

    @Override
    public List<ExRareItem> getItemsSold() {

        List<ExRareItem> itemsSold = new ArrayList<>();
        for (MarketItemKey marketItemKey : trader.getMarketItemsKey()){
            MarketItem marketItem = marketItemStorage.getMarketItem(marketItemKey);
            StockMarket stockMarket = stockMarketManager.getMarketFor(MarketItemKey.of(marketItem));

            itemsSold.add(ExRareItemImpl.of(marketItem, stockMarket));
        }
        return itemsSold;
    }

    @Override
    public Vector3D getCurrentPosition() {
        return trader.getPosition().getCurrentPosition();
    }

    @Override
    public List<Vector3D> getPotentialPosition() {
        return  trader.getPosition().getAllPositions();
    }

    @Override
    public int getNbHoursBeforeNextPosition() {
        return trader.getPosition().getNbHoursBeforeNextPosition();
    }
}
