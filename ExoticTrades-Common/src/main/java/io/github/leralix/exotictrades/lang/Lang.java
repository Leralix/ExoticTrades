package io.github.leralix.exotictrades.lang;

import io.github.leralix.exotictrades.ExoticTrades;
import io.github.leralix.exotictrades.storage.EconomyManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.leralix.lib.utils.config.ConfigUtil;

import java.io.File;
import java.util.EnumMap;

public enum Lang {

    EXOTIC_TRADE_STRING,
    LANGUAGE_SUCCESSFULLY_LOADED,
    TRADER_BASE_NAME,
    SYNTAX_ERROR,
    COMMAND_WORLD_NOT_FOUND,
    COMMAND_GENERIC_SUCCESS,
    NEW_TIME_BETWEEN_POSITION_SET,
    NUMBER_OF_ITEM_SOLD_PER_DAY_SET,
    ADMIN_SPAWN_RARE_ITEM,
    ITEM_NOT_FOUND,
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
    SOLD_MARKET_ITEM_SUCCESS,
    CLICK_TO_OPEN,
    CLICK_TO_DELETE,
    RIGHT_CLICK_TO_DELETE,
    TRADER_MENU,
    MARKET_MENU,
    NEW_TRADER,
    CLICK_TO_CREATE,
    LEFT_CLICK_TO_MANAGE,
    LEFT_CLICK_TO_TELEPORT,
    RIGHT_CLICK_TO_TELEPORT,
    CURRENT_BIOME,
    CURRENT_PROFESSION,
    DELETE_TRADER,
    MANAGE_POSITION,
    MANAGE_ITEM_TO_SELL,
    SELECT_ITEM_TO_SELL,
    CURRENT_STATE_ENABLE,
    CURRENT_STATE_DISABLE,
    CLICK_TO_SWAP,
    WRITE_POSITION_OF_TRADER_OR_HERE,
    NEW_TRADER_POSITION_REGISTERED,
    TRADER_FIXED_POSITION,
    TRADER_RANDOM_POSITION,
    TRADER_CAN_SPAWN_IN_X_LOCATIONS,
    NUMBER_OF_DAYS_BEFORE_NEXT_POSITION,
    CLICK_TO_MANAGE,
    CLICK_TO_MODIFY,
    CURRENT_NAME,
    CURRENT_PRICE,
    TARGET_PRICE,
    MIN_PRICE,
    MAX_PRICE,
    CURRENT_SELLS,
    CURRENT_BUYS,
    ADD_NEW_SELLABLE_ITEM,
    DRAG_AND_DROP,
    CLICK_TO_INTERACT,
    SET_NUMBER_OF_ITEMS_TO_SELL,
    SET_NUMBER_OF_ITEMS_TO_SELL_DESC,
    PRICE,
    CLICK_TO_BUY,
    TRANSACTION_SUCCESS,
    EXPECTED_NEXT_PRICE,
    BUY_ITEM_MENU,
    MARKET_INFO,
    WRITE_CANCEL_TO_CANCEL,
    ADD_POSITION,
    CLICK_TO_ADD_POSITION,
    CANCEL_WORD,
    CANCELLED_ACTION,
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
    NOT_ENOUGH_MONEY;


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

        ConfigUtil.saveAndUpdateResource(ExoticTrades.getPlugin(), "lang/" + fileTag + "/main.yml");

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

        if (translation == null)
            return this.name();

        translation = ChatColor.translateAlternateColorCodes('§', translation);
        for (int i = 0; i < placeholders.length; i++) {
            String val = placeholders[i] == null ? "null" : placeholders[i].toString();
            translation = translation.replace("{" + i + "}",val);
        }
        if(translation.contains("{MONEY_CHAR}")) {
            String moneyChar = EconomyManager.getCurrencySymbol();
            if(moneyChar == null) {
                moneyChar = "$";
            }
            translation = translation.replace("{MONEY_CHAR}", moneyChar);
        }
        return translation;
    }



}