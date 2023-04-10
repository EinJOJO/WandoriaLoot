package me.einjojo.wandorialoot.view.chest;

import me.einjojo.joslibrary.JoPlugin;
import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.chest.LootChest;
import me.einjojo.wandorialoot.view.View;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

//TODO: Implement LootChestLootView
public class LootChestLootView extends View {

    private final int chestSize = 9*3;
    private final LootChest lootChest;
    private final WandoriaLoot plugin;
    private final Player player;

    public LootChestLootView(LootChest lootChest, Player player) {
        super(WandoriaLoot.getInstance());
        this.plugin = WandoriaLoot.getInstance();
        this.lootChest = lootChest;
        this.player = player;
    }

    @Override
    public void onInteraction(InventoryClickEvent event) {}

    private void placeItemsAtRandomSlots(ItemStack[] itemStacks) {
        for (ItemStack itemStack : itemStacks) {
            int slot = (int) (Math.random() * chestSize);
            while (inventory.getItem(slot) != null) {
                slot = (int) (Math.random() * chestSize);
            }
            inventory.setItem(slot, itemStack);
        }
    }

    public Inventory createInventory() {
        Inventory inventory = plugin.getServer().createInventory(this, chestSize, "Loot Chest");
        if (lootChest.getContent() != null) {
            placeItemsAtRandomSlots(lootChest.getContent());
        } else if (lootChest.getLootTable() != null) {
            placeItemsAtRandomSlots(lootChest.getLootTable().generate());
        } else {
            //Not configured message
            player.sendMessage(plugin.getPrefix() + "§cIt is empty because this loot chest is not configured yet!");
        }
        return inventory;
    }

    @Override
    public void onClose() {
        plugin.debug(String.format("Player %s closed the lootchest %s", player.getName(), lootChest));
        plugin.getLootChestManager().closeLootChest(lootChest, player);
    }

}
