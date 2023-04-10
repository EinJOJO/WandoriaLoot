package me.einjojo.wandorialoot.chest;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.command.SetupCommand;
import me.einjojo.wandorialoot.loot.LootTable;
import me.einjojo.wandorialoot.view.chest.LootChestConfigurationView;
import me.einjojo.wandorialoot.view.View;
import me.einjojo.wandorialoot.view.chest.LootChestLootView;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
/**
 * Represents a loot chest.
 */
public class LootChest implements ConfigurationSerializable {

    private final Map<UUID, BukkitTask> playerChestRemoveTasks = new HashMap<>();
    private final UUID uuid;
    private final Location location;
    private LootTable lootTable;
    private final ItemStack[] content;


    private final HashMap<UUID, View> playerViews = new HashMap<>();

    /**
     * Creates a new loot chest instance for the given location.
     * @param location location of the loot chest
     */
    public LootChest(Location location) {
        this(UUID.randomUUID(), location, null, null);
    }

    /**
     * Creates a new loot chest instance for the given location.
     * @param uuid uuid of the loot chest
     * @param location location of the loot chest
     * @param content content of the loot chest
     * @param lootTable loot table of the loot chest
     */
    public LootChest(UUID uuid, Location location, @Nullable ItemStack[] content, @Nullable LootTable lootTable) {
        this.uuid = uuid;
        this.location = location;
        this.content = content;
        this.lootTable = lootTable;
    }

    /**
     * @return uuid of the loot chest
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * @return Loot table of the loot chest
     */
    public LootTable getLootTable() {
        return lootTable;
    }

    /**
     * @param lootTable Loot table of the loot chest
     */
    public void setLootTable(LootTable lootTable) {
        this.lootTable = lootTable;
    }

    /**
     * @return location of the loot chest
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @param player Player that receive the close packet
     * //TODO: Fix chest close packet
     */
    public void closeChest(Player player) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.BLOCK_ACTION);
        packet.getBlockPositionModifier().write(0, new BlockPosition(location.toVector()));
        //Close chest
        packet.getIntegers().write(0, 0);
        packet.getIntegers().write(1, 0);
        Bukkit.getScheduler().scheduleSyncDelayedTask(WandoriaLoot.getInstance(), ()-> {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            player.playSound(location, Sound.BLOCK_CHEST_CLOSE, 1, 1);
        }, 5);
    }

    public void openInventory(Player player) {
        View view = playerViews.get(player.getUniqueId());
        if (view instanceof LootChestConfigurationView) {
            destroyInventory(player); // Destroy old view, to prevent opening Config-View twice
            view = null;
        }
        if(view == null) { // Create new View if player has no View
            if (SetupCommand.setUpPlayer.contains(player.getUniqueId())) {
                view = new LootChestConfigurationView( this, player);
            } else {
                view = new LootChestLootView(this, player);
            }
            playerViews.put(player.getUniqueId(), view);
        }
        view.open(player);
    }

    public void destroyInventory(Player player) {
        View view = playerViews.get(player.getUniqueId());
        if(view == null) {
            return;
        }
        view.unregister();
        playerViews.remove(player.getUniqueId());
    }

    /**
     * Unrenders the chest and destroys the inventory for the given player after 5 seconds.
     * @param player Packet receiver
     */
    protected void destroyChest(Player player) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.BLOCK_CHANGE);
        packet.getBlockPositionModifier().write(0, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        packet.getBlockData().write(0, WrappedBlockData.createData(Material.AIR));

        BukkitTask task = Bukkit.getScheduler().runTaskLater(WandoriaLoot.getInstance(), () -> {
            destroyInventory(player);
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            playerChestRemoveTasks.remove(player.getUniqueId());
        }, 2 * 20L);

        playerChestRemoveTasks.computeIfPresent(player.getUniqueId(), (uuid, bukkitTask) -> {
            bukkitTask.cancel();
            return task;
        });
        playerChestRemoveTasks.putIfAbsent(player.getUniqueId(), task);
    }


    public ItemStack[] getContent() {
        return content;
    }

    /**
     * Opens the chest by packet.
     * @param player Receiver
     */
    protected void openChest(Player player) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.BLOCK_ACTION);
        packet.getBlockPositionModifier().write(0, new BlockPosition(location.toVector()));
        packet.getIntegers().write(0, 1);
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        player.playSound(location, Sound.BLOCK_CHEST_OPEN, 1, 1);

        if (playerChestRemoveTasks.get(player.getUniqueId()) != null) {
            playerChestRemoveTasks.get(player.getUniqueId()).cancel();
            playerChestRemoveTasks.remove(player.getUniqueId());
        }
    }

    /**
     * Renders the chest for the given player.
     * @param player Packet receiver
     */
    public void renderChest(Player player) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.BLOCK_CHANGE);

        packet.getBlockPositionModifier().write(0, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        packet.getBlockData().write(0, WrappedBlockData.createData(Material.CHEST));
        Bukkit.getScheduler().scheduleSyncDelayedTask(WandoriaLoot.getInstance(), () -> ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet), 10);
    }


    @NotNull
    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> c = new HashMap<>();
        List<Map<String, Object>> contentList = new ArrayList<>();
        if (content != null) {
            for (ItemStack itemStack : content) {
                contentList.add(itemStack.serialize());
            }
        }
        c.put("uuid", uuid.toString());
        c.put("content", (!contentList.isEmpty()) ? contentList : null);
        c.put("location", location.serialize());
        c.put("lootTable", (lootTable != null) ? lootTable.getUuid().toString() : null); //points to lootTable
        return c;
    }

    public static LootChest deserialize(Map<String, Object> map) {
        try {
            List<Map<String,Object>> contentList = (List<Map<String,Object>>) map.get("content");
            Location location1 = Location.deserialize((Map<String, Object>) map.get("location"));
            UUID chestUUID = UUID.fromString((String) map.get("uuid"));

            UUID lootTableUUID = UUID.fromString((String) map.get("lootTable"));
            LootTable lootTable = WandoriaLoot.getInstance().getLootManager().getLootTable(lootTableUUID);
            ItemStack[] content;
            if (contentList.isEmpty()) {
                content = null;
            } else {
                content = contentList.stream().map(ItemStack::deserialize).toArray(ItemStack[]::new);
            }
            return new LootChest(chestUUID, location1, content, lootTable);
        } catch (Exception e) {
            WandoriaLoot.getInstance().warn("Could not deserialize LootChest: " + e.getMessage());
            if (WandoriaLoot.getInstance().isDebug()) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("LootChest: { x:%.2f, y:%.2f, z:%.2f | UUID: %s }", location.getX(), location.getY(),
                location.getZ(), uuid.toString());
    }
}