package me.einjojo.wandorialoot.loot;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class LootTable implements ConfigurationSerializable {
    private final UUID uuid;
    private final List<LootItem> content;
    private final String name;
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


    public void addContent(LootItem item){

    }
    public void removeContent(LootItem item){

    }

    public String getName() {
        return name;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public Rarity getRarity() {
        return rarity;
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

        map.put("id", uuid);
        map.put("name", name);
        map.put("rarity", rarity);
        map.put("content", serializedLootItems);
        return map;
    }

    public static LootTable deserialize(Map<String, Object> map) {
        UUID id = UUID.fromString(map.get("id").toString());
        String name = map.get("name").toString();
        Rarity rarity =  Rarity.valueOf(map.get("rarity").toString());
        ArrayList<LootItem> deserializedLootItems = new ArrayList<>();
        List<Map<String, Object>> serializedLootItems = (List<Map<String, Object>>) map.get("content");
        for (Map<String, Object> entry: serializedLootItems) {
            deserializedLootItems.add(LootItem.deserialize(entry));
        }

        return new LootTable(id, name, rarity, deserializedLootItems);
    }

}
