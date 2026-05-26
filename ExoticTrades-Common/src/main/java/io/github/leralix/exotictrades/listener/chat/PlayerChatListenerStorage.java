package io.github.leralix.exotictrades.listener.chat;

import io.github.leralix.exotictrades.ExoticTrades;
import io.github.leralix.exotictrades.lang.Lang;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
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

    public static boolean contains(Player player){
        return chatStorage.containsKey(player);
    }

    public static void execute(Player player, @NotNull String message) {
        ChatListenerEvent event = chatStorage.get(player);
        if(event == null){
            chatStorage.remove(player);
            return;
        }

        boolean success = event.execute(player, message);
        if(success){
            chatStorage.remove(player);
        }
        else {
            player.sendMessage(ExoticTrades.getNameString() + Lang.WRITE_CANCEL_TO_CANCEL.get(Lang.CANCEL_WORD.get()));
        }
    }
}