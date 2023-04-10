package me.einjojo.wandorialoot.config;

import me.einjojo.joslibrary.JoPlugin;
import me.einjojo.joslibrary.config.ConfigurationFile;
import me.einjojo.wandorialoot.loot.LootTable;

import java.util.*;

public class LootTableConfig extends ConfigurationFile {

    public LootTableConfig(JoPlugin plugin) {
        super(plugin, "loottables.yml");
    }

    public void writeConfig(Set<LootTable> lootTable) {
        List<Map<String,Object>> serialized = new ArrayList<>();
        for (LootTable lt : lootTable) {
            serialized.add(lt.serialize());
        }
        getFile().set("loottables", serialized);
        saveFile();
    }

    public Set<LootTable> readConfig() {
        Set<LootTable> lootTables = new HashSet<>();
        List<Map<String, Object>> serialized = (List<Map<String, Object>>) getFile().getList("loottables");
        if (serialized == null) return lootTables;
        for (Map<String, Object> entry : serialized) {
            lootTables.add(LootTable.deserialize(entry));
        }
        return lootTables;
    }
}
