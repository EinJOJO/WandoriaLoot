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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class LootChest implements ConfigurationSerializable, InventoryHolder{
    private final UUID uuid;
    private final Location location;
    private ItemStack[] content;
    private LootItem[] lootTable;

    private final HashMap<UUID, Inventory> playerInventories = new HashMap<>();


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

    public ItemStack[] generateContent() {
        Random random = new Random();
        ItemStack[] generated = new ItemStack[random.nextInt(27) + 1]; //Random amount of items that will be generated
        if (lootTable == null) setLootTable(new LootItem[0]);
        for (int i = 0; i < generated.length; i++) {
            if (lootTable.length > 0) {
                ItemStack gen = lootTable[random.nextInt(lootTable.length)].getItem(); // Choose random item from loot table
                gen.setAmount(i + 1);
                generated[i] = gen;
            }
        }
        return generated;
    }

    public Location getLocation() {
        return location;
    }

    /**
     * Changes chest state
     * @param player Packet Receiver
     */
    protected void close(Player player) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.BLOCK_ACTION);
        packet.getBlockPositionModifier().write(0, new BlockPosition(location.toVector()));
        packet.getIntegers().write(0, 0);
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
    }

    /**
     * Unrenders the chest and destroys the inventory for the player.
     * @param player Packet receiver
     */
    protected void destroy(Player player) {
        playerInventories.remove(player.getUniqueId());
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.BLOCK_CHANGE);

        packet.getBlockPositionModifier().write(0, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        packet.getBlockData().write(0, WrappedBlockData.createData(Material.AIR));
        Bukkit.getScheduler().scheduleSyncDelayedTask(WandoriaLoot.getInstance(), () -> {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        }, 1);
    }

    /**
     * opens chest by packet
     * Creates inventory, (generates loot), , opens inventory
     * @param player Receiver
     */
    protected void open(Player player) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.BLOCK_ACTION);
        packet.getBlockPositionModifier().write(0, new BlockPosition(location.toVector()));
        packet.getIntegers().write(0, 1);
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        // Make lootTable
        if (SetupCommand.setUpPlayer.contains(player.getUniqueId())) {
            Inventory inventory = Bukkit.createInventory(this, 9 * 4, "§cLootTable einstellen");
            for (LootItem lootItem : getLootTable()) {
                ItemStack demo = new ItemStack(lootItem.getItem());
                inventory.addItem(demo);
            }
            playerInventories.put(player.getUniqueId(), inventory);
            player.openInventory(inventory);
        } else {
            if (playerInventories.containsKey(player.getUniqueId())) {
                player.openInventory(playerInventories.get(player.getUniqueId()));
            } else {
                Inventory inventory = Bukkit.createInventory(this, 9 * 3, "§8Lootchest");
                //Generate loot
                ItemStack[] generated = generateContent();
                Random random = new Random();
                for (int i = 0; i < generated.length; i++) {
                    int slot = random.nextInt(inventory.getSize());
                    if (inventory.getItem(slot) == null) {
                        inventory.setItem(slot, generated[i]);
                    } else {
                        i--;
                    }
                }
                playerInventories.put(uuid, inventory);
                player.openInventory(inventory);
            }

        }


    }

    public void render(Player player) {
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
        c.put("lootTable", null);
        c.put("content", null);
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

