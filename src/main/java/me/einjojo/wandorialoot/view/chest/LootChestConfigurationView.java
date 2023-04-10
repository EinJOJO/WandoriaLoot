package me.einjojo.wandorialoot.view.chest;

import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.chest.LootChest;
import me.einjojo.wandorialoot.loot.Rarity;
import me.einjojo.wandorialoot.view.GuiItem;
import me.einjojo.wandorialoot.view.View;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

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
        event.getWhoClicked().sendMessage(event.getSlot() + "");
        plugin.debug(event.getAction().toString());
    }



    @Override
    public Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(this, 9*6, "Loot Chest Configuration");
        //fill inventory with glass
        for (int i = 0; i < 9; i++) { // First row
            // Set glass based on lootchest  status
            if (lootChest.getContent() != null) {
                inventory.setItem(i, new GuiItem(Material.GRAY_STAINED_GLASS_PANE, "§8Nutze §7Predefined-Content"));
            } else if (lootChest.getLootTable() != null) {
                Rarity selectedRarity = lootChest.getLootTable().getRarity();
                inventory.setItem(i, new GuiItem(selectedRarity.getGuiMaterial(), "§8Nutze" + selectedRarity.getColor() + selectedRarity.name() + " LootTable"));
            } else {
                inventory.setItem(i, new GuiItem(Material.RED_STAINED_GLASS_PANE, "§4Nicht Konfiguriert"));
            }
        }
        //Following rows
        for (int i = 9; i < inventory.getSize(); i++) {
            inventory.setItem(i, new GuiItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE));
        }
        inventory.setItem(5, new GuiItem(Material.SHULKER_BOX, "§aTest-Generation", "§7Öffne die Chest und schaue was drin ist!"));
        GuiItem lootTable = new GuiItem(Material.CHEST_MINECART, "§7LootTable", "§8Setze den möglichen Inhalt der Chest", "");
        if (lootChest.getLootTable() != null) {
            lootTable.setLeftClickAction(() -> {
                //TODO: LeftClickAction
            }, "§aSetze die LootTable der Chest");
            lootTable.getBaseLore().add("Aktuell ausgewählt:" + lootChest.getLootTable().getRarity().getColor() + lootChest.getLootTable().getName());
        }
        lootTable.setRightClickAction(() -> {
            //TODO: RightClickAction
        }, "§cEntferne die LootTable der Chest");
        inventory.setItem(29, lootTable);


        inventory.setItem(31, new GuiItem(Material.SUNFLOWER, "§7Information", "§8Setze die LootTable der Chest"));

        GuiItem contentItem = new GuiItem(Material.CHEST_MINECART, "§7Content",
                "§8Setze den Inhalt der Chest", "");
        contentItem.setLeftClickAction(() -> {
            //TODO: LeftClickAction
        }, "§aSetze den Inhalt der Chest");
        if (lootChest.getContent() != null) {
            contentItem.getBaseLore().add("§7Aktuell definiert: " + "§aJa");
            contentItem.setRightClickAction(() -> {
                //TODO: RightClickAction
            }, "§cEntferne den Inhalt der Chest");
        } else {
            contentItem.getBaseLore().add("§7Aktuell definiert: " + "§cNein");
        }
        inventory.setItem(33, contentItem);
        inventory.setItem(45, new GuiItem(Material.BARRIER, "§cLöschen", "§cEntferne die Chest"));

        return inventory;
    }


    @Override
    public void onClose() {
        lootChest.closeChest(player);
        player.sendMessage("§cLoot Chest Configuration closed!");
    }
}
