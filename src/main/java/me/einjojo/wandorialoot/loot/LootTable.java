package me.einjojo.wandorialoot.loot;

import me.einjojo.wandorialoot.WandoriaLoot;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class LootTable implements ConfigurationSerializable, Comparable<LootTable> {
    private final UUID uuid;
    private List<LootItem> content;
    private String name;
    private Rarity rarity;

    public LootTable(String name) {
        this.name = name;
        this.uuid = UUID.randomUUID();
        this.rarity = Rarity.COMMON;
        this.content = new ArrayList<>();
    }

    public LootTable(UUID uuid, String name, Rarity rarity, List<LootItem> lootItems) {
        this.name = name;
        this.uuid = uuid;
        this.rarity = rarity;
        this.content = lootItems;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public void setContent(List<LootItem> content) {
        this.content = content;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void addLootItem(LootItem item) {
        content.add(item);
    }


    public ItemStack[] generate() {
        return null; //TODO Implement
    }

    public List<LootItem> getContent() {
        return content;
    }

    public UUID getUuid() {
        return uuid;
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        List<Map<String, Object>> serializedLootItems = new ArrayList<>();
        for (LootItem item : content) {
            serializedLootItems.add(item.serialize());
        }

        map.put("id", uuid.toString());
        map.put("name", name);
        map.put("rarity", rarity.name());
        map.put("content", serializedLootItems);
        return map;
    }

    @Nullable
    public static LootTable deserialize(Map<String, Object> map) {
        try {
            UUID id = UUID.fromString(map.get("id").toString());
            String name = map.get("name").toString();
            Rarity rarity = Rarity.valueOf(map.get("rarity").toString());
            ArrayList<LootItem> deserializedLootItems = new ArrayList<>();
            List<Map<String, Object>> serializedLootItems = (List<Map<String, Object>>) map.get("content");
            for (Map<String, Object> entry: serializedLootItems) {
                LootItem lootItem = LootItem.deserialize(entry);
                if (lootItem != null) {
                    deserializedLootItems.add(lootItem);
                }
            }

            return new LootTable(id, name, rarity, deserializedLootItems);
        } catch (Exception e) {
            WandoriaLoot.getInstance().warn("Could not deserialize loot table! Error:" + e.getMessage());
            if (WandoriaLoot.getInstance().isDebug()) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public int compareTo(@NotNull LootTable o) {
        int rarityCompare = this.rarity.compareTo(o.rarity);
        if (rarityCompare != 0) {
            return rarityCompare;
        }
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return "LootTable {" +
                "uuid=" + uuid +
                ", content=" + content.size() +
                ", name='" + name + '\'' +
                ", rarity=" + rarity.toString() +
                '}';
    }
}
