package me.einjojo.wandorialoot.config;

import me.einjojo.joslibrary.JoPlugin;
import me.einjojo.joslibrary.config.ConfigurationFile;
import me.einjojo.wandorialoot.loot.LootTable;

import java.util.Set;

public class LootTableConfig extends ConfigurationFile {

    public LootTableConfig(JoPlugin plugin) {
        super(plugin, "loottables.yml");
    }

    public void writeConfig(Set<LootTable> lootTable) {
        saveFile();
    }

    public Set<LootTable> readConfig() {
        loadFile();

        return null;
    }
}
