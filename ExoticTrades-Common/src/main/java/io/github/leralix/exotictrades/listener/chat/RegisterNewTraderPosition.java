package io.github.leralix.exotictrades.listener.chat;

import io.github.leralix.exotictrades.lang.Lang;
import io.github.leralix.exotictrades.traders.position.FixedPosition;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.leralix.lib.position.Vector3D;

public class RegisterNewTraderPosition extends ChatListenerEvent {

    private final FixedPosition position;


    public RegisterNewTraderPosition(Player player, FixedPosition position) {
        player.sendMessage(Lang.WRITE_POSITION_OF_TRADER_OR_HERE.get());
        this.position = position;
    }

    @Override
    protected boolean execute(Player player, String message) {
        Location location;
        if(message.equalsIgnoreCase("here")){
            location = player.getLocation();
        }
        else {
            String[] coordinates = message.split(";");
            if(coordinates.length != 3){
                player.sendMessage(Lang.SYNTAX_ERROR.get());
                return false;
            }
            try {
                World world = player.getWorld();
                double x = Double.parseDouble(coordinates[0]);
                double y = Double.parseDouble(coordinates[1]);
                double z = Double.parseDouble(coordinates[2]);
                location = new Location(world, x, y, z);
            }
            catch (NumberFormatException invalidNumber) {
                player.sendMessage(Lang.SYNTAX_ERROR.get());
                return false;
            }
        }
        Vector3D vector3D = new Vector3D(location);
        position.addPosition(vector3D);
        player.sendMessage(Lang.NEW_TRADER_POSITION_REGISTERED.get(vector3D));
        return true;
    }
}
