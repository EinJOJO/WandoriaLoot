package me.einjojo.wandorialoot.loot;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class LootItem implements ConfigurationSerializable {

    private final ItemStack item;
    private final float spawnRate;
    private final int amountMin;
    private final int amountMax;

    public LootItem(ItemStack itemStack) {
        this(itemStack, itemStack.getAmount(), itemStack.getAmount(), 1f);
    }

    public LootItem(ItemStack itemStack, int amountMin, int amountMax, float spawnRate) {
        this.item = itemStack;
        this.amountMax = amountMax;
        this.amountMin = amountMin;
        this.spawnRate = spawnRate;
    }

    public float getSpawnRate() {
        return spawnRate;
    }

    public int getAmountMax() {
        return amountMax;
    }

    public int getAmountMin() {
        return amountMin;
    }

    public ItemStack getItem() {
        return item;
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("item", item.serialize());
        map.put("spawnRate", spawnRate);
        map.put("amountMin", amountMin);
        map.put("amountMax", amountMax);
        return map;
    }

    public static LootItem deserialize(Map<String, Object> map) {
        ItemStack item = ItemStack.deserialize((Map<String, Object>) map.get("item"));
        float spawnRate = Float.parseFloat(map.get("spawnRate").toString());
        int amountMin = (int) map.get("amountMin");
        int amountMax = (int) map.get("amountMax");
        return new LootItem(item, amountMin, amountMax, spawnRate);
    }
}
