package me.einjojo.wandorialoot.view.loottable;

import me.einjojo.joslibrary.JoPlugin;
import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.loot.LootItem;
import me.einjojo.wandorialoot.view.View;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LootTableConfigurationView extends View {

    private final WandoriaLoot plugin;

    public LootTableConfigurationView() {
        super(WandoriaLoot.getInstance());
        plugin = WandoriaLoot.getInstance();
    }

    @Override
    public void onClose() {

    }

    @Override
    public void onInteraction(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        ItemStack currentItem = event.getCurrentItem();

        plugin.debug(event.getAction().toString());
        plugin.debug(event.getClick().toString());
    }

    private ItemStack convert(LootItem lootItem) {
        ItemStack itemStack = new ItemStack(lootItem.getItem());
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            itemMeta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
        }

        return null;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 54, "Loot Table Configuration");



        return inventory;
    }
}
