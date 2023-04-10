package me.einjojo.wandorialoot.view.loottable;

import me.einjojo.joslibrary.JoPlugin;
import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.loot.LootTable;
import me.einjojo.wandorialoot.view.View;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

//TODO: Implement LootTablesView
public class LootTablesView extends View {
    public LootTablesView(LootTable[] lootTables) {
        super(WandoriaLoot.getInstance());
    }

    @Override
    public void onClose() {
        unregister();
    }

    @Override
    public void onInteraction(InventoryClickEvent event) {

    }

    @Override
    public Inventory createInventory() {
        return null;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return null;
    }
}
