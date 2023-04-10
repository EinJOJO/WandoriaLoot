package me.einjojo.wandorialoot.view.chest;

import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.chest.LootChest;
import me.einjojo.wandorialoot.view.View;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

//TODO: Implement LootChestConfigurationView
// Select a loot table
public class LootChestConfigurationView extends View {

    private final WandoriaLoot plugin;
    private final LootChest lootChest;
    private  Inventory inventory;
    private final Player player;
    public LootChestConfigurationView( LootChest lootChest, Player player) {
        super(WandoriaLoot.getInstance());
        this.plugin = WandoriaLoot.getInstance();
        this.lootChest = lootChest;
        this.player = player;
    }


    @Override
    public void onInteraction(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(this, 9*3, "Loot Chest Configuration");
        //fill inventory with glass
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }

        return inventory;
    }


    @Override
    public void onClose() {
        lootChest.closeChest(player);
        player.sendMessage("§cLoot Chest Configuration closed!");
    }
}
