package me.einjojo.wandorialoot.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListeningWhitelist;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import java.util.List;
import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.chest.LootChest;

import org.bukkit.plugin.Plugin;

public class ChunkLoadListener implements PacketListener {

    private WandoriaLoot plugin;
    public ChunkLoadListener(WandoriaLoot plugin) {
        this.plugin = plugin;
    }


    @Override
    public void onPacketSending(PacketEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.MAP_CHUNK) {

            return;
        }
        PacketContainer packet = event.getPacket();
        int x = packet.getIntegers().read(0);
        int z = packet.getIntegers().read(1);
        List<LootChest> chests = plugin.getLootChestManager().getChests(event.getPlayer().getWorld().getChunkAt(x,z));
        if (chests == null) return;
        for (LootChest lc: chests) {
            if (!plugin.getLootChestManager().isChestDiscovered(event.getPlayer(), lc)) {
                lc.render(event.getPlayer());
            }
        }
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {

    }

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
