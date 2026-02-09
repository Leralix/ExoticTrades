package io.github.leralix.exotictrades.listener;

import io.github.leralix.exotictrades.guis.admin.ManageTrader;
import io.github.leralix.exotictrades.guis.player.SellItemMenu;
import io.github.leralix.exotictrades.market.StockMarketManager;
import io.github.leralix.exotictrades.storage.MarketItemStorage;
import io.github.leralix.exotictrades.storage.StorageForGui;
import io.github.leralix.exotictrades.storage.TraderStorage;
import io.github.leralix.exotictrades.traders.Trader;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class InteractWithTrader implements Listener {

    private final boolean blockVanillaTrade;

    private final StorageForGui storage;

    public InteractWithTrader(TraderStorage traderStorage, MarketItemStorage marketItemStorage, StockMarketManager stockMarketManager, boolean blockVanillaTrade) {
        this.blockVanillaTrade = blockVanillaTrade;
        this.storage = new StorageForGui(traderStorage, marketItemStorage, stockMarketManager);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        if (event.getRightClicked() instanceof Villager villager) {

            if(blockVanillaTrade){
                event.setCancelled(true);
            }

            for(String tag : villager.getScoreboardTags()){
                if(tag.startsWith("exoticTrade_")){
                    handleTraderInteraction(event, villager, tag, player);
                    return;
                }
            }
        }
    }

    private void handleTraderInteraction(PlayerInteractEntityEvent event, Villager villager, String tag, Player player) {
        event.setCancelled(true);
        String id = tag.split("_")[1];
        Trader trader = storage.traderStorage().get(id);
        if(trader == null){
            villager.remove();
            return;
        }
        if(player.isSneaking() && player.hasPermission("exotictrades.admin.manage_trader")){
            new ManageTrader(player, trader, storage).open();
            event.setCancelled(true);
        }
        else {
            new SellItemMenu(player, trader, storage).open();
        }
    }

}
