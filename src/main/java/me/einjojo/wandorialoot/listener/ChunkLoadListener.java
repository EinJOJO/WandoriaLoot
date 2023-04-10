package me.einjojo.wandorialoot.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListeningWhitelist;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import java.util.Set;

import com.comphenix.protocol.wrappers.BlockPosition;
import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.chest.LootChest;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

public class ChunkLoadListener implements PacketListener {

    private WandoriaLoot plugin;
    public ChunkLoadListener(WandoriaLoot plugin) {
        this.plugin = plugin;
    }


    @Override
    public void onPacketSending(PacketEvent event) {
        //Prevent loot chest from being replaced unless it is discovered
        if (event.getPacketType() == PacketType.Play.Server.BLOCK_CHANGE) {
            BlockPosition bps = event.getPacket().getBlockPositionModifier().read(0);
            Location loc = bps.toLocation(event.getPlayer().getWorld());
            if (!plugin.getLootChestManager().isLootChest(loc)) return;
            if (plugin.getLootChestManager().isChestDiscovered(event.getPlayer(), plugin.getLootChestManager().getLootChest(loc))) return;
            if (event.getPacket().getBlockData().read(0).getType() == Material.CHEST) return;
            event.setCancelled(true);
            return;
        }
        //Send loot chest to player if it is in the chunk that is being sent
        PacketContainer packet = event.getPacket();
        int x = packet.getIntegers().read(0);
        int z = packet.getIntegers().read(1);
        Set<LootChest> chests = plugin.getLootChestManager().getChests(event.getPlayer().getWorld().getChunkAt(x,z));
        if (chests == null) return;
        for (LootChest lc: chests) {
            //TODO: Implement Bypass Check or lootChestManager render logic
            if (!plugin.getLootChestManager().isChestDiscovered(event.getPlayer(), lc)) {
                lc.renderChest(event.getPlayer());
            }
        }
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {}

    @Override
    public ListeningWhitelist getSendingWhitelist() {
        return ListeningWhitelist.newBuilder().types(PacketType.Play.Server.MAP_CHUNK, PacketType.Play.Server.BLOCK_CHANGE).build();
    }

    @Override
    public ListeningWhitelist getReceivingWhitelist() {
        return null;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }
}
