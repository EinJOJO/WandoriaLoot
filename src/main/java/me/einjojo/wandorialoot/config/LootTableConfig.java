package me.einjojo.wandorialoot.config;

import me.einjojo.joslibrary.JoPlugin;
import me.einjojo.joslibrary.config.ConfigurationFile;
import me.einjojo.wandorialoot.loot.LootTable;

import java.util.*;
import java.util.stream.Collectors;

public class LootTableConfig extends ConfigurationFile {
    private final JoPlugin plugin;
    public LootTableConfig(JoPlugin plugin) {
        super(plugin, "loottables.yml"); this.plugin = plugin;
    }

    public void writeConfig(List<LootTable> lootTable) {
        try {
            getFile().set("loottables", lootTable.stream().map(LootTable::serialize).collect(Collectors.toList()));
            saveFile();
        } catch (Exception e) {
            plugin.error("Failed to save LootTables");
            e.printStackTrace();
        }

    }

    public List<LootTable> readConfig() {
        List<Map<String, Object>> serialized = (List<Map<String, Object>>) getFile().getList("loottables");
        List<LootTable> table =  serialized == null ? Collections.emptyList() :
                serialized.stream()
                        .map(LootTable::deserialize)
                        .filter(Objects::nonNull)
                        .toList();
        plugin.info("Loaded " + table.size() + " LootTables");
        return table;
    }

}
