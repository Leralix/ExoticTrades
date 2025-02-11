package org.leralix.exotictrades.traders.position;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.leralix.exotictrades.guis.GuiUtil;
import org.leralix.exotictrades.guis.ManageTrader;
import org.leralix.exotictrades.guis.ManageTraderPosition;
import org.leralix.exotictrades.lang.Lang;
import org.leralix.exotictrades.listener.chat.PlayerChatListenerStorage;
import org.leralix.exotictrades.listener.chat.RegisterNewTraderPosition;
import org.leralix.exotictrades.traders.Trader;
import org.leralix.lib.position.Vector3D;
import org.leralix.lib.utils.HeadUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FixedPosition implements TraderPosition {

    private final List<Vector3D> positions;
    private int currentPosition = 0;

    public FixedPosition(Vector3D position) {
        this.positions = new ArrayList<>();
        this.positions.add(position);
    }


    @Override
    public Vector3D getNextPosition() {
        if(currentPosition >= positions.size()){
            currentPosition = 0;
        }
        Vector3D position = positions.get(currentPosition);
        currentPosition++;
        return position;
    }

    @Override
    public String getSpawnInfo() {
        return Lang.TRADER_CAN_SPAWN_IN_X_LOCATIONS.get(positions.size());
    }

    @Override
    public void addGuiItems(Gui gui, ManageTraderPosition manageTraderPosition, Player player, Trader trader, int page) {

        List<GuiItem> guiItems = new ArrayList<>();
        for(Vector3D position : positions){
            GuiItem positionManager = createPositionGui(position, player, manageTraderPosition);
            guiItems.add(positionManager);
        }

        GuiUtil.createIterator(gui,guiItems, page, player,
                p -> new ManageTrader(player, trader).open(),
                p -> new ManageTraderPosition(player, trader, page + 1).open(),
                p -> new ManageTraderPosition(player, trader, page - 1).open());

        ItemStack addPosition = HeadUtils.makeSkullB64(Lang.ADD_POSITION.get(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTc5YTVjOTVlZTE3YWJmZWY0NWM4ZGMyMjQxODk5NjQ5NDRkNTYwZjE5YTQ0ZjE5ZjhhNDZhZWYzZmVlNDc1NiJ9fX0=",
                Lang.CLICK_TO_ADD_POSITION.get());

        GuiItem addPositionButton = ItemBuilder.from(addPosition).asGuiItem(event -> {
            PlayerChatListenerStorage.register(player, new RegisterNewTraderPosition(player, this));
        });

        gui.setItem(gui.getRows(), 4, addPositionButton);

    }

    private GuiItem createPositionGui(Vector3D position, Player player, ManageTraderPosition manageTraderPosition) {
        Material blockType = position.getWorld().getHighestBlockAt(position.getX(), position.getZ()).getType();
        String worldName = position.getWorld().getName();
        ItemStack positionItem = HeadUtils.createCustomItemStack(blockType, Lang.TRADER_FIXED_POSITION.get(
                position.getX(),
                position.getY(),
                position.getZ(),
                worldName));
        return ItemBuilder.from(positionItem).asGuiItem(event -> {
            if(event.isLeftClick()){
                player.teleport(position.getLocation());
            }
            if(event.isRightClick()){
                positions.remove(position);
                manageTraderPosition.reload();
            }
        });

    }

    public void addPosition(Vector3D vector3D) {
        positions.add(vector3D);
    }
}
