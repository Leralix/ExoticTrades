package io.github.leralix.exotictrades.storage;

import org.bukkit.configuration.ConfigurationSection;
import io.github.leralix.exotictrades.traders.TraderBiome;
import io.github.leralix.exotictrades.traders.TraderWork;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.EnumMap;
import java.util.Map;

public class VillagerHeadStorage {


    private final Map<TraderBiome, Map<TraderWork, String>> heads = new EnumMap<>(TraderBiome.class);


    public VillagerHeadStorage(FileConfiguration fileConfiguration) {

        ConfigurationSection section = fileConfiguration.getConfigurationSection("villagerHeads");
        if (section == null) {
            throw new IllegalStateException("Villager heads section is missing in the config");
        }

        for (String biome : section.getKeys(false)) {
            ConfigurationSection biomeSection = section.getConfigurationSection(biome);
            if (biomeSection != null) {
                Map<TraderWork, String> professionHeads = new EnumMap<>(TraderWork.class);
                for (String profession : biomeSection.getKeys(false)) {
                    String texture = biomeSection.getString(profession);
                    if (texture != null) {
                        professionHeads.put(TraderWork.valueOf(profession), texture);
                    }
                }
                heads.put(TraderBiome.valueOf(biome), professionHeads);
            }
        }
    }

    public String getURL(TraderBiome biome, TraderWork work) {
        return heads.get(biome).get(work);
    }

}
