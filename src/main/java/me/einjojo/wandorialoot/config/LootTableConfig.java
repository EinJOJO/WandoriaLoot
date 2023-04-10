package me.einjojo.wandorialoot.config;

import me.einjojo.joslibrary.JoPlugin;
import me.einjojo.joslibrary.config.ConfigurationFile;
import me.einjojo.wandorialoot.loot.LootTable;

import java.util.*;
import java.util.stream.Collectors;

public class LootTableConfig extends ConfigurationFile {

    public LootTableConfig(JoPlugin plugin) {
        super(plugin, "loottables.yml");
    }

    public void writeConfig(Set<LootTable> lootTable) {
        getFile().set("loottables", lootTable.stream().map(LootTable::serialize).collect(Collectors.toList()));
        saveFile();
    }

    public Set<LootTable> readConfig() {
        List<Map<String, Object>> serialized = (List<Map<String, Object>>) getFile().getList("loottables");
        return serialized == null ? Collections.emptySet() :
                serialized.stream()
                        .map(LootTable::deserialize)
                        .collect(Collectors.toSet());
    }

}
