package org.leralix.exotictrades.listener.chat.events;

import org.bukkit.entity.Player;
import org.leralix.exotictrades.guis.OpenTraderMenu;
import org.leralix.exotictrades.listener.chat.ChatListenerEvent;
import org.leralix.exotictrades.traders.Trader;

public class RenameTraderChatListener extends ChatListenerEvent {
    Trader trader;

    public RenameTraderChatListener(Trader trader) {
        this.trader = trader;
    }

    @Override
    public void execute(Player player, String message) {
        trader.setName(message);
        new OpenTraderMenu(player, trader).open();
    }
}
