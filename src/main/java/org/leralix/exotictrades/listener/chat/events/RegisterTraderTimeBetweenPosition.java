package org.leralix.exotictrades.listener.chat.events;

import org.bukkit.entity.Player;
import org.leralix.exotictrades.guis.ManageTraderPosition;
import org.leralix.exotictrades.lang.Lang;
import org.leralix.exotictrades.listener.chat.ChatListenerEvent;
import org.leralix.exotictrades.traders.Trader;

public class RegisterTraderTimeBetweenPosition extends ChatListenerEvent {

    private final Trader trader;

    public RegisterTraderTimeBetweenPosition(Player player, Trader trader) {
        super();
        this.trader = trader;
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
            openGui(player1 -> new ManageTraderPosition(player, trader).open(),player);
        } catch (NumberFormatException e) {
            player.sendMessage("Please enter a valid number");
        }

    }
}
