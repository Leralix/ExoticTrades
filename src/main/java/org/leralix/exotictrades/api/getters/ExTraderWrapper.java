package org.leralix.exotictrades.api.getters;

import io.github.leralix.interfaces.ExRareItem;
import io.github.leralix.interfaces.ExTrader;
import org.leralix.exotictrades.traders.Trader;
import org.leralix.lib.position.Vector3D;

import java.util.List;

public class ExTraderWrapper implements ExTrader {

    private final Trader trader;

    private ExTraderWrapper(Trader trader){
        this.trader = trader;
    }

    public static Object of(Trader trader) {
        return new ExTraderWrapper(trader);
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
        return trader.getMarketItems().stream()
                .map(ExRareItemImpl::of)
                .map(ExRareItem.class::cast)
                .toList();
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
