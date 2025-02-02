package org.leralix.exotictrades.listener.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.leralix.exotictrades.ExoticTrades;
import org.leralix.exotictrades.lang.Lang;

public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        ChatListenerEvent chatListenerEvent = PlayerChatListenerStorage.getPlayer(player);

        if(chatListenerEvent == null)
            return;

        String message = event.getMessage();
        event.setCancelled(true);

        if (message.equalsIgnoreCase(Lang.CANCEL_WORD.get())) {
            player.sendMessage(ExoticTrades.getNameString() + Lang.CANCELLED_ACTION.get());
            PlayerChatListenerStorage.removePlayer(player);
            return;
        }

        chatListenerEvent.execute(player, message);
    }

}


