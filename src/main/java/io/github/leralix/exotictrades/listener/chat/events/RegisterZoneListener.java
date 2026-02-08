package io.github.leralix.exotictrades.listener.chat.events;

import org.bukkit.entity.Player;
import io.github.leralix.exotictrades.guis.ManageTraderPosition;
import io.github.leralix.exotictrades.listener.chat.ChatListenerEvent;
import io.github.leralix.exotictrades.listener.chat.PlayerChatListenerStorage;
import io.github.leralix.exotictrades.traders.position.RandomPosition;
import io.github.leralix.exotictrades.traders.Trader;
import org.leralix.lib.position.Vector3D;
import org.leralix.lib.position.Zone2D;

public class RegisterZoneListener extends ChatListenerEvent {

    private final Trader trader;
    private Vector3D position1;
    private Vector3D position2;
    private final RandomPosition randomRandomPosition;

    public RegisterZoneListener(Trader trader, RandomPosition randomPosition) {
        this.trader = trader;
        this.randomRandomPosition = randomPosition;
    }

    @Override
    public void execute(Player player, String message) {
        if(position1 == null){
            position1 = new Vector3D(player.getLocation());
            player.sendMessage("Position 1 set to " + position1);
            return;
        }

        if(position2 == null){
            position2 = new Vector3D(player.getLocation());
            player.sendMessage("Position 2 set to " + position2);

            randomRandomPosition.setZone(new Zone2D(position1, position2));

            openGui(player1 -> new ManageTraderPosition(player, trader).open(),player);
            PlayerChatListenerStorage.removePlayer(player);
        }
    }
}
