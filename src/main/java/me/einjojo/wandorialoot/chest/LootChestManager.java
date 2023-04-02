package me.einjojo.wandorialoot.chest;

import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.config.LootChestConfig;
import me.einjojo.wandorialoot.config.PlayersConfig;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class LootChestManager {

    private final WandoriaLoot plugin;
    private final Map<Chunk, List<LootChest>> chunkChestMap = new HashMap<>();
    private Map<UUID, List<LootChest>> playerChestMap;
    private final PlayersConfig playersConfig;
    private final LootChestConfig lootChestConfig;

    public LootChestManager(WandoriaLoot plugin) {
        this.plugin = plugin;
        lootChestConfig = new LootChestConfig(plugin);
        playersConfig = new PlayersConfig(plugin);

    }

    public boolean isChestDiscovered(Player player, LootChest lootChest) {
        if (playerChestMap.get(player.getUniqueId()) == null) return false;
        return playerChestMap.get(player.getUniqueId()).contains(lootChest);
    }

    public void loadConfig() {
        List<LootChest> lcs = lootChestConfig.readConfig();
        for (LootChest lc : lcs) {
            addChest(lc);
        }
        playerChestMap = playersConfig.readConfig();
    }
    public void saveConfig() {
        List<LootChest> allChests = new ArrayList<>();
        for (List<LootChest> chunkChests : chunkChestMap.values()) {
            allChests.addAll(chunkChests);
        }
        lootChestConfig.saveConfig(allChests);
        playersConfig.saveConfig(playerChestMap);
    }


    /**
     * Adds a lootChest
     * @param lootChest Lootchest that should be registered inside the chunkmap
     */
    public void addChest(LootChest lootChest) {
        Chunk lChunk = lootChest.getLocation().getChunk();
        if (chunkChestMap.containsKey(lChunk)) {
            chunkChestMap.get(lChunk).add(lootChest);
        } else {
            List<LootChest> list = new ArrayList<>();
            list.add(lootChest);
            chunkChestMap.put(lChunk, list);
        }
        plugin.debug(String.format("Added Lootchest %s", lootChest.getUuid().toString()));

    }


    /**
     *
     * @param uuid UUID of the LootChest
     * @return a LootChest object
     */
    public @Nullable LootChest getLootChest(UUID uuid) {
        for (Map.Entry<Chunk, List<LootChest>> entry : chunkChestMap.entrySet()) {
            for (LootChest lootChest : entry.getValue()) {
                if (lootChest.getUuid() == uuid) {
                    return lootChest;
                }
            }
        }
        return null;
    }

    public @Nullable LootChest getLootChest(Location location) {
        List<LootChest> lootchestsList = chunkChestMap.get(location.getChunk());
        if(lootchestsList == null) return null;
        if(lootchestsList.isEmpty()) return null;
        for (LootChest lootChest : lootchestsList) {
            if (lootChest.getLocation() == location) {
                return lootChest;
            }
        }
        return null;
    }

    /**
     * @param location Chunk-location of the chest
     * @return List of chests inside the chunk of the given location
     */
    public @Nullable List<LootChest> getChests(Location location) {
        return getChests(location.getChunk());
    }

    /**
     * @param location Chunk-location of the chest
     * @return List of chests inside the chunk of the given location
     */
    public @Nullable List<LootChest> getChests(Chunk location) {
        return chunkChestMap.get(location);
    }

    /**
     * @param block Block being checked
     * @return whether the block's location matches a lootChest
     */
    public boolean isLootChest(Block block) {
        return isLootChest(block.getLocation());
    }

    /**
     * @param location location of the chest
     * @return whether on the location matches a lootchest
     */
    public boolean isLootChest(Location location) {
        return getLootChest(location) != null;
    }
}
