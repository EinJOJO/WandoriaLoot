package me.einjojo.wandorialoot.gui.table;

import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.loot.LootTable;
import org.bukkit.event.inventory.InventoryClickEvent;

public class LootTablesSelectorGui extends LootTablesGui {


    private final SelectionResult handler;

    public LootTablesSelectorGui(SelectionResult handler) {
        super(WandoriaLoot.getInstance().getLootManager().getLootTables().toArray(new LootTable[0]));
        this.handler = handler;
        setTitle("§aWähle LootTable aus");
        setOnClose(e -> handler.onSelect(null));
    }

    @Override
    public void onChestSelect(InventoryClickEvent e) {
        LootTable lootTable = getLootChest(e.getCurrentItem());
        if (lootTable == null) return;
        e.getWhoClicked().closeInventory();
        handler.onSelect(lootTable);
    }

    @FunctionalInterface
    public interface SelectionResult {
        void onSelect(LootTable lootTable);
    }
}
