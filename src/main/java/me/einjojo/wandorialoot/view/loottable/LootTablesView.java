package me.einjojo.wandorialoot.view.loottable;

import me.einjojo.joslibrary.JoPlugin;
import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.loot.LootTable;
import me.einjojo.wandorialoot.view.View;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

//TODO: Implement LootTablesView
public class LootTablesView extends View {

    private final LootTable[] lootTables;
    private Player player;
    public LootTablesView(LootTable[] lootTables) {
        super(WandoriaLoot.getInstance());
        this.lootTables = lootTables;
    }
    public LootTablesView(LootTable[] lootTables, Player player, View parentView) {
        super(WandoriaLoot.getInstance());
        this.lootTables = lootTables;
        this.parentView = parentView;
        this.player = player;
    }

    @Override
    public void onClose() {
        unregister();
    }

    @Override
    public void onInteraction(InventoryClickEvent event) {
        if (parentView != null) {
            parentView.onInteraction(event);
        }
    }

    @Override
    public Inventory createInventory() {
        Inventory inv = Bukkit.createInventory(this, 54, "Loot Tables");
        if (getParentView() != null) {

        }
        return inv;
    }
}
