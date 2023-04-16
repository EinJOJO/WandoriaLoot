package me.einjojo.wandorialoot.view.loottable;

import me.einjojo.joslibrary.JoPlugin;
import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.loot.LootItem;
import me.einjojo.wandorialoot.loot.LootTable;
import me.einjojo.wandorialoot.view.View;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

//TODO: Implement LootTableConfigurationView
public class LootTableConfigurationView extends View {

    private final WandoriaLoot plugin;
    private final LootTable table;
    public LootTableConfigurationView(LootTable table) {
        super(WandoriaLoot.getInstance());
        plugin = WandoriaLoot.getInstance();
        this.table = table;
    }

    @Override
    public void onClose() {

    }

    @Override
    public void onInteraction(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        ItemStack currentItem = event.getCurrentItem();
        int slot = event.getSlot();
        // first and second row with guiItem black glass pane
        for (int i = 0; i < 18; i++) {
            //Stop it...        }
        // Only 3-5 slots are interactable
        if (slot < 2*9 || slot > 5*9) {
            event.setCancelled(true);
            return;
        };

        plugin.debug(event.getAction().toString());
        plugin.debug(event.getClick().toString());
    }

    @Override
    public Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(this, 54, "Loot Table Configuration");

        for (LootItem lootItem : table.getContent()) {
            inventory.addItem(convert(lootItem));
        }

        return inventory;
    }

    private ItemStack convert(LootItem lootItem) {
        ItemStack itemStack = new ItemStack(lootItem.getItem());
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            itemMeta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
        }

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
