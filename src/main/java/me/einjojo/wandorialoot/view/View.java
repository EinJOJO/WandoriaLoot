package me.einjojo.wandorialoot.view;

import me.einjojo.joslibrary.JoPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public abstract class View implements InventoryHolder, Listener {

    private final JoPlugin javaPlugin;
    protected Inventory inventory;
    boolean registered = false;
    public View(JoPlugin plugin) {
        this.javaPlugin = plugin;
        register();
    }

    public void register() {
        if (!registered) {
            registered = true;
            javaPlugin.getServer().getPluginManager().registerEvents(this, javaPlugin);
        }
    }
    public void unregister() {
        if (registered) {
            registered = false;
            HandlerList.unregisterAll(this);
        }
    }

    @EventHandler
    public void inventoryCloseListener(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() != this) return;

        onClose();
    }
    @EventHandler
    public void inventoryInteractionListener(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getHolder() != this) { return; }
        onInteraction(event);
    };
    public abstract void onClose();

    public boolean isRegistered() {
        return registered;
    }

    public void open(Player player) {
        player.openInventory(getInventory());
    }

    public abstract void onInteraction(InventoryClickEvent event);

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
    public abstract Inventory createInventory();

    @NotNull
    @Override
    public Inventory getInventory() {
        if (inventory == null) {
            setInventory(createInventory());
        }
        return inventory;
    }
}
