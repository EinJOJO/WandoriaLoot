package me.einjojo.wandorialoot.view;

import me.einjojo.joslibrary.JoPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

public abstract class View implements InventoryHolder, Listener {

    private final JoPlugin javaPlugin;
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
    private void inventoryCloseListener(InventoryCloseEvent event) {
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

}
