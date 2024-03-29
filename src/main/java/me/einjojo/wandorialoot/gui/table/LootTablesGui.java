package me.einjojo.wandorialoot.gui.table;

import com.github.stefvanschie.inventoryframework.font.util.Font;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.*;
import com.github.stefvanschie.inventoryframework.pane.util.Pattern;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import me.einjojo.joslibrary.util.ItemBuilder;
import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.loot.LootTable;
import me.einjojo.wandorialoot.util.ItemHelper;
import me.einjojo.wandorialoot.util.Sounds;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


public class LootTablesGui extends ChestGui {

    private final PaginatedPane paginatedPane = new PaginatedPane(0, 1, 9, 4);
    private GuiItem leftButton;
    private GuiItem rightButton;
    private final LootTable[] lootTables;


    public LootTablesGui() {
        this(WandoriaLoot.getInstance().getLootManager().getLootTables());
    }

    public LootTablesGui(List<LootTable> lootTableList) {
        this(lootTableList.toArray(new LootTable[0]));
    }


    public LootTablesGui(LootTable[] lootTable) {
        super(6, "Loot Tables");
        this.lootTables = lootTable;
        setOnTopClick(e -> e.setCancelled(true));
        addPane(createBackgroundPane());
        paginatedPane.setPriority(Pane.Priority.HIGH);
        addPane(paginatedPane);
        populatePaginatedPane();
        addPane(navigatorPane());
        setPage(0);
        update();
    }

    public void setPage(int page) {
        try {
            paginatedPane.setPage(page);
        } catch (IndexOutOfBoundsException ignored) {
        }
        leftButton.setVisible(page != 0);
        rightButton.setVisible(page != paginatedPane.getPages() - 1);
        update();
    }


    private Pane navigatorPane() {
        StaticPane pane = new StaticPane(3, 5, 3, 1);

        leftButton = new GuiItem(new ItemHelper(Font.WHITE.toItem('←')).setDisplayName("§6Previous Page"));
        leftButton.setAction(event -> {
            setPage(paginatedPane.getPage() - 1);
            Sounds.GUI_PAGE_SWITCH.play(event.getWhoClicked());
        });
        pane.addItem(leftButton, Slot.fromIndex(0));

        rightButton = new GuiItem(new ItemHelper(Font.WHITE.toItem('→')).setDisplayName("§6Next Page"));
        rightButton.setAction(event -> {
            setPage(paginatedPane.getPage() + 1);
            Sounds.GUI_PAGE_SWITCH.play(event.getWhoClicked());
        });
        pane.addItem(rightButton, Slot.fromIndex(2));

        pane.setPriority(Pane.Priority.HIGH);
        return pane;
    }

    private Pane createBackgroundPane() {
        Pattern pattern = new Pattern("111111111", "000000000", "000000000", "000000000", "000000000", "111111111");
        PatternPane patternPane = new PatternPane(9, 6, pattern);
        patternPane.bindItem('0', new GuiItem(new ItemHelper(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§0")));
        patternPane.bindItem('1', new GuiItem(new ItemHelper(Material.WHITE_STAINED_GLASS_PANE).setDisplayName("§0")));
        patternPane.setPriority(Pane.Priority.LOWEST);
        return patternPane;
    }

    @Nullable
    public LootTable getLootChest(ItemStack itemStack) {
        //get the table from the item
        if (itemStack == null) return null;
        String sUid = (String) new ItemBuilder(itemStack).getPersistentData("loottable", PersistentDataType.STRING);
        if (sUid == null) return null;
        UUID uuid = UUID.fromString(sUid);
        LootTable table1 = WandoriaLoot.getInstance().getLootManager().getLootTable(uuid);
        return table1;
    }

    public void onChestSelect(InventoryClickEvent e) {
        LootTable table = getLootChest(e.getCurrentItem());
        if (table == null) return;
        new LootTableGui(table).show(e.getWhoClicked());

    }

    protected void populatePaginatedPane() {
        paginatedPane.clear();
        OutlinePane pane = null;
        ArrayList<LootTable> arrayList = new ArrayList<>(List.of(lootTables));
        Collections.sort(arrayList);
        for (int i = 0; i < arrayList.size(); i++) {
            if ((i % (9 * 4 + 1)) == 0) {
                pane = new OutlinePane(0, 0, 9, 4);
                pane.setPriority(Pane.Priority.HIGHEST);
                paginatedPane.addPane(paginatedPane.getPages(), pane);
            }

            LootTable table = arrayList.get(i);
            if (table == null) continue;
            GuiItem item = new GuiItem(ItemHelper.lootTableItem(table).build(), this::onChestSelect);

            pane.addItem(item);

        }
    }
}
