package io.github.leralix.exotictrades.listener.chat.events;

import io.github.leralix.exotictrades.guis.ManageTraderItemToSell;
import io.github.leralix.exotictrades.item.SellableItem;
import io.github.leralix.exotictrades.listener.chat.ChatListenerEvent;
import io.github.leralix.exotictrades.listener.chat.PlayerChatListenerStorage;
import io.github.leralix.exotictrades.storage.StorageForGui;
import io.github.leralix.exotictrades.traders.Trader;
import org.bukkit.entity.Player;
import org.leralix.lib.data.SoundEnum;
import org.leralix.lib.utils.SoundUtil;

public class ChangeItemPrice extends ChatListenerEvent {

    private final Trader trader;
    private final SellableItem item;
    private final StorageForGui storage;

    public ChangeItemPrice(Trader trader, SellableItem item, StorageForGui storage) {
        super();
        this.trader = trader;
        this.item = item;
        this.storage = storage;
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
            openGui(p -> new ManageTraderItemToSell(p, trader, storage).open(), player);
        } catch (NumberFormatException e) {
            player.sendMessage("Please enter a valid number");
        }
    }


}
