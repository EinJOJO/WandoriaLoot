package me.einjojo.wandorialoot.listener;

import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.chest.LootChest;
import me.einjojo.wandorialoot.chest.LootItem;
import me.einjojo.wandorialoot.command.SetupCommand;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

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
            lootChest.closeChest((Player)e.getPlayer());
        } else {
            plugin.getLootChestManager().closeLootChest(lootChest, (Player) e.getPlayer());
        }

    }

    public void closeLootTable(HumanEntity player, LootChest lootChest) {
        ItemStack[] contents = lootChest.getPlayerInventories().get(player.getUniqueId()).getContents();
        lootChest.setLootTable(contents);
        player.sendMessage("LootTable updated!");
    }




}
