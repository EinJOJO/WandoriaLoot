package me.einjojo.wandorialoot.gui.table;

import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.loot.LootTable;

public class LootTablesSelectorGui extends LootTablesGui {

    private SelectionResult handler;
    public LootTablesSelectorGui() {
        super(WandoriaLoot.getInstance().getLootManager().getLootTables().toArray(new LootTable[0]));
        setTitle("Select LootTable");
    }




    public void setOnSelection(SelectionResult method) {
        handler = method;
    }

    @FunctionalInterface
    protected interface SelectionResult {
        void onSelect(LootTable lootTable);
    }
}
