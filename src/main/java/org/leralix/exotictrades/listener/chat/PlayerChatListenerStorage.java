package org.leralix.exotictrades.listener.chat;

import org.bukkit.entity.Player;
import org.leralix.exotictrades.ExoticTrades;
import org.leralix.exotictrades.lang.Lang;
import org.leralix.lib.data.SoundEnum;
import org.leralix.lib.utils.SoundUtil;

import java.util.HashMap;
import java.util.Map;

public class PlayerChatListenerStorage {

    private static final Map<Player, ChatListenerEvent> chatStorage = new HashMap<>();

    public static void register(Player player, ChatListenerEvent category) {
        chatStorage.put(player, category);
        player.sendMessage(ExoticTrades.getNameString() + Lang.WRITE_CANCEL_TO_CANCEL.get(Lang.CANCEL_WORD.get()));
        SoundUtil.playSound(player, SoundEnum.WRITE);
        player.closeInventory();
    }

    public static ChatListenerEvent getPlayer(Player player){
        return chatStorage.get(player);
    }

    public static void removePlayer(Player p) {
        chatStorage.remove(p);
    }

    public static boolean contains(Player player){
        return chatStorage.containsKey(player);
    }
}