package me.einjojo.wandorialoot.listener;

import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.chest.LootChest;
import me.einjojo.wandorialoot.command.SetupCommand;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractionListener implements Listener {

    private final WandoriaLoot plugin;
    public InteractionListener(WandoriaLoot plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChestClick(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) return;
        if (!(SetupCommand.setUpPlayer.contains(e.getPlayer().getUniqueId()))) return;
        e.setCancelled(true);
        Block block = e.getClickedBlock();
        block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
        block.setType(Material.AIR);
        LootChest lootChest = new LootChest(block.getLocation());
        plugin.getLootChestManager().addChest(lootChest);

        SetupCommand.setUpPlayer.remove(e.getPlayer().getUniqueId());
        lootChest.render(e.getPlayer());

    }




}
