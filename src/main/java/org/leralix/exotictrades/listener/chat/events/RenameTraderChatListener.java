package org.leralix.exotictrades.listener.chat.events;

import org.bukkit.entity.Player;
import org.leralix.exotictrades.guis.ManageTrader;
import org.leralix.exotictrades.listener.chat.ChatListenerEvent;
import org.leralix.exotictrades.listener.chat.PlayerChatListenerStorage;
import org.leralix.exotictrades.traders.Trader;

public class RenameTraderChatListener extends ChatListenerEvent {
    Trader trader;

    public RenameTraderChatListener(Trader trader) {
        this.trader = trader;
    }

    @Override
    public void execute(Player player, String message) {
        PlayerChatListenerStorage.removePlayer(player);
        trader.setName(message);
        openGui(player1 -> new ManageTrader(player1, trader).open(), player);
    }
}
