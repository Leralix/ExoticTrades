package io.github.leralix.exotictrades.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import io.github.leralix.exotictrades.item.MarketItem;
import io.github.leralix.exotictrades.item.RareItem;
import io.github.leralix.exotictrades.lang.Lang;
import io.github.leralix.exotictrades.storage.MarketItemStorage;
import io.github.leralix.exotictrades.util.StringUtil;
import org.leralix.lib.commands.SubCommand;

import java.util.ArrayList;
import java.util.List;

public class DropRareItem extends SubCommand {

    @Override
    public String getName() {
        return "drop";
    }

    @Override
    public String getDescription() {
        return Lang.ADMIN_SPAWN_RARE_ITEM.get();
    }

    @Override
    public int getArguments() {
        return 1;
    }

    @Override
    public String getSyntax() {
        return "/extrade drop <item> [amount] <world_name> <x> <y> <z>";
    }

    public List<String> getTabCompleteSuggestions(CommandSender sender, String lowerCase, String[] args){
        List<String> suggestions = new ArrayList<>();
        if (args.length == 2) {
            List<RareItem> rareItemList = MarketItemStorage.getAllRareItems();
            for (RareItem rareItem : rareItemList) {
                suggestions.add(rareItem.getName().replace(" ", "_"));
            }
        }
        if(args.length == 3){
            suggestions.add("1");
            suggestions.add("64");
        }
        if(args.length == 4){
            for(World world : Bukkit.getWorlds()){
                suggestions.add(world.getName());
            }
        }
        if(args.length == 5 || args.length == 6 || args.length == 7){
            suggestions.add("0");
        }
        return suggestions;
    }
    @Override
    public void perform(CommandSender sender, String[] args) {

        if(args.length != 7) {
            return;
        }

        String itemName = args[1].replace("_", " ");
        MarketItem marketItem = MarketItemStorage.getMarketItem(itemName);

        if (marketItem == null) {
            sender.sendMessage(StringUtil.getPluginString() + Lang.ITEM_NOT_FOUND.get());
            return;
        }
        int amount = Integer.parseInt(args[2]);

        String worldName = args[3];
        double x = Double.parseDouble(args[4]);
        double y = Double.parseDouble(args[5]);
        double z = Double.parseDouble(args[6]);

        World world = Bukkit.getWorld(worldName);
        if(world == null){
            sender.sendMessage(StringUtil.getPluginString() + Lang.COMMAND_WORLD_NOT_FOUND.get(worldName));
            return;
        }

        Location location = new Location(world, x, y, z);
        world.dropItem(location, marketItem.getItemStack(amount));

        sender.sendMessage(StringUtil.getPluginString() + Lang.COMMAND_GENERIC_SUCCESS.get());
    }
}