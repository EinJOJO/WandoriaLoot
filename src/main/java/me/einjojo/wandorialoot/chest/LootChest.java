package me.einjojo.wandorialoot.chest;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class LootChest  {
    private Location location;
    private ItemStack[] content;
    private LootItem[] lootTable;

    public LootItem[] getLootTable() {
        return lootTable;
    }

    public ItemStack[] getContent() {
        return content;
    }

    public Location getLocation() {
        return location;
    }

    public void setContent(ItemStack[] content) {
        this.content = content;
    }
}

