package me.einjojo.wandorialoot.loot;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LootItem implements ConfigurationSerializable {
    public static AtomicInteger atomicInteger = new AtomicInteger(0);
    private final int id;
    private final ItemStack item;
    private float spawnRate;
    private int amountMin;
    private int amountMax;

    public LootItem(LootItem item) {
        this(item.getID(), item.getItem(), item.getAmountMin(), item.getAmountMax(), item.getSpawnRate());
    }

    public LootItem(ItemStack itemStack) {
        this(atomicInteger.getAndIncrement(), itemStack, itemStack.getAmount(), itemStack.getAmount(), 1f);
    }

    public LootItem(int id, ItemStack itemStack, int amountMin, int amountMax, float spawnRate) {
        this.id = id;
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

    public int getID() {
        return id;
    }

    public void setAmountMax(int amountMax) {
        this.amountMax = amountMax;
    }

    public void setAmountMin(int amountMin) {
        this.amountMin = amountMin;
        getItem().setAmount(amountMin);
    }

    public void setSpawnRate(float spawnRate) {
        this.spawnRate = spawnRate;
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
        if (item.getType() == Material.AIR) return null;
        float spawnRate = Float.parseFloat(map.get("spawnRate").toString());
        int amountMin = (int) map.get("amountMin");
        int amountMax = (int) map.get("amountMax");
        return new LootItem(atomicInteger.getAndIncrement(), item, amountMin, amountMax, spawnRate);
    }
}
