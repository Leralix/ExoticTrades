package io.github.leralix;


import io.github.leralix.getters.ExTraderManager;
import org.leralix.lib.data.PluginVersion;


/**
 * The main entry point to the TanAPI.
 * <p>
 * Use {@link #getInstance()} to access the instance.
 */

public abstract class ExtradeAPI {

    /**
     * The TanAPI instance
     */
    private static ExtradeAPI instance;

    protected ExtradeAPI() {

    }

    /**
     * Register the TanAPI instance
     * This method should never be called outside the TanAPI implementation
     * @param api the TanAPI instance
     */
    public static void register(ExtradeAPI api) {
        if (instance != null) {
            throw new IllegalStateException("TanAPI is already initialised !");
        }
        instance = api;
    }

    /**
     * TanAPI entry point
     * @return the TanAPI instance
     */

    public static ExtradeAPI getInstance() {
        if (instance == null) {
            throw new IllegalStateException("TanAPI is not yet initialised !");
        }
        return instance;
    }

    /**
     * @return the player manager, responsible for player access
     */
    public abstract ExTraderManager getPlayerManager();

    /**
     * Get the minimum supporting map plugin version, used for ensuring compatibility with the map plugin
     * @return the minimum supporting map plugin version
     */
    public abstract PluginVersion getMinimumSupportingMapPlugin();

}