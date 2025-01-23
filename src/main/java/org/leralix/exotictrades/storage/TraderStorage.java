package org.leralix.exotictrades.storage;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Villager;
import org.leralix.exotictrades.ExoticTrades;
import org.leralix.exotictrades.traders.Trader;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class TraderStorage {

    private static Map<String, Trader> traders = new HashMap<>();
    private static int nextID = 0;


    public static void register(Location location) {
        String id = "T" + nextID;
        Trader trader = new Trader(id, location);
        traders.put(trader.getID(), trader);
        nextID++;
        save();
    }

    public static Trader get(String id) {
        return traders.get(id);
    }

    public static Trader get(Villager villager) {
        Optional<String> id = villager.getScoreboardTags().stream().filter(tag -> tag.startsWith("exoticTrade_")).findFirst();
        return id.map(s -> traders.get(s)).orElse(null);
    }

    public static Collection<Trader> getAll() {
        return Collections.unmodifiableCollection(traders.values());
    }

    public static void load(){

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(ExoticTrades.getPlugin().getDataFolder().getAbsolutePath() + "storage/json/traders.json");
        if (!file.exists())
            return;

        Reader reader;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Type type = new TypeToken<HashMap<String, Trader>>() {}.getType();
        traders = gson.fromJson(reader, type);

        int id = 0;
        for (String ids: traders.keySet()) {
            int newID =  Integer.parseInt(ids.substring(1));
            if(newID > id)
                id = newID;
        }
        nextID = id+1;

    }

    public static void save() {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File storageFolder = new File(ExoticTrades.getPlugin().getDataFolder().getAbsolutePath() + "/storage");
        storageFolder.mkdir();
        File jsonFile = new File(storageFolder.getAbsolutePath() + "/json");
        jsonFile.mkdir();
        File file = new File(jsonFile.getAbsolutePath() + "/traders.json");

        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Writer writer;
        try {
            writer = new FileWriter(file, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gson.toJson(traders, writer);
        try {
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
