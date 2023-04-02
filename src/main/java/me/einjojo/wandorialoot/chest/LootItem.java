package me.einjojo.wandorialoot.chest;

import org.bukkit.inventory.ItemStack;

public class LootItem {

    private ItemStack item;
    private float spawnRate;
    private int amountMin;
    private int amountMax;

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
}
