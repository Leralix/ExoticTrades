package org.leralix.exotictrades.lang;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.leralix.exotictrades.ExoticTrades;
import org.leralix.lib.utils.config.ConfigTag;
import org.leralix.lib.utils.config.ConfigUtil;

import java.io.File;
import java.util.EnumMap;

public enum Lang {

    EXOTIC_TRADE_STRING,
    LANGUAGE_SUCCESSFULLY_LOADED,
    SYNTAX_ERROR,
    COMMAND_GENERIC_SUCCESS,
    ADMIN_SPAWN_RARE_ITEM,
    ITEM_RARE_STONE,
    ITEM_RARE_WOOD,
    ITEM_RARE_CROP,
    ITEM_RARE_SOUL,
    ITEM_RARE_FISH,
    RARE_ITEM_DESC_1,
    GUI_PREVIOUS_PAGE,
    GUI_NEXT_PAGE,
    GUI_BACK_ARROW,
    CLICK_TO_SELECT,
    SELL_BUTTON,
    CONFIRM_BUTTON,
    NO_ITEM_OR_WRONG,
    QUANTITY_ITEM_TO_SELL,
    NEW_TRADER,
    CLICK_TO_CREATE,
    LEFT_CLICK_TO_MANAGE,
    RIGHT_CLICK_TO_TELEPORT,
    CURRENT_BIOME,
    CURRENT_PROFESSION,
    PLAINS,
    DESERT,
    SAVANNA,
    SNOW,
    TAIGA,
    JUNGLE,
    SWAMP,
    NONE,
    FARMER,
    LIBRARIAN,
    BUTCHER,
    NITWIT,
    ARMORER,
    CARTOGRAPHER,
    CLERIC,
    FISHERMAN,
    FLETCHER,
    LEATHERWORKER,
    MASON,
    SHEPHERD,
    TOOLSMITH,
    WEAPONSMITH,

    ;


    private static final EnumMap<Lang, String> translations = new EnumMap<>(Lang.class);

    public static void loadTranslations(String fileTag) {

        File langFolder = new File(ExoticTrades.getPlugin().getDataFolder(), "lang");

        if (!langFolder.exists()) {
            langFolder.mkdir();
        }

        File specificLangFolder = new File(langFolder, fileTag);
        if(!specificLangFolder.exists()) {
            specificLangFolder.mkdir();
        }

        File file = new File(specificLangFolder, "main.yml");

        boolean replace = ConfigUtil.getCustomConfig(ConfigTag.LANG).getBoolean("autoUpdateLangFiles",true);


        if(!file.exists() || replace) {
            ExoticTrades.getPlugin().saveResource("lang/" + fileTag + "/main.yml", true);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (Lang key : Lang.values()) {
            String message = config.getString("language." + key.name());
            if (message != null) {
                translations.put(key, message);
            }
        }
    }



    public String get() {
        String translation = translations.get(this);
        if (translation != null) {
            return ChatColor.translateAlternateColorCodes('§', translation);
        }
        return this.name();
    }

    public String get(Object... placeholders) {
        String translation = translations.get(this);
        if (translation != null) {
            translation = ChatColor.translateAlternateColorCodes('§', translation);
            for (int i = 0; i < placeholders.length; i++) {
                String val = placeholders[i] == null ? "null" : placeholders[i].toString();
                translation = translation.replace("{" + i + "}",val);
            }
            return translation;
        }
        return this.name();
    }



}