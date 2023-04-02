package me.einjojo.wandorialoot.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListeningWhitelist;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.ChunkCoordIntPair;
import me.einjojo.wandorialoot.WandoriaLoot;
import net.minecraft.network.protocol.Packet;
import org.bukkit.Bukkit;
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

        Bukkit.getLogger().info(
                String.format("%d %d %s", x, z, event.getPlayer().getName())
        );
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {

    }

    @Override
    public ListeningWhitelist getSendingWhitelist() {
        return ListeningWhitelist.newBuilder().types(PacketType.Play.Server.MAP_CHUNK).build();
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
