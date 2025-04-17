package org.leralix.exotictrades.listener;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.leralix.exotictrades.guis.ManageTrader;
import org.leralix.exotictrades.guis.SellItemMenu;
import org.leralix.exotictrades.storage.TraderStorage;
import org.leralix.exotictrades.traders.Trader;

public class InteractWithTrader implements Listener {


    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        if (event.getRightClicked() instanceof Villager villager) {
            for(String tag : villager.getScoreboardTags()){
                if(tag.startsWith("exoticTrade_")){
                    handleTraderInteraction(event, villager, tag, player);
                    return;
                }
            }
        }
    }

    private static void handleTraderInteraction(PlayerInteractEntityEvent event, Villager villager, String tag, Player player) {
        event.setCancelled(true);
        String id = tag.split("_")[1];
        Trader trader = TraderStorage.get(id);
        if(trader == null){
            villager.remove();
            return;
        }
        if(player.isSneaking() && player.hasPermission("exotictrades.admin.manage_trader")){
            new ManageTrader(player, trader).open();
            event.setCancelled(true);
        }
        else {
            new SellItemMenu(player, trader).open();
        }
    }

}
