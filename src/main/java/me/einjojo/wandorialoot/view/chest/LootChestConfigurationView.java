package me.einjojo.wandorialoot.view.chest;

import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.chest.LootChest;
import me.einjojo.wandorialoot.view.View;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

//TODO: Implement LootChestConfigurationView
// Select a loot table
public class LootChestConfigurationView extends View {

    private final WandoriaLoot plugin;
    private final LootChest lootChest;
    public LootChestConfigurationView(WandoriaLoot plugin, LootChest lootChest) {
        super(plugin);
        this.plugin = plugin;
        this.lootChest = lootChest;
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
