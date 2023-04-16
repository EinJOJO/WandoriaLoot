package me.einjojo.wandorialoot.loot;

import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.config.LootTableConfig;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LootManager {

    //TODO: Implement LootManager
    private final Map<UUID, LootTable> lootTables = new HashMap<>();
    private final LootTableConfig lootTableConfig;
    private final WandoriaLoot plugin;

    public LootManager(WandoriaLoot plugin){
        this.lootTableConfig = new LootTableConfig(plugin);
        this.plugin = plugin;
    }

    public void loadLootTables() {
        lootTables.clear();
        lootTables.putAll(lootTableConfig.readConfig().stream().collect(HashMap::new, (map, lootTable) -> map.put(lootTable.getUuid(), lootTable), HashMap::putAll));
    }
    public void saveLootTables() {
        lootTableConfig.writeConfig(lootTables.values().stream().toList());
    }

    /**
     * Get a loot table by its UUID
     * @param uuid The UUID of the loot table
     * @return The loot table or null if it doesn't exist
     */
    @Nullable
    public LootTable getLootTable(UUID uuid) {
        return lootTables.get(uuid);
    }

    @Nullable
    public LootTable getLootTable(String name) {
        return lootTables.values().stream().filter(lootTable -> lootTable.getName().equals(name))
                .findFirst().orElse(null);
    }

    /**
     * Add a loot table to the manager
     * @param lootTable The loot table to add
     */
    public void addLootTable(LootTable lootTable) {
        lootTables.put(lootTable.getUuid(), lootTable);
        plugin.debug("Added loottable: " + lootTable);

    }

    /**
     * Remove a loot table from the manager
     * @param lootTable
     */
    public void removeLootTable(LootTable lootTable) {
        removeLootTable(lootTable.getUuid());

    }

    /**
     * Remove a loot table from the manager
     * @param uuid
     */
    public void removeLootTable(UUID uuid) {
        lootTables.remove(uuid);
        plugin.debug("Removed loottable: " + uuid);
    }

    public List<LootTable> getLootTables() {
        return List.copyOf(lootTables.values());
    }
}
