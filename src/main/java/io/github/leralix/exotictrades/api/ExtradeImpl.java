package io.github.leralix.exotictrades.api;

import io.github.leralix.ExtradeAPI;
import io.github.leralix.getters.ExTraderManager;
import io.github.leralix.exotictrades.ExoticTrades;
import io.github.leralix.exotictrades.api.getters.ExTraderManagerImpl;
import org.bukkit.plugin.Plugin;
import org.leralix.lib.data.PluginVersion;

public class ExtradeImpl extends ExtradeAPI {

    private final ExTraderManagerImpl exTraderManager;
    private final ExoticTrades pluginInstance;

    public ExtradeImpl(ExoticTrades pluginInstance) {
        this.exTraderManager = new ExTraderManagerImpl();
        this.pluginInstance = pluginInstance;
    }

    @Override
    public ExTraderManager getPlayerManager() {
        return exTraderManager;
    }

    @Override
    public PluginVersion getMinimumSupportingMapPlugin() {
        return pluginInstance.getMinimumSupportingMapPlugin();
    }
}
