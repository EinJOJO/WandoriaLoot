package me.einjojo.wandorialoot.chest;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.loot.LootTable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Represents a loot chest.
 */
public class LootChest implements ConfigurationSerializable, InventoryHolder {

    private final Map<UUID, BukkitTask> playerChestRemoveTasks = new HashMap<>();
    private final Map<UUID, Inventory> playerInventories = new HashMap<>();
    private final UUID uuid;
    private final Location location;
    private UUID lootTableUUID;
    private final ItemStack[] content;


    /**
     * Creates a new loot chest instance for the given location.
     *
     * @param location location of the loot chest
     */
    public LootChest(Location location) {
        this(UUID.randomUUID(), location, null, null);
    }

    /**
     * Creates a new loot chest instance for the given location.
     *
     * @param uuid      uuid of the loot chest
     * @param location  location of the loot chest
     * @param content   content of the loot chest
     * @param lootTable loot table of the loot chest
     */
    public LootChest(UUID uuid, Location location, @Nullable ItemStack[] content, @Nullable UUID lootTable) {
        this.uuid = uuid;
        this.location = location;
        this.content = content;
        this.lootTableUUID = lootTable;
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
        return WandoriaLoot.getInstance().getLootManager().getLootTable(lootTableUUID);
    }

    public UUID getLootTableUUID() {
        return lootTableUUID;
    }

    /**
     * @param lootTable Loot table of the loot chest
     */
    public void setLootTable(@Nullable LootTable lootTable) {
        if (lootTable != null) {
            this.lootTableUUID = lootTable.getUuid();
        } else {
            this.lootTableUUID = null;
        }
    }

    /**
     * @return location of the loot chest
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @param player Player that receive the close packet
     *               //TODO: Fix chest close packet
     */
    public void closeChest(Player player) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.BLOCK_ACTION);
        packet.getBlockPositionModifier().write(0, new BlockPosition(location.toVector()));
        //Close chest
        packet.getIntegers().write(0, 0);
        packet.getIntegers().write(1, 0);
        Bukkit.getScheduler().scheduleSyncDelayedTask(WandoriaLoot.getInstance(), () -> {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            player.playSound(location, Sound.BLOCK_CHEST_CLOSE, 1, 1);
        }, 5);
    }

    public void openInventory(Player player) {
        if (playerInventories.containsKey(player.getUniqueId())) {
            player.openInventory(playerInventories.get(player.getUniqueId()));
        } else {
            Inventory inventory = getInventory();
            playerInventories.put(player.getUniqueId(), inventory);
            player.openInventory(inventory);
        }
    }

    public void destroyInventory(Player player) {
        playerInventories.remove(player.getUniqueId());
    }

    /**
     * Unrenders the chest and destroys the inventory for the given player after 5 seconds.
     *
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
            WandoriaLoot.getInstance().getLootChestManager().setChestDiscovered(player, this, true);
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
     *
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
     *
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
        c.put("lootTable", (lootTableUUID != null) ? lootTableUUID.toString() : null); //points to lootTable
        return c;
    }

    public static LootChest deserialize(Map<String, Object> map) {
        try {
            List<Map<String, Object>> contentList = (List<Map<String, Object>>) map.get("content");
            Location location1 = Location.deserialize((Map<String, Object>) map.get("location"));
            UUID chestUUID = UUID.fromString((String) map.get("uuid"));

            UUID lootTableUUID = UUID.fromString((String) map.getOrDefault("lootTable", null));
            ItemStack[] content;
            if (contentList == null || contentList.isEmpty()) {
                content = null;
            } else {
                content = contentList.stream().map(ItemStack::deserialize).toArray(ItemStack[]::new);
            }
            return new LootChest(chestUUID, location1, content, lootTableUUID);
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

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 27, "LootChest");
        Random random = new Random();
        LootTable lootTable = getLootTable();
        if (lootTable == null) {
            return inventory;
        }
        ItemStack[] generatedItems = lootTable.generate();
        for (ItemStack generatedItem : generatedItems) {
            int slot = random.nextInt(27);
            int tries = 0;
            while (inventory.getItem(slot) != null) {
                if (tries++ > 27) {
                    return inventory; //No empty slot found
                };
                slot = (slot+1)%27;
            }
            inventory.setItem(slot, generatedItem);
        }

        return inventory;
    }
}