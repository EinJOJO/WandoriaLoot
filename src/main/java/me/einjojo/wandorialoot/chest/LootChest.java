package me.einjojo.wandorialoot.chest;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.command.SetupCommand;
import me.einjojo.wandorialoot.loot.LootItem;
import me.einjojo.wandorialoot.loot.LootTable;
import me.einjojo.wandorialoot.view.chest.LootChestConfigurationView;
import me.einjojo.wandorialoot.view.View;
import me.einjojo.wandorialoot.view.chest.LootChestLootView;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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

    private final Map<UUID, BukkitTask> playerChestDespawnTasks = new HashMap<>();
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
     * @return random generated content from the loot table
     */
    public ItemStack[] generateContent() {
        return lootTable.generate();
    }

    /**
     * @return location of the loot chest
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @param player Player that receive the close packet
     */
    public void closeChest(Player player) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.BLOCK_ACTION);
        packet.getBlockPositionModifier().write(0, new BlockPosition(location.toVector()));
        //Close chest
        packet.getIntegers().write(0, 0);
        packet.getIntegers().write(1, 0);
        Bukkit.getScheduler().scheduleSyncDelayedTask(WandoriaLoot.getInstance(), ()-> {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            player.playSound(location, "minecraft:block.chest.close", 1, 1);
        }, 5);
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
            playerChestDespawnTasks.remove(player.getUniqueId());
        }, 2 * 20L);

        playerChestDespawnTasks.computeIfPresent(player.getUniqueId(), (uuid, bukkitTask) -> {
            bukkitTask.cancel();
            return task;
        });
        playerChestDespawnTasks.putIfAbsent(player.getUniqueId(), task);
    }


    public ItemStack[] getContent() {
        return content;
    }

    /**
     * Opens the chest by packet.
     * Generates loot, creates and opens inventory for the given player.
     * @param player Receiver
     */
    protected void openChest(Player player) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.BLOCK_ACTION);
        packet.getBlockPositionModifier().write(0, new BlockPosition(location.toVector()));
        packet.getIntegers().write(0, 1);
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        player.playSound(location, "minecraft:block.chest.open", 1, 1);

        if (playerChestDespawnTasks.get(player.getUniqueId()) != null) {
            playerChestDespawnTasks.get(player.getUniqueId()).cancel();
            playerChestDespawnTasks.remove(player.getUniqueId());
        }

        if (SetupCommand.setUpPlayer.contains(player.getUniqueId())) {
            createInventoryForSetup(player);
        } else {
            if (playerViews.containsKey(player.getUniqueId())) {
                playerViews.get(player.getUniqueId()).open(player);
            } else {
                View view = new LootChestLootView(WandoriaLoot.getInstance(), this);
                view.open(player);
            }
        }
    }



    /**
     * Creates inventory for setup and adds all the items from lootTable in it.
     * @param player Player for whom to create the inventory
     */
    private void createInventoryForSetup(Player player) {
        View view = new LootChestConfigurationView(WandoriaLoot.getInstance(), this);
        playerViews.put(player.getUniqueId(), view);
    }

    /**
     * Renders the chest for the given player.
     * @param player Packet receiver
     */
    public void renderChest(Player player) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.BLOCK_CHANGE);

        packet.getBlockPositionModifier().write(0, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        packet.getBlockData().write(0, WrappedBlockData.createData(Material.CHEST));
        Bukkit.getScheduler().scheduleSyncDelayedTask(WandoriaLoot.getInstance(), () -> {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        }, 10);
    }


    @NotNull
    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> c = new HashMap<>();
        c.put("loc", location.serialize());
        c.put("lootTable", lootTable.getName());

        List<ItemStack> contentList = new ArrayList<>();
        if (content != null) {
            Collections.addAll(contentList, this.content);
        }
        c.put("content", contentList);
        return c;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return null;
    }

    @Override
    public String toString() {
        return String.format("LootChest: { x:%.2f, y:%.2f, z:%.2f | UUID: %s }", location.getX(), location.getY(),
                location.getZ(), uuid.toString());
    }
}