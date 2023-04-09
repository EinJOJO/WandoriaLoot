package me.einjojo.wandorialoot.config;

import me.einjojo.joslibrary.JoPlugin;
import me.einjojo.joslibrary.config.ConfigurationFile;
import me.einjojo.wandorialoot.chest.LootChest;
import me.einjojo.wandorialoot.loot.LootItem;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.*;

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
                UUID uuid = UUID.fromString(key);
                Location location = Location.deserialize(section.getConfigurationSection("loc").getValues(false));
                List<Object> temporaryList = (List<Object>) section.getList("lootTable", new ArrayList<Object>());
                LootItem[] lootItems = new LootItem[temporaryList.size()];
                for (int i = 0; i < temporaryList.size(); i++) {
                    lootItems[i] = LootItem.deserialize((Map<String, Object>) temporaryList.get(i));
                }
                List<Object> tempItemList = (List<Object>) section.getList("content", new ArrayList<Object>());
                ItemStack[] content = new ItemStack[tempItemList.size()];
                for (int i = 0; i < tempItemList.size(); i++) {
                    content[i] = ItemStack.deserialize((Map<String, Object>) tempItemList.get(i));
                }
                LootChest lootChest = new LootChest(uuid, location, content, lootItems);
                lootChests.add(lootChest);
            }

        }
        plugin.debug(String.format("Loaded %d LootChests", lootChests.size()));
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
