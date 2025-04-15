package org.leralix.exotictrades.api;

import io.github.leralix.ExtradeAPI;
import io.github.leralix.getters.ExTraderManager;
import org.leralix.exotictrades.ExoticTrades;
import org.leralix.exotictrades.api.getters.ExTraderManagerImpl;
import org.leralix.lib.data.PluginVersion;

public class ExtradeImpl extends ExtradeAPI {

    ExTraderManagerImpl exTraderManager;

    public ExtradeImpl() {
        this.exTraderManager = new ExTraderManagerImpl();
    }

    @Override
    public ExTraderManager getPlayerManager() {
        return exTraderManager;
    }

    @Override
    public PluginVersion getMinimumSupportingMapPlugin() {
        return ExoticTrades.getMinimumSupportingMapPlugin();
    }
}
