package io.github.leralix.exotictrades.guis;

import dev.triumphteam.gui.guis.Gui;
import io.github.leralix.exotictrades.storage.StorageForGui;
import org.bukkit.entity.Player;

public abstract class BasicGui {

    protected final Gui gui;
    protected final Player player;
    protected final StorageForGui storage;

    protected BasicGui(Player player, String title, int rows, StorageForGui storage){
        gui = GuiUtil.createChestGui(title, rows);
        this.player = player;
        this.storage = storage;
    }

    public void open(){
        gui.open(player);
    }
}
