package org.leralix.exotictrades.listener.chat;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.leralix.exotictrades.traders.position.FixedPosition;
import org.leralix.lib.position.Vector3D;

public class RegisterNewTraderPosition extends ChatListenerEvent {

    private final FixedPosition position;


    public RegisterNewTraderPosition(Player player, FixedPosition position) {
        player.sendMessage("Please write the position of the trader in the format x;y;z or write here to use your current position");
        this.position = position;
    }

    @Override
    protected void execute(Player player, String message) {
        Location location;
        if(message.equalsIgnoreCase("here")){
            location = player.getLocation();
        }
        else {
            String[] coordinates = message.split(";");
            World world = player.getWorld();
            double x = Double.parseDouble(coordinates[0]);
            double y = Double.parseDouble(coordinates[1]);
            double z = Double.parseDouble(coordinates[2]);
            location = new Location(world, x, y, z);
        }
        Vector3D vector3D = new Vector3D(location);
        position.addPosition(vector3D);
        PlayerChatListenerStorage.removePlayer(player);

    }
}
