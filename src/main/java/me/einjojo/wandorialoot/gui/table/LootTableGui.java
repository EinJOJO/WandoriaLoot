package me.einjojo.wandorialoot.gui.table;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import me.einjojo.wandorialoot.loot.LootTable;

public class LootTableGui extends ChestGui {

    public LootTableGui(LootTable lootTable) {
        super(5, "Loot Table " + lootTable.getName());

    }
}
