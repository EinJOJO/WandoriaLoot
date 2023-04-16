package me.einjojo.wandorialoot.gui.table;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.*;
import com.github.stefvanschie.inventoryframework.pane.util.Mask;
import com.github.stefvanschie.inventoryframework.pane.util.Pattern;
import me.einjojo.wandorialoot.loot.LootTable;
import me.einjojo.wandorialoot.util.Heads;
import me.einjojo.wandorialoot.util.ItemHelper;
import org.bukkit.Material;

import java.util.List;


public class LootTablesGui extends ChestGui {

    private PaginatedPane paginatedPane = new PaginatedPane(0,1,9,4);
    private GuiItem leftButton;
    private GuiItem rightButton;
    private LootTable[] lootTables;
    public LootTablesGui(List<LootTable> lootTableList) {
        this(lootTableList.toArray(new LootTable[0]));
    }
    public LootTablesGui(LootTable[] lootTable) {
        super(5, "Loot Tables");
        setOnTopClick(e -> e.setCancelled(true));
        addPane(createBackgroundPane());
        paginatedPane.setPriority(Pane.Priority.HIGH);
        addPane(paginatedPane);
        addPane(navigatorPane());
        setPage(0);
        update();
    }

    public void setPage(int page) {
        leftButton.setVisible(page != 0);
        rightButton.setVisible(page != paginatedPane.getPages() - 1);
        if (page < 0 || page >= paginatedPane.getPages()) {
            return;
        }
        paginatedPane.setPage(page);
        update();
    }


    private Pane navigatorPane() {
        OutlinePane pane = new OutlinePane(0,0, 3, 1);
        pane.setGap(1);
        leftButton = new GuiItem(new ItemHelper(Heads.WHITE_ARROW_LEFT.getSkull()).setDisplayName("§6Previous Page"));
        leftButton.setAction(event -> {
            setPage(paginatedPane.getPage() - 1);
        });
        pane.addItem(leftButton);

        rightButton = new GuiItem(new ItemHelper(Heads.WHITE_ARROW_RIGHT.getSkull()).setDisplayName("§6Next Page"));
        rightButton.setAction(event -> {
            setPage(paginatedPane.getPage() + 1);
        });
        pane.addItem(rightButton);

        pane.setPriority(Pane.Priority.HIGH);
        return pane;
    }

    private Pane createBackgroundPane()
    {
        MasonryPane pane = new MasonryPane(0, 0, 9, 5);
        OutlinePane bg = new OutlinePane(0, 0, 9, 5);
        bg.addItem(new GuiItem(new ItemHelper(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§0")));
        bg.setRepeat(true);
        pane.addPane(bg);

        OutlinePane whiteTop = new OutlinePane(0, 0, 9, 1);
        OutlinePane whiteBottom = new OutlinePane(5, 0, 9, 1);
        whiteTop.addItem(new GuiItem(new ItemHelper(Material.WHITE_STAINED_GLASS_PANE).setDisplayName("§0")));
        whiteBottom.addItem(new GuiItem(new ItemHelper(Material.WHITE_STAINED_GLASS_PANE).setDisplayName("§0")));
        whiteTop.setRepeat(true);
        whiteBottom.setRepeat(true);
        pane.addPane(whiteTop);
        pane.addPane(whiteBottom);
        pane.setPriority(Pane.Priority.LOWEST);
        return pane;
    }

    protected Pane contentPane() {
        PaginatedPane pane = new PaginatedPane(0, 1, 9, 4);
        pane.setPriority(Pane.Priority.HIGHEST);
        return pane;
    }



}
