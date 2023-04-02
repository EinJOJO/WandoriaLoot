package me.einjojo.wandorialoot.chest;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LootChest implements ConfigurationSerializable {
    private final UUID uuid;
    private final Location location;
    private ItemStack[] content;
    private LootItem[] lootTable;

    public LootChest(Location location) {
        this(UUID.randomUUID(),location, null, null);
    }

    public LootChest(UUID uuid, Location l, @Nullable ItemStack[] content, @Nullable LootItem[] lootTable) {
        this.uuid = uuid;
        this.location = l;
        this.content = content;
        this.lootTable = lootTable;
    }

    public UUID getUuid() {
        return uuid;
    }

    public LootItem[] getLootTable() {
        return lootTable;
    }

    public void setLootTable(LootItem[] lootTable) {
        this.lootTable = lootTable;
    }

    public ItemStack[] getContent() {
        return content;
    }

    public Location getLocation() {
        return location;
    }

    public void setContent(ItemStack[] content) {
        this.content = content;
    }

    public void render(Player player) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.BLOCK_CHANGE);

        // Set the block change data for the chest block at the given location
        packet.getBlockPositionModifier().write(0, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        packet.getBlockData().write(0, WrappedBlockData.createData(Material.CHEST));

        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> c = new HashMap<>();
        c.put("loc", location.serialize());
        c.put("lootTable", null);
        c.put("content", null);
        return c;
    }
}

