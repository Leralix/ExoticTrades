package io.github.leralix.exotictrades.listener.chat.events;

import io.github.leralix.exotictrades.guis.ManageTraderPosition;
import io.github.leralix.exotictrades.lang.Lang;
import io.github.leralix.exotictrades.listener.chat.ChatListenerEvent;
import io.github.leralix.exotictrades.storage.StorageForGui;
import io.github.leralix.exotictrades.traders.Trader;
import org.bukkit.entity.Player;

public class RegisterTraderTimeBetweenPosition extends ChatListenerEvent {

    private final Trader trader;
    private final StorageForGui storage;

    public RegisterTraderTimeBetweenPosition(Trader trader, StorageForGui storage) {
        super();
        this.trader = trader;
        this.storage = storage;
    }

    @Override
    protected void execute(Player player, String message) {

        message = message.trim();
        try {
            int time = Integer.parseInt(message);
            if(time < 1){
                player.sendMessage("Please enter a valid number");
                return;
            }
            trader.getPosition().setNumberOfDaysBeforeNextPosition(time);
            player.sendMessage(Lang.NEW_TIME_BETWEEN_POSITION_SET.get(time));
            openGui(player1 -> new ManageTraderPosition(player, trader, storage).open(),player);
        } catch (NumberFormatException e) {
            player.sendMessage("Please enter a valid number");
        }

    }
}
