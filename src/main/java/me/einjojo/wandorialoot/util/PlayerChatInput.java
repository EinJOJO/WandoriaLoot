package me.einjojo.wandorialoot.util;


import me.einjojo.wandorialoot.WandoriaLoot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerChatInput implements Listener {

    private final WandoriaLoot plugin;
    private final BukkitTask taskID;
    private final Runnable callback;
    private final UUID playerUUID;

    private static final Map<UUID, PlayerChatInput> inputs = new HashMap<>();

    public PlayerChatInput(WandoriaLoot plugin, Player player, String title, Runnable callback) {
        this.plugin = plugin;

        this.taskID = new BukkitRunnable() {
            @Override
            public void run() {
                player.sendTitle(title, "§7Gib §ccancel §7ein, um abzubrechen", 0, 25 ,0);
            }
        }.runTaskTimer(plugin, 0, 20);

        this.playerUUID = player.getUniqueId();
        this.callback = callback;

        register();
    }

    @EventHandler (priority = EventPriority.LOWEST)
    private void onChat(AsyncPlayerChatEvent e) {
        String input = e.getMessage();
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if(!inputs.containsKey(playerUUID)) {
            return;
        }
        PlayerChatInput current = inputs.get(playerUUID);

        e.setCancelled(true);


        current.taskID.cancel();
        current.unregister();
        player.resetTitle();
        if(input.equalsIgnoreCase("cancel")) {
            input = null;
            player.sendMessage(plugin.getPrefix() + "§cabgebrochen.");
        }
        String finalInput = input;
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> current.callback.run(finalInput), 3);
    }

    @EventHandler
    private void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        UUID uuid = player.getUniqueId();

        if(!inputs.containsKey(uuid)){
            return;
        }
        PlayerChatInput current = inputs.get(uuid);
        current.taskID.cancel();
        current.unregister();
        player.resetTitle();
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> current.callback.run(null), 3);
        player.sendMessage(plugin.getPrefix() + "§cabgebrochen.");
    }

    private void cancel() {

    }

    private void register() {
        inputs.put(this.playerUUID, this);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private void unregister() {
        inputs.remove(this.playerUUID);
        HandlerList.unregisterAll(this);
    }

    @FunctionalInterface
    public interface Runnable {
        void run(String input);
    }



}