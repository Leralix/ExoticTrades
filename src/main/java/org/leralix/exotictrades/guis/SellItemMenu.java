package org.leralix.exotictrades.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.leralix.exotictrades.ExoticTrades;
import org.leralix.exotictrades.item.MarketItem;
import org.leralix.exotictrades.item.MarketItemStack;
import org.leralix.exotictrades.lang.Lang;
import org.leralix.exotictrades.market.StockMarketManager;
import org.leralix.exotictrades.storage.EconomyManager;
import org.leralix.exotictrades.storage.MarketItemKey;
import org.leralix.exotictrades.storage.MarketItemStorage;
import org.leralix.exotictrades.traders.Trader;
import org.leralix.lib.utils.HeadUtils;

import java.util.*;

public class SellItemMenu extends BasicGui {

    private final List<ItemStack> allItems = new ArrayList<>();
    private final Map<Integer, Integer> rareItems = new HashMap<>();
    private final Trader trader;

    public SellItemMenu(Player player, Trader trader) {
        super(player, "Trader", 4);
        this.trader = trader;
        setupGui();

        ItemStack pane = HeadUtils.createCustomItemStack(Material.GRAY_STAINED_GLASS_PANE,"");
        GuiItem filler = ItemBuilder.from(pane).asGuiItem();
        gui.getFiller().fill(filler);

        gui.getFiller().fillBetweenPoints(2,2,3,4, ItemBuilder.from(Material.AIR).asGuiItem());

        GuiAction<InventoryDragEvent> actionDrag = event -> {
            event.setCancelled(false);
            Bukkit.getScheduler().runTask(ExoticTrades.getPlugin(), this::updateItems);
        };

        GuiAction<InventoryClickEvent> action = event -> {
            event.setCancelled(false);
            Bukkit.getScheduler().runTask(ExoticTrades.getPlugin(), this::updateItems);
        };
        gui.setDragAction(actionDrag);
        gui.addSlotAction(2,2, action);
        gui.addSlotAction(2,3, action);
        gui.addSlotAction(2,4, action);
        gui.addSlotAction(3,2, action);
        gui.addSlotAction(3,3, action);
        gui.addSlotAction(3,4, action);

        GuiItem sellButton = getConfirmButton();
        gui.setItem(2,6, sellButton);

        gui.setItem(2,8, getMarketInfoButton(trader));

        ItemStack traderItem = HeadUtils.makeSkullURL(Lang.BUY_ITEM_MENU.get(), "https://textures.minecraft.net/texture/cc1b2f592cfc8d372dcf5fd44eed69dddc64601d7846d72619f70511d8043a89",
                Lang.CLICK_TO_OPEN.get());

        GuiItem traderGuiItem = ItemBuilder.from(traderItem).asGuiItem(event -> new BuyItemMenu(player, trader, 0).open());
        gui.setItem(3,8, traderGuiItem);


        gui.setItem(4,1, GuiUtil.createBackArrow(player, event -> player.closeInventory()));
    }

    private GuiItem getMarketInfoButton(Trader trader) {
        ItemStack marketInfo = HeadUtils.makeSkullURL(Lang.MARKET_INFO.get(), "https://textures.minecraft.net/texture/ab69967163c743ddb1f083566757576b9e63ac380cc150f518b33dc4e91ef712",
                Lang.CLICK_TO_OPEN.get());
        return ItemBuilder.from(marketInfo).asGuiItem(event -> {
            new MarketInfoMenu(player, trader, 0).open();
            event.setCancelled(true);
        });
    }

    private void setupGui() {
        gui.setDefaultTopClickAction(event -> event.setCancelled(true));
        gui.setDefaultClickAction(event -> {
            if(event.isShiftClick())
                event.setCancelled(true);}
        );

        gui.setCloseGuiAction(closeEvent -> {
            for(ItemStack item : allItems){
                player.getInventory().addItem(item);
            }
        });
    }

    private void updateItems() {
        rareItems.clear();
        allItems.clear();
        for(int i = 1; i < 3; i++){
            for(int j = 1; j < 4; j++){
                ItemStack item = gui.getInventory().getItem(i * 9 + j);

                if(item == null || item.getType() == Material.AIR){
                    continue;
                }
                allItems.add(item);
                MarketItemKey key = MarketItemKey.of(item);
                MarketItem marketItem = MarketItemStorage.getMarketItem(key);
                if(marketItem == null){
                    continue;
                }
                rareItems.put(marketItem.getId(), rareItems.getOrDefault(marketItem.getId(),0) + item.getAmount());
            }
        }
        GuiItem sellButton = getConfirmButton();
        gui.updateItem(2,6, sellButton);
    }

    private GuiItem getConfirmButton(){
        GuiItem confirmButton;

        boolean allItemsAreNotRare = false;
        for(ItemStack item : allItems){
            MarketItemKey key = MarketItemKey.of(item);
            MarketItem marketItem = MarketItemStorage.getMarketItem(key);
            if(marketItem == null || !trader.canTradeMarketItem(marketItem)){
                allItemsAreNotRare = true;
                break;
            }
        }

        if(rareItems.isEmpty() || allItemsAreNotRare){
            ItemStack confirm = HeadUtils.makeSkullURL(Lang.CONFIRM_BUTTON.get(), "https://textures.minecraft.net/texture/27548362a24c0fa8453e4d93e68c5969ddbde57bf6666c0319c1ed1e84d89065",
                    Lang.NO_ITEM_OR_WRONG.get());
            confirmButton = ItemBuilder.from(confirm).asGuiItem(event -> event.setCancelled(true));
        }
        else {
            List<String> description = new ArrayList<>();
            getAllMarketItem().forEach(item -> description.add(item.getDescription()));

            ItemStack confirm = HeadUtils.makeSkullURL(Lang.CONFIRM_BUTTON.get(), "https://textures.minecraft.net/texture/a79a5c95ee17abfef45c8dc224189964944d560f19a44f19f8a46aef3fee4756",
                    description);
            confirmButton = ItemBuilder.from(confirm).asGuiItem(event -> {
                double total = StockMarketManager.sellMarketItems(getAllMarketItem());

                player.sendMessage(Lang.SOLD_MARKET_ITEM_SUCCESS.get(total));
                EconomyManager.getEconomy().depositPlayer(player, total);
                gui.setCloseGuiAction(null);
                event.setCancelled(true);
                player.closeInventory();
            });
        }
        return confirmButton;
    }

    private List<MarketItemStack> getAllMarketItem(){
        List<MarketItemStack> displayMarketItems = new ArrayList<>();
        for(Map.Entry<Integer, Integer> entry : rareItems.entrySet()){
            MarketItemStack marketItem = new MarketItemStack(MarketItemStorage.getMarketItem(entry.getKey()), entry.getValue());
            displayMarketItems.add(marketItem);
        }
        return displayMarketItems;
    }
}
