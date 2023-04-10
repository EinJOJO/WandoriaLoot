package me.einjojo.wandorialoot.view.loottable;

import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.loot.LootTable;
import me.einjojo.wandorialoot.view.View;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class LootTableView extends View {

    private final WandoriaLoot plugin;
    private final LootTable lootTable;

    public LootTableView(WandoriaLoot plugin, LootTable lootTable) {
        super(plugin);
        this.plugin = plugin;
        this.lootTable = lootTable;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return null;
    }

    @Override
    public void onInteraction(InventoryClickEvent event) {

    }

    @Override
    public void onClose() {

    }
}
