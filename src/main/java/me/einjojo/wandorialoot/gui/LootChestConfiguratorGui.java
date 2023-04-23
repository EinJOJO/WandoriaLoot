package me.einjojo.wandorialoot.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import me.einjojo.joslibrary.util.ItemBuilder;
import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.chest.LootChest;
import me.einjojo.wandorialoot.gui.table.LootTablesGui;
import me.einjojo.wandorialoot.gui.table.LootTablesSelectorGui;
import me.einjojo.wandorialoot.loot.LootTable;
import me.einjojo.wandorialoot.util.Heads;
;
import me.einjojo.wandorialoot.util.ItemHelper;
import me.einjojo.wandorialoot.util.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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

        pane.addItem(getLootTableSelectorItem());
        pane.addItem(getLootChestInfoItem());
        pane.addItem(getTestGenerationItem());


        return pane;
    }

    private GuiItem getTestGenerationItem() {
        ItemStack stack = new ItemBuilder(Material.BLAZE_ROD).setName("§eTestgenerierung").setLore(List.of("§7Generiere eine Testkiste")).build();
        return new GuiItem(stack, (e -> {
            Inventory inventory1 = lootChest.getInventory();
            new TestGeneration(this, inventory1.getContents()).show(e.getWhoClicked());
        }));
    }

    private GuiItem getLootTableSelectorItem() {
        LootTable table = WandoriaLoot.getInstance().getLootManager().getLootTable(lootChest.getLootTableUUID());
        ArrayList<String> arrayList = new ArrayList();
        arrayList.add("§7Verwalte die Lootchests");
        arrayList.add("§6Linksklick §ezum Setzen");

        ItemBuilder selector;
        if (table != null) {
             selector = ItemHelper.lootTableItem(table);
            arrayList.add("§eAktueller LootTable: §6" + table.getName());
            arrayList.add("§4Rechtsklick §czum Entfernen");
        } else {
            selector = new ItemBuilder(Material.BARRIER);
        }
        selector.setName("§eLootTable auswählen");
        selector.setLore(arrayList);
        final GuiItem item =  new GuiItem(selector.build());
        item.setAction(e -> {
            if (e.isRightClick()) {
                //Entfernen des LootTables
                lootChest.setLootTable(null);
                item.setItem(getLootTableSelectorItem().getItem());
                update();
                e.getWhoClicked().sendMessage("§aLootTable entfernt");
            } else if (e.isLeftClick()) {
                final HumanEntity player = e.getWhoClicked();
                LootTablesSelectorGui gui = new LootTablesSelectorGui(lootTable -> {
                    if (lootTable == null) {
                        Sounds.CANCEL.play(player);
                        update();
                        show(player);
                        return;
                    }
                    lootChest.setLootTable(lootTable);
                    player.sendMessage("§aLootTable gesetzt");
                    item.setItem(getLootTableSelectorItem().getItem());
                    Sounds.GUI_CLICK.play(player);
                });
                gui.show(player);
            }
        });
        return item;
    }

    private GuiItem getLootChestInfoItem() {
        return new GuiItem(new ItemBuilder(Heads.WHITE_I.getSkull()).setLore(List.of(
                // String information about lootchest
                String.format("§eLootChest: §6%s", lootChest.getUuid()),
                String.format("§eLootTable: §6%s", lootChest.getLootTableUUID()),
                String.format("§ePosition: §6%s", lootChest.getLocation().toString())
        )).setName("Informationen").build());
    }
}
