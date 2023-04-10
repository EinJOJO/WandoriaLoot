package me.einjojo.wandorialoot.view.chest;

import me.einjojo.joslibrary.JoPlugin;
import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.chest.LootChest;
import me.einjojo.wandorialoot.view.View;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class LootChestLootView extends View {

    private final LootChest lootChest;
    private final WandoriaLoot plugin;
    private Inventory inventory;

    public LootChestLootView(WandoriaLoot plugin, LootChest lootChest) {
        super(plugin);
        this.plugin = plugin;
        this.lootChest = lootChest;
    }

    @Override
    public void onInteraction(InventoryClickEvent event) {

    }

    private void createInventory() {
        inventory = plugin.getServer().createInventory(this, 54, "Loot Chest");
        if (lootChest.getContent() != null) {
            //Use preconfigured content
        } else if (lootChest.getLootTable() != null) {
            //Use LootTable to generate content
        } else {

        }
    }

    @Override
    public void onClose() {

    }

    @NotNull
    @Override
    public Inventory getInventory() {
        if (inventory == null) {
            createInventory();
        }
        return inventory;
    }
}
