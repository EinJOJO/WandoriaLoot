package me.einjojo.wandorialoot.chest;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.command.SetupCommand;
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
    private final ItemStack[] content;
    private LootItem[] lootTable;

    private final HashMap<UUID, Inventory> playerInventories = new HashMap<>();

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
    public LootChest(UUID uuid, Location location, @Nullable ItemStack[] content, @Nullable LootItem[] lootTable) {
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
    public LootItem[] getLootTable() {
        return lootTable;
    }

    /**
     * @param lootTable Loot table of the loot chest
     */
    public void setLootTable(LootItem[] lootTable) {
        this.lootTable = lootTable;
    }

    /**
     * @param itemStacks Loot of the loot chest that will be converted to a loot table
     */
    public void setLootTable(ItemStack[] itemStacks) {
        ArrayList<LootItem> lootItems = new ArrayList<>();
        for (ItemStack content : itemStacks) {
            if (content == null || content.getType().equals(Material.AIR)) continue;
            lootItems.add(new LootItem(content));
        }
        setLootTable(lootItems.toArray(new LootItem[]{}));
    }

    /**
     * @return random generated content from the loot table
     */
    public ItemStack[] generateContent() {
        if (lootTable == null) {
            setLootTable(new LootItem[0]);
        }
        ItemStack[] generated = new ItemStack[new Random().nextInt(27) + 1];
        for (int i = 0; i < generated.length; i++) {
            if (lootTable.length > 0) {
                ItemStack gen = lootTable[new Random().nextInt(lootTable.length)].getItem();
                gen.setAmount(i + 1);
                generated[i] = gen;
            }
        }
        return generated;
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
        packet.getIntegers().write(0, 0);
        Bukkit.getScheduler().scheduleSyncDelayedTask(WandoriaLoot.getInstance(), ()-> {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        }, 2);
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
            playerInventories.remove(player.getUniqueId());
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            playerChestDespawnTasks.remove(player.getUniqueId());
        }, 5 * 20L);

        playerChestDespawnTasks.computeIfPresent(player.getUniqueId(), (uuid, bukkitTask) -> {
            bukkitTask.cancel();
            return task;
        });
        playerChestDespawnTasks.putIfAbsent(player.getUniqueId(), task);
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

        if (playerChestDespawnTasks.get(player.getUniqueId()) != null) {
            playerChestDespawnTasks.get(player.getUniqueId()).cancel();
            playerChestDespawnTasks.remove(player.getUniqueId());
        }

        if (SetupCommand.setUpPlayer.contains(player.getUniqueId())) {
            createInventoryForSetup(player);
        } else {
            if (playerInventories.containsKey(player.getUniqueId())) {
                player.openInventory(playerInventories.get(player.getUniqueId()));
            } else {
                createInventoryAndFillWithLoot(player);
            }
        }
    }

    /**
     * Creates inventory and fills it with the generated loot for the given player.
     * @param player Player for whom to create the inventory
     */
    private void createInventoryAndFillWithLoot(Player player) {
        Inventory inventory = Bukkit.createInventory(this, 9 * 3, "§8Lootchest");
        ItemStack[] content;
        if (this.content != null && this.content.length > 0) {
            // Content is already set, thus no loot gen.
            content = this.content;
        } else {
            //Generate it
            content = generateContent();
        }

        //Place at random slots
        Random random = new Random();
        for (int i = 0; i < content.length; i++) {
            int slot = random.nextInt(inventory.getSize());
            if (inventory.getItem(slot) == null) {
                inventory.setItem(slot, content[i]);
            } else {
                i--;
            }
        }

        //Set inventory
        playerInventories.put(player.getUniqueId(), inventory);
        player.openInventory(inventory);
    }

    /**
     * Creates inventory for setup and adds all the items from lootTable in it.
     * @param player Player for whom to create the inventory
     */
    private void createInventoryForSetup(Player player) {
        Inventory inventory = Bukkit.createInventory(this, 9 * 4, "§cLootTable einstellen");
        if (getLootTable() != null) {
            for (LootItem lootItem : getLootTable()) {
                if (lootItem.getItem() == null) continue;
                ItemStack demo = new ItemStack(lootItem.getItem());
                inventory.addItem(demo);
            }
        }
        playerInventories.put(player.getUniqueId(), inventory);
        player.openInventory(inventory);
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

    public HashMap<UUID, Inventory> getPlayerInventories() {
        return playerInventories;
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> c = new HashMap<>();
        c.put("loc", location.serialize());
        c.put("lootTable", lootTable);
        c.put("content", content);
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