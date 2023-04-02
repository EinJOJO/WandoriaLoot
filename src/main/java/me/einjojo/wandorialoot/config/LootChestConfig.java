package me.einjojo.wandorialoot.config;

import me.einjojo.joslibrary.JoPlugin;
import me.einjojo.joslibrary.config.ConfigurationFile;
import me.einjojo.wandorialoot.chest.LootChest;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class LootChestConfig extends ConfigurationFile {

    private final JoPlugin plugin;
    public LootChestConfig(JoPlugin plugin) {
        super(plugin, "lootchests.yml");
        this.plugin = plugin;

    }

    public List<LootChest> readConfig() {
        List<LootChest> lootChests = new ArrayList<>();
        Set<String> keys = getFile().getKeys(false);
        for (String key: keys) {
            ConfigurationSection section = getFile().getConfigurationSection(key);
            if (section != null) {
                Location location = Location.deserialize(section.getConfigurationSection("loc").getValues(false));
                LootChest chest = new LootChest(UUID.fromString(key), location, null, null);
                lootChests.add(chest);

                plugin.debug(String.format("Read Lootchest#%s -> %f, %f, %f", key, location.getX(), location.getY(), location.getZ()));
            }

        }
        return lootChests;
    }

    public void saveConfig(List<LootChest> lootChests) {
        for (LootChest lc : lootChests) {
            String uuid = lc.getUuid().toString();
            getFile().set(uuid,lc.serialize());
        }
        saveFile();
    }
}
