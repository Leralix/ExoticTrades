package org.leralix.exotictrades.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.leralix.exotictrades.lang.Lang;
import org.leralix.exotictrades.traders.Trader;
import org.leralix.lib.utils.HeadUtils;

import java.util.EnumMap;
import java.util.Map;

public class OpenTraderMenu extends GUImenu{

    private final Map<Material, Integer> items = new EnumMap<>(Material.class);

    public OpenTraderMenu(Player player, Trader trader) {
        super(player, "Trader", 4);


        gui.setDefaultTopClickAction(event -> event.setCancelled(true));
        gui.setDefaultClickAction(event -> {
            if(event.isShiftClick())
                event.setCancelled(true);}
        );
        ItemStack pane = HeadUtils.createCustomItemStack(Material.GRAY_STAINED_GLASS_PANE,"");
        GuiItem filler = ItemBuilder.from(pane).asGuiItem();
        gui.getFiller().fill(filler);

        gui.getFiller().fillBetweenPoints(2,2,3,4, ItemBuilder.from(Material.AIR).asGuiItem());


        GuiAction<InventoryClickEvent> action = event -> {
            updateItems(event);
        };
        gui.addSlotAction(2,2, action);
        gui.addSlotAction(2,3, action);
        gui.addSlotAction(2,4, action);
        gui.addSlotAction(3,2, action);
        gui.addSlotAction(3,3, action);
        gui.addSlotAction(3,4, action);

        ItemStack sell = HeadUtils.makeSkullURL(Lang.SELL_BUTTON.get(), "https://textures.minecraft.net/texture/a79a5c95ee17abfef45c8dc224189964944d560f19a44f19f8a46aef3fee4756");
        GuiItem sellButton = ItemBuilder.from(sell).asGuiItem(event -> {
            event.setCancelled(true);
        });

        gui.setItem(2,6, sellButton);

        gui.setItem(4,1, IGUI.createBackArrow(player, event -> player.closeInventory()));
    }

    private void updateItems(InventoryClickEvent event) {
        event.setCancelled(false);
        player.sendMessage("You clicked the slot : " + event.getSlot());

        ItemStack currentItem = event.getCurrentItem();
        if(currentItem != null && items.containsKey(currentItem.getType())) {
            int amount = items.get(currentItem.getType());
            if(amount - currentItem.getAmount() <= 0) {
                items.remove(currentItem.getType());
            }
            else{
                items.put(currentItem.getType(), amount - currentItem.getAmount());
            }
        }

        ItemStack cursor = event.getCursor();
        if(items.containsKey(cursor.getType())) {
            int amount = items.get(cursor.getType());
            items.put(cursor.getType(), amount + cursor.getAmount());
        }
        else {
            items.put(cursor.getType(), cursor.getAmount());
        }

        gui.setItem(event.getSlot(), ItemBuilder.from(cursor).asGuiItem());
        items.clear();

        for(int i = 1; i < 3; i++){
            for(int j = 1; j < 4; j++){
                GuiItem guiItem = gui.getGuiItem(i * 9 + j);
                System.out.println(guiItem.getItemStack());
            }
        }
        
        gui.setCloseGuiAction(closeEvent -> {
            for(Map.Entry<Material, Integer> entry : items.entrySet()){
                player.getInventory().addItem(new ItemStack(entry.getKey(), entry.getValue()));
            }
        });
        open();
    }
}
