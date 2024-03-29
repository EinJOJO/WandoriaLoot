package me.einjojo.wandorialoot.config;

import me.einjojo.joslibrary.JoPlugin;
import me.einjojo.joslibrary.config.ConfigurationFile;
import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.chest.LootChest;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PlayersConfig extends ConfigurationFile {
//TODO: Refactor, this is a mess

    WandoriaLoot plugin;
    public PlayersConfig(JoPlugin plugin) {
        super(plugin, "players.yml");
        loadFile();
        this.plugin = (WandoriaLoot) plugin;
    }

    public Map<UUID, Set<LootChest>> readConfig() {
        Map<UUID, Set<LootChest>> playerChestMap = new HashMap<>();
        Set<String> players = getFile().getKeys(false);
        for (String player : players) {
            UUID playerUUID = UUID.fromString(player);
            List<Object> chestUIDs = (List<Object>) getFile().getList(player);
            HashSet<LootChest> playersOpenedChests = new HashSet<>();
            for (Object uid: chestUIDs) {
                UUID uuid1 = UUID.fromString(String.valueOf(uid));
                LootChest lootChest = plugin.getLootChestManager().getLootChest(uuid1);
                if (lootChest != null) {
                    playersOpenedChests.add(lootChest);
                }
            }
            plugin.debug(String.format("Found %d chests for player %s", playersOpenedChests.size(), player));
            playerChestMap.put(playerUUID, playersOpenedChests);
        }
        return playerChestMap;
    }

    public void saveConfig(Map<UUID, Set<LootChest>> playerChestMap) {
        try {
            for (Map.Entry<UUID, Set<LootChest>> entry : playerChestMap.entrySet()) {
                List<String> uuidChests = new ArrayList<>();
                for (LootChest lc : entry.getValue()) {
                    uuidChests.add(lc.getUuid().toString());
                }
                getFile().set(entry.getKey().toString(), uuidChests);
                plugin.debug(String.format("Saved %d LootChests for player %s", uuidChests.size(), entry.getKey().toString()));

            }
            saveFile();
        } catch (Exception e) {
            plugin.error("Failed to save PlayerConfig");
            e.printStackTrace();
        }

    }

}
