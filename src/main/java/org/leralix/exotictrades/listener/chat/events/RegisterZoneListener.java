package org.leralix.exotictrades.listener.chat.events;

import org.bukkit.entity.Player;
import org.leralix.exotictrades.guis.ManageTraderPosition;
import org.leralix.exotictrades.listener.chat.ChatListenerEvent;
import org.leralix.exotictrades.listener.chat.PlayerChatListenerStorage;
import org.leralix.exotictrades.traders.SpawnZone;
import org.leralix.exotictrades.traders.Trader;
import org.leralix.lib.position.Vector3D;
import org.leralix.lib.position.Zone2D;

public class RegisterZoneListener extends ChatListenerEvent {

    private final Trader trader;
    private Vector3D position1;
    private Vector3D position2;

    public RegisterZoneListener(Trader trader) {
        this.trader = trader;
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

            SpawnZone randomSpawnZone = trader.getSpawnZone();
            randomSpawnZone.setZone(new Zone2D(position1, position2));

            openGui(player1 -> new ManageTraderPosition(player, trader).open(),player);
            PlayerChatListenerStorage.removePlayer(player);
        }
    }
}
