package io.github.leralix.exotictrades.api.getters;

import io.github.leralix.getters.ExTraderManager;
import io.github.leralix.interfaces.ExTrader;
import io.github.leralix.exotictrades.storage.TraderStorage;

import java.util.List;

public class ExTraderManagerImpl implements ExTraderManager {

    private TraderStorage traderStorage;

    public ExTraderManagerImpl(TraderStorage traderStorage){
        this.traderStorage = traderStorage;
    }

    @Override
    public ExTrader getTrader(String s) {
        return null;
    }

    @Override
    public List<ExTrader> getTraders() {
        return traderStorage.getAll().stream()
                .map(ExTraderWrapper::of)
                .map(ExTrader.class::cast)
                .toList();

    }
}
