package org.leralix.exotictrades.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.leralix.exotictrades.lang.Lang;
import org.leralix.exotictrades.listener.chat.PlayerChatListenerStorage;
import org.leralix.exotictrades.listener.chat.events.RenameTraderChatListener;
import org.leralix.exotictrades.traders.Trader;
import org.leralix.lib.utils.HeadUtils;

public class ManageTrader extends basicGUI {

    private final Trader trader;

    public ManageTrader(Player player, Trader trader) {
        super(player, "Manage Trader", 3);
        gui.setDefaultClickAction(event -> event.setCancelled(true));
        this.trader = trader;

        ItemStack villagerItem = trader.getIcon();
        GuiItem villagerGuiItem = ItemBuilder.from(villagerItem).asGuiItem();


        ItemStack biomeItem = trader.getBiomeType().getIcon(Lang.CURRENT_BIOME.get(trader.getBiomeType().getName()), Lang.CLICK_TO_MANAGE);
        GuiItem biomeGuiItem = ItemBuilder.from(biomeItem).asGuiItem(event -> new SelectTraderBiomeMenu(player, trader).open());

        ItemStack workItem = trader.getWorkType().getIcon(Lang.CURRENT_PROFESSION.get(trader.getWorkType().getName()), Lang.CLICK_TO_MANAGE);
        GuiItem workGuiItem = ItemBuilder.from(workItem).asGuiItem(event -> new SelectTraderProfessionMenu(player, trader).open());

        ItemStack renameTraderItem = HeadUtils.createCustomItemStack(Material.NAME_TAG, Lang.CURRENT_NAME.get(trader.getName()), Lang.CLICK_TO_SELECT.get());
        GuiItem renameTraderGuiItem = ItemBuilder.from(renameTraderItem).asGuiItem(event -> {
            player.closeInventory();
            PlayerChatListenerStorage.register(player, new RenameTraderChatListener(trader));
        });

        ItemStack deleteTraderItem = HeadUtils.createCustomItemStack(Material.BARRIER, Lang.DELETE_TRADER.get(), Lang.CLICK_TO_DELETE.get());
        GuiItem deleteTraderGuiItem = ItemBuilder.from(deleteTraderItem).asGuiItem(event -> {
            trader.delete();
            new ManageTraders(player).open();
        });

        String positionInfo = getSpawnZoneInfo();
        ItemStack managePosition = HeadUtils.createCustomItemStack(Material.FILLED_MAP, Lang.MANAGE_POSITION.get(),
                positionInfo,
                Lang.CLICK_TO_MANAGE.get()
        );
        GuiItem managePositionGuiItem = ItemBuilder.from(managePosition).asGuiItem(event -> new ManageTraderPosition(player, trader).open());

        ItemStack selectedMarketItems = HeadUtils.createCustomItemStack(Material.CHEST, Lang.SELECT_ITEM_TO_SELL.get(),
                Lang.LEFT_CLICK_TO_MANAGE.get());
        GuiItem selectMarketItemsButton = ItemBuilder.from(selectedMarketItems).asGuiItem(event -> new ManageTraderAuthorized(player, trader, 0).open());


        gui.setItem(1, 5, villagerGuiItem);

        gui.setItem(2, 2, biomeGuiItem);
        gui.setItem(2, 3, workGuiItem);
        gui.setItem(2, 4, renameTraderGuiItem);

        gui.setItem(2, 6, managePositionGuiItem);
        gui.setItem(2, 7, selectMarketItemsButton);
        gui.setItem(2,8, deleteTraderGuiItem);



        gui.setItem(3, 1, GuiUtil.createBackArrow(player, event -> new ManageTraders(player).open()));
    }

    private String getSpawnZoneInfo() {
        return trader.getPosition().getSpawnInfo();

    }

}
