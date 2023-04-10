package me.einjojo.wandorialoot.loot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LootManager {

    //TODO: Implement LootManager
    private final Map<UUID, LootTable> lootTables = new HashMap<>();

    public LootManager(){}

    public LootTable getLootTable(UUID uuid) {
        return lootTables.get(uuid);
    }
}
