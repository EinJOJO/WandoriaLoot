package me.einjojo.wandorialoot.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListeningWhitelist;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.MovingObjectPositionBlock;
import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.chest.LootChest;
import me.einjojo.wandorialoot.command.SetupCommand;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class InteractionListener implements Listener, PacketListener {

    private final WandoriaLoot plugin;
    public InteractionListener(WandoriaLoot plugin) {
        this.plugin = plugin;
    }


    /**
     * Setup of the chest
     */
    @EventHandler
    public void setupChest(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) return;
        if (!(SetupCommand.setUpPlayer.contains(e.getPlayer().getUniqueId()))) return;
        e.setCancelled(true);
        Block block = e.getClickedBlock();
        block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
        block.setType(Material.AIR);
        //Create lootChest
        LootChest lootChest = new LootChest(block.getLocation());
        plugin.getLootChestManager().addChest(lootChest);

        // Send BlockChange Packet to all players that see the chunk
        Chunk chunk = block.getChunk();
        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld() == block.getWorld()) {
                int viewDistance = player.getClientViewDistance();
                int playerChunkX = player.getLocation().getBlockX() >> 4;
                int playerChunkZ = player.getLocation().getBlockZ() >> 4;
                if (Math.abs(chunkX - playerChunkX) <= viewDistance && Math.abs(chunkZ - playerChunkZ) <= viewDistance) {
                    lootChest.renderChest(player); // Sends BlockChange Packet
                }
            }
        }

    }


    @Override
    public void onPacketSending(PacketEvent event) {

    }

    @Override
    public void onPacketReceiving(PacketEvent e) {
        if (e.getPacketType() == PacketType.Play.Client.USE_ITEM) {
            MovingObjectPositionBlock pos = e.getPacket().getMovingBlockPositions().read(0);
            Location location = pos.getBlockPosition().toLocation(e.getPlayer().getWorld());
            if (!(plugin.getLootChestManager().isLootChest(location))) return;
            LootChest lootChest = plugin.getLootChestManager().getLootChest(location);
            if (lootChest == null) return;
            e.setCancelled(true);
            new BukkitRunnable() { // sync it
                @Override
                public void run() {
                    plugin.getLootChestManager().openLootChest(lootChest, e.getPlayer());
                }
            }.runTask(plugin);
        }
        if (e.getPacketType() == PacketType.Play.Client.BLOCK_DIG) {
            if (!(SetupCommand.setUpPlayer.contains(e.getPlayer().getUniqueId()))) return;
            //TODO: Do something when the block gets destroyed
            // Maybe delete the chest from the list

            }

    }

    @Override
    public ListeningWhitelist getSendingWhitelist() {
        return null;
    }

    @Override
    public ListeningWhitelist getReceivingWhitelist() {
         return ListeningWhitelist.newBuilder().types(PacketType.Play.Client.BLOCK_DIG, PacketType.Play.Client.USE_ITEM).build();
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }
}
