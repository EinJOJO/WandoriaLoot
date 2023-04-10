package me.einjojo.wandorialoot.config;

import me.einjojo.joslibrary.JoPlugin;
import me.einjojo.joslibrary.config.ConfigurationFile;
import me.einjojo.wandorialoot.chest.LootChest;

import java.util.*;
import java.util.stream.Collectors;

public class LootChestConfig extends ConfigurationFile {

    private final JoPlugin plugin;
    public LootChestConfig(JoPlugin plugin) {
        super(plugin, "lootchests.yml");
        this.plugin = plugin;
    }

    public List<LootChest> readConfig() {
        List<Map<String, Object>> configMap = (List<Map<String, Object>>) getFile().getList("chests");
        List<LootChest> lootChests = configMap == null ? new ArrayList<>() :
                configMap.stream()
                        .map(LootChest::deserialize)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
        plugin.debug(String.format("Loaded %d LootChests", lootChests.size()));
        return lootChests;
    }


    public void saveConfig(List<LootChest> lootChests) {
        getFile().set("chests", lootChests.stream().map(LootChest::serialize).collect(Collectors.toList()));
        plugin.debug(String.format("Saved %d LootChests", lootChests.size()));
        saveFile();
    }
}
