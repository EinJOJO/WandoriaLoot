package me.einjojo.wandorialoot.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.chest.LootChest;
import me.einjojo.wandorialoot.gui.table.LootTablesSelectorGui;
import me.einjojo.wandorialoot.loot.LootTable;
import me.einjojo.wandorialoot.util.Heads;
;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LootChestConfiguratorGui extends ChestGui {


    private final PaginatedPane itemsPane = new PaginatedPane(0, 0, 9, 4);
    private final LootChest lootChest;
    private final WandoriaLoot plugin;

    public LootChestConfiguratorGui(LootChest lootChest) {
        super(5, "Lootchest Einstellen");
        this.plugin = WandoriaLoot.getInstance();
        this.lootChest = lootChest;
        setOnTopClick(e -> e.setCancelled(true));
        addPane(backgroundPane());
        addPane(createSelectionPane());
        update();
    }

    private Pane backgroundPane()
    {
        OutlinePane pane = new OutlinePane(0, 0, 9, 5);
        pane.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        pane.setRepeat(true);
        pane.setPriority(Pane.Priority.LOWEST);
        return pane;
    }

    private Pane createSelectionPane()
    {
        OutlinePane pane = new OutlinePane(2, 2, 5, 1);
        pane.setGap(1);

        pane.addItem(getLootChestInfoItem());
        pane.addItem(getLootTableSelectorItem());

        return pane;
    }

    private GuiItem getLootTableSelectorItem() {
        ItemStack selector = Heads.WHITE_I.getSkull();
        UUID tableUUID = lootChest.getLootTableUUID();
        LootTable table = tableUUID == null ? null : plugin.getLootManager().getLootTable(tableUUID);
        ItemMeta selectorMeta = selector.getItemMeta();
        if (selectorMeta == null) {
            selectorMeta = Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
        };
        ArrayList<String> arrayList = new ArrayList();
        arrayList.add("§7Verwalte die Lootchests");
        arrayList.add("§6Linksklick §ezum Setzen");
        if (table != null) {
            arrayList.add("§eAktueller LootTable: §6" + table.getName());
            arrayList.add("§4Rechtsklick §czum Entfernen");
        }
        selectorMeta.setLore(arrayList);

        selector.setItemMeta(selectorMeta);
        GuiItem item =  new GuiItem(selector);
        item.setAction(e -> {
            if (e.isRightClick()) {

                //Entfernen des LootTables
                lootChest.setLootTable(null);
                e.getWhoClicked().sendMessage("§aLootTable entfernt");
            } else if (e.isLeftClick()) {

            }
        });
        return item;
    }

    private GuiItem getLootChestInfoItem() {
        ItemStack info = Heads.WHITE_I.getSkull();
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setLore(List.of(
                String.format("§eGeöffnet von §6%d §eSpielern", 0), //TODO: Get player count
                String.format("§eAktueller Modus: %s", "§cLootTable")
        ));
        info.setItemMeta(infoMeta);
        return new GuiItem(info);
    }
}
