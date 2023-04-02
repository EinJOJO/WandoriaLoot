package me.einjojo.wandorialoot.listener;

import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.chest.LootChest;
import me.einjojo.wandorialoot.chest.LootItem;
import me.einjojo.wandorialoot.command.SetupCommand;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryCloseListener implements Listener {

    WandoriaLoot plugin;
    public InventoryCloseListener(WandoriaLoot plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void closeInventory(InventoryCloseEvent e) {
        if (!(e.getInventory().getHolder() instanceof LootChest lootChest)) return;
        if (SetupCommand.setUpPlayer.contains(e.getPlayer().getUniqueId())) {
            closeLootTable(e.getPlayer(), lootChest);
        } else {
            closeLootChest(e.getPlayer(), lootChest);
        }
    }

    public void closeLootTable(HumanEntity player, LootChest lootChest) {
        ItemStack[] contents = lootChest.getPlayerInventories().get(player.getUniqueId()).getContents();
        LootItem[] lootItems = new LootItem[contents.length];
        for (int i = 0; i < contents.length; i++) {
            lootItems[i] = new LootItem(contents[i]);
        }
        lootChest.setLootTable(lootItems);
        player.sendMessage("LootTable updated!");
    }


    public void closeLootChest(HumanEntity player, LootChest lootChest) {

    }

}
