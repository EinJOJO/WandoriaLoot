package me.einjojo.wandorialoot.chest;

import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.command.SetupCommand;
import me.einjojo.wandorialoot.config.LootChestConfig;
import me.einjojo.wandorialoot.config.PlayersConfig;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class LootChestManager {

    private final WandoriaLoot plugin;
    private final Map<String, Set<LootChest>> chunkChestMap = new HashMap<>(); // chunk-string pointing to set of LootChests inside that chunk
    private Map<UUID, Set<LootChest>> playerChestMap = new HashMap<>(); //player pointing to discovered chests
    private final PlayersConfig playersConfig;
    private final LootChestConfig lootChestConfig;

    public LootChestManager(WandoriaLoot plugin) {
        this.plugin = plugin;
        lootChestConfig = new LootChestConfig(plugin);
        playersConfig = new PlayersConfig(plugin);

    }

    public void openLootChest(LootChest lootChest, Player player) {
        lootChest.openChest(player);
        lootChest.openInventory(player);
        setChestDiscovered(player, lootChest, true);
    }

    public void closeLootChest(LootChest lootChest, Player player) {
        lootChest.closeChest(player);
        lootChest.destroyChest(player);
    }

    public boolean isChestDiscovered(Player player, LootChest lootChest) {
        if (playerChestMap.get(player.getUniqueId()) == null) return false;
        return playerChestMap.get(player.getUniqueId()).contains(lootChest);
    }

    public void setChestDiscovered(Player player, LootChest lootChest, boolean discovered) {
        if (SetupCommand.setUpPlayer.contains(player.getUniqueId())) return; // don't set chest discovered if player is in setup mode
        if (discovered) {
            playerChestMap.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>()).add(lootChest);
            plugin.debug(String.format("%s has discovered %s", player.getName(), lootChest.toString()));
        } else {
            playerChestMap.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>()).remove(lootChest);
            plugin.debug(String.format("%s has undiscovered %s", player.getName(), lootChest.toString()));
        }

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
        for (Set<LootChest> chunkChests : chunkChestMap.values()) {
            allChests.addAll(chunkChests);
        }
        lootChestConfig.saveConfig(allChests);
        playersConfig.saveConfig(playerChestMap);
    }


    /**
     * Adds a lootChest
     * @param lootChest LootChest that should be registered inside the ChunkMap
     */
    public boolean addChest(LootChest lootChest) {
        boolean exists = isLootChest(lootChest.getLocation());
        if (exists) {
            return false;
        }
        String lChunk = parseChunk(lootChest.getLocation().getChunk());
        chunkChestMap.computeIfAbsent(lChunk, k -> new HashSet<>()).add(lootChest);
        plugin.debug(String.format("Added LootChest %s", lootChest));
        return true;
    }

    /**
     *
     * @param uuid UUID of the LootChest
     * @return a LootChest object
     */
    public @Nullable LootChest getLootChest(UUID uuid) {
        for (Map.Entry<String, Set<LootChest>> entry : chunkChestMap.entrySet()) {
            for (LootChest lootChest : entry.getValue()) {
                if (lootChest.getUuid().equals(uuid)) {
                    return lootChest;
                }
            }
        }
        return null;
    }

    public @Nullable LootChest getLootChest(Location location) {
        Set<LootChest> lootChestsList = chunkChestMap.get(parseChunk(location.getChunk()));
        if(lootChestsList == null) return null;
        if(lootChestsList.isEmpty()) return null;
        for (LootChest lootChest : lootChestsList) {
            if (lootChest.getLocation().equals(location)) {
                return lootChest;
            }
        }
        return null;
    }


    /**
     * @param location Chunk-location of the chest
     * @return Set of chests inside the chunk of the given location
     */
    public @Nullable Set<LootChest> getChests(Chunk location) {
        Set<LootChest> a = chunkChestMap.get(parseChunk(location));
        if (a != null) {
            plugin.debug("hit!");
        }
        return a;
    }

    /**
     * @param location location of the chest
     * @return whether on the location matches a LootChest
     */
    public boolean isLootChest(Location location) {
        return getLootChest(location) != null;
    }

    public String parseChunk(Chunk chunk) {
        return chunk.getX() + "|" + chunk.getZ();
    }


}
