package io.github.leralix.exotictrades.guis;

import dev.triumphteam.gui.guis.GuiItem;
import io.github.leralix.exotictrades.storage.StorageForGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;

public abstract class IteratorGUI extends BasicGui {

    protected int page;

    protected IteratorGUI(Player player, String title, int rows, StorageForGui storage) {
        super(player, title, rows, storage);
        this.page = 0;
    }


    protected void iterator(List<GuiItem> itemList, Consumer<Player> onLeave) {
        iterator(itemList, onLeave, Material.GRAY_STAINED_GLASS_PANE);
    }

    protected void iterator(List<GuiItem> itemList, Consumer<Player> onLeave, Material decorativeMaterial) {
        GuiUtil.createIterator(gui, itemList, page, player, onLeave, p -> nextPage(), p -> previousPage(), decorativeMaterial);
    }

    protected void previousPage() {
        page--;
        open();
    }

    protected void nextPage() {
        page++;
        open();
    }
}
