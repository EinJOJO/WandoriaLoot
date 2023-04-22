package me.einjojo.wandorialoot.gui.table;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import me.einjojo.joslibrary.util.ItemBuilder;
import me.einjojo.wandorialoot.gui.LootItemConfigurator;
import me.einjojo.wandorialoot.loot.LootItem;
import me.einjojo.wandorialoot.loot.LootTable;
import me.einjojo.wandorialoot.util.Heads;
import me.einjojo.wandorialoot.util.ItemHelper;
import me.einjojo.wandorialoot.util.Sounds;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LootTableGui extends ChestGui {
    private boolean saved;
    private boolean editsItem = false;
    private final String baseTitle;
    private final Map<Integer, LootItem> lootItemsList;
    private final LootTable lootTable;
    private OutlinePane contentPane;
    public LootTableGui(LootTable lootTable) {
        super(6, "Lade...");
        this.baseTitle = "Loot Table " + lootTable.getRarity().getColor() + lootTable.getName();
        this.lootTable = lootTable;
        setOnBottomClick(this::selectItemClick);
        setOnTopClick(e -> e.setCancelled(true));

        lootItemsList = lootTable.getContent().stream().collect(HashMap::new, (m, v) -> m.put(v.getID(), v), HashMap::putAll);
        setOnClose((e) -> {
            if (editsItem) return;
            HumanEntity p = e.getPlayer();
            new LootTablesGui().show(p);
            if (saved) {
                return;
            }
            p.sendMessage("§cAbgebrochen");
            Sounds.CANCEL.play(p);
        });
        addPanes();
        setSaved(true);
    }


    public void setSaved(boolean saved) {
        this.saved = saved;
        setTitle(baseTitle + (saved ? " §7(§aGespeichert§7)" : " §7(§cNicht gespichert§7)"));
        update();
    }

    public void selectItemClick(InventoryClickEvent e) {
        e.setCancelled(true);
        ItemStack itemStack = e.getCurrentItem();
        if (itemStack == null) return;
        if (itemStack.getType() == Material.AIR) return;
        LootItem lootItem = new LootItem(itemStack);
        lootItemsList.put(lootItem.getID(), lootItem);
        Sounds.GUI_CLICK.play(e.getWhoClicked());
        loadContentPane();
        setSaved(false);
    }

    public void addPanes() {
        addPane(loadContentPane());
        addPane(divisionPane());
        addPane(buttons());
    }

    public void returnToView(HumanEntity p) {
        show(p);
        editsItem = false;
        loadContentPane();
        update();

    }

    private StaticPane buttons() {
        StaticPane staticPane = new StaticPane(0,0,1,6);
        staticPane.addItem(new GuiItem(ItemHelper.lootTableItem(lootTable).build()), Slot.fromIndex(0));
        staticPane.addItem(new GuiItem(new ItemBuilder(Material.NAME_TAG).setName("§eUmbenennen").build(), (e) -> {

        }), Slot.fromIndex(1));
        staticPane.addItem(new GuiItem(new ItemBuilder(Heads.BARRIER.getSkull()).setName("§cAbbrechen").build(),e -> e.getWhoClicked().closeInventory()), Slot.fromIndex(4));
        staticPane.addItem(new GuiItem(new ItemBuilder(Heads.GREEN_CHECK.getSkull()).setName("§aSpeichern").build(),(e) -> {
            lootTable.setContent(lootItemsList.values().stream().toList());
            setSaved(true);
            e.getWhoClicked().sendMessage("§aLootTable gespeichert");
            e.getWhoClicked().closeInventory();
        }), Slot.fromIndex(5));
        staticPane.setPriority(Pane.Priority.HIGH);
        return staticPane;
    }


    private GuiItem guiLootItem(LootItem lootItem) {
        ArrayList<String> lore = ItemHelper.getLootItemLore(lootItem);
        lore.add("§7Linksklick zum §abearbeiten");
        lore.add("§7Rechtsklick zum §centfernen");
        return new GuiItem(new ItemBuilder(lootItem.getItem()).setLore(lore).setPersistentData("lootItem", lootItem.getID(), PersistentDataType.INTEGER).build(), (e -> {
            e.setCancelled(true);
            Sounds.GUI_CLICK.play(e.getWhoClicked());
            ItemStack item = e.getCurrentItem();
            Integer lootID = (Integer) new ItemBuilder(item).getPersistentData("lootItem", PersistentDataType.INTEGER);
            if (e.isLeftClick()) {
                editsItem = true;
                new LootItemConfigurator(lootItemsList.get(lootID), this).show(e.getWhoClicked());
                return;
            }
            if(e.isRightClick()) {
                lootItemsList.remove(lootID);
                loadContentPane();
                update();
            }
        }));
    }

    public OutlinePane loadContentPane() {
        if (contentPane == null) {
            contentPane = new OutlinePane(2, 0, 7, 6);
            contentPane.setPriority(Pane.Priority.NORMAL);
        }
        contentPane.clear();
        for (LootItem lootItem : lootItemsList.values()) {
            contentPane.addItem(guiLootItem(lootItem));
        }

        return contentPane;
    }

    public OutlinePane divisionPane() {
        OutlinePane pane = new OutlinePane(0, 0, 2, 6);
        pane.addItem(new GuiItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§8").build()));
        pane.setRepeat(true);
        pane.setPriority(Pane.Priority.LOWEST);
        return pane;
    }
}
