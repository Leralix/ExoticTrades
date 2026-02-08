package io.github.leralix.exotictrades.storage;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.leralix.exotictrades.ExoticTrades;
import io.github.leralix.exotictrades.storage.adapters.TraderPositionTypeAdapter;
import io.github.leralix.exotictrades.traders.Trader;
import io.github.leralix.exotictrades.traders.position.TraderPosition;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Villager;
import org.leralix.lib.position.Vector2D;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class TraderStorage {

    private Map<String, Trader> traders;
    private final Map<Vector2D, List<Trader>> traderPosition;
    private int nextID;


    public TraderStorage(){
        this.traders = new HashMap<>();
        this.traderPosition = new HashMap<>();
        this.nextID = 0;
    }


    public void register(Location location, Collection<MarketItemKey> acceptedMarketItems) {
        String id = "T" + nextID;
        Trader trader = new Trader(id, location, acceptedMarketItems);
        traders.put(trader.getID(), trader);
        nextID++;
        updateTraderPosition();
        save();
    }

    public Trader get(String id) {
        return traders.get(id);
    }

    public Trader get(Villager villager) {
        Optional<String> id = villager.getScoreboardTags().stream().filter(tag -> tag.startsWith("exoticTrade_")).findFirst();
        return id.map(s -> traders.get(s)).orElse(null);
    }

    public Collection<Trader> getAll() {
        return Collections.unmodifiableCollection(traders.values());
    }

    public void updateTraderPosition() {
        for(Trader trader : traders.values()){
            Vector2D chunkVector = trader.getChunkPosition();
            if(traderPosition.containsKey(chunkVector)){
                traderPosition.get(chunkVector).add(trader);
            }else{
                List<Trader> list = new ArrayList<>();
                list.add(trader);
                traderPosition.put(chunkVector, list);
            }
        }
    }

    public List<Trader> getTradersInChunk(Chunk chunk){
        if(!traderPosition.containsKey(new Vector2D(chunk)))
            return Collections.emptyList();
        return traderPosition.get(new Vector2D(chunk));
    }

    public void save() {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(TraderPosition.class, new TraderPositionTypeAdapter())
                .enableComplexMapKeySerialization()
                .setPrettyPrinting()
                .create();

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
        Type traderMapType = new TypeToken<Map<String, Trader>>(){}.getType();
        gson.toJson(traders, traderMapType, writer);
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

    public void load(){

        Gson gson = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(TraderPosition.class, new TraderPositionTypeAdapter())
                .create();
        File file = new File(ExoticTrades.getPlugin().getDataFolder().getAbsolutePath() + "/storage/json/traders.json");
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

        updateTraderPosition();
    }


    public void delete(Trader trader) {
        trader.delete();
        traders.remove(trader.getID());
        updateTraderPosition();
        save();
    }
}
