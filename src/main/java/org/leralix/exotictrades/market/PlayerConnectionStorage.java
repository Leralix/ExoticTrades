package org.leralix.exotictrades.market;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.leralix.exotictrades.ExoticTrades;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.UUID;

public class PlayerConnectionStorage {

    private PlayerConnectionStorage() {
        throw new IllegalStateException("Utility class");
    }

    private static HashMap<UUID, Long> playersConnections = new HashMap<>();

    public static void newConnection(UUID playerID){
        if(playersConnections.containsKey(playerID)){
            playersConnections.replace(playerID, System.currentTimeMillis());
        } else {
            playersConnections.put(playerID, System.currentTimeMillis());
        }
    }

    public static int getNumberOfConnections(){
        updateConnections();
        return playersConnections.size();
    }

    private static void updateConnections() {
        long timeBeforeRemoval = (long) 7 * 24 * 60 * 60 * 1000;
        playersConnections.keySet().removeIf(playerID -> System.currentTimeMillis() - playersConnections.get(playerID) > timeBeforeRemoval);
    }

    public static void save() {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File storageFolder = new File(ExoticTrades.getPlugin().getDataFolder().getAbsolutePath() + "/storage");
        storageFolder.mkdir();
        File jsonFile = new File(storageFolder.getAbsolutePath() + "/json");
        jsonFile.mkdir();
        File file = new File(jsonFile.getAbsolutePath() + "/players.json");

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
        gson.toJson(playersConnections, writer);
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

    public static void load(){

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(ExoticTrades.getPlugin().getDataFolder().getAbsolutePath() + "/storage/json/players.json");
        if (!file.exists())
            return;

        Reader reader;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Type type = new TypeToken<HashMap<UUID, Long>>() {}.getType();
        playersConnections = gson.fromJson(reader, type);
    }
}
