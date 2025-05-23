package org.leralix.exotictrades.listener.chat.events;

import org.bukkit.entity.Player;
import org.leralix.exotictrades.guis.ManageTraderItemToSell;
import org.leralix.exotictrades.item.SellableItem;
import org.leralix.exotictrades.listener.chat.ChatListenerEvent;
import org.leralix.exotictrades.listener.chat.PlayerChatListenerStorage;
import org.leralix.exotictrades.traders.Trader;
import org.leralix.lib.data.SoundEnum;
import org.leralix.lib.utils.SoundUtil;

public class ChangeItemPrice extends ChatListenerEvent {

    private final Trader trader;
    private final SellableItem item;

    public ChangeItemPrice(Trader trader, SellableItem item) {
        super();

        this.trader = trader;
        this.item = item;
    }

    @Override
    protected void execute(Player player, String message) {

        message = message.trim();
        try {
            int price = Integer.parseInt(message);
            if(price < 1){
                player.sendMessage("Please enter a valid number");
                return;
            }
            PlayerChatListenerStorage.removePlayer(player);
            item.setPrice(price);
            SoundUtil.playSound(player, SoundEnum.MINOR_GOOD);
            openGui(p -> new ManageTraderItemToSell(p, trader).open(), player);
        } catch (NumberFormatException e) {
            player.sendMessage("Please enter a valid number");
        }
    }


}
