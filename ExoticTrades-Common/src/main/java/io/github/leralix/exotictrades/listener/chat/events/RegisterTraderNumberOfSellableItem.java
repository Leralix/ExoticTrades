package io.github.leralix.exotictrades.listener.chat.events;

import io.github.leralix.exotictrades.guis.admin.ManageTraderItemToSell;
import io.github.leralix.exotictrades.lang.Lang;
import io.github.leralix.exotictrades.listener.chat.ChatListenerEvent;
import io.github.leralix.exotictrades.storage.StorageForGui;
import io.github.leralix.exotictrades.traders.Trader;
import org.bukkit.entity.Player;

public class RegisterTraderNumberOfSellableItem extends ChatListenerEvent {

    private final Trader trader;
    private final StorageForGui storage;

    public RegisterTraderNumberOfSellableItem(Trader trader, StorageForGui storage) {
        this.trader = trader;
        this.storage = storage;
    }


    @Override
    protected void execute(Player player, String message) {

        message = message.trim();
        try {
            int nbItems = Integer.parseInt(message);
            if (nbItems < 0) {
                player.sendMessage("Please enter a valid number");
                return;
            }
            trader.getSellableItemManager().setNbDailySellableItems(nbItems);
            player.sendMessage(Lang.NUMBER_OF_ITEM_SOLD_PER_DAY_SET.get(trader.getName(), nbItems));
            openGui(player1 -> new ManageTraderItemToSell(player, trader, storage).open(), player);
        } catch (NumberFormatException e) {
            player.sendMessage("Please enter a valid number");
        }
    }
}
