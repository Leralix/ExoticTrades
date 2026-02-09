package io.github.leralix.exotictrades.listener.chat.events;

import io.github.leralix.exotictrades.guis.admin.ManageTrader;
import io.github.leralix.exotictrades.listener.chat.ChatListenerEvent;
import io.github.leralix.exotictrades.listener.chat.PlayerChatListenerStorage;
import io.github.leralix.exotictrades.storage.StorageForGui;
import io.github.leralix.exotictrades.traders.Trader;
import org.bukkit.entity.Player;

public class RenameTraderChatListener extends ChatListenerEvent {
    private final Trader trader;
    private final StorageForGui storage;

    public RenameTraderChatListener(Trader trader, StorageForGui storage) {
        this.trader = trader;
        this.storage = storage;
    }

    @Override
    public void execute(Player player, String message) {
        PlayerChatListenerStorage.removePlayer(player);
        trader.setName(message);
        openGui(player1 -> new ManageTrader(player1, trader, storage).open(), player);
    }
}
