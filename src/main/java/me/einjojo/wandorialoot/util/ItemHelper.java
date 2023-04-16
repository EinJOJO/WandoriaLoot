package me.einjojo.wandorialoot.util;

import me.einjojo.wandorialoot.loot.LootItem;
import me.einjojo.wandorialoot.loot.LootTable;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemHelper extends ItemStack {

    public ItemHelper(ItemStack itemStack) {
        super(itemStack);
    }

    public ItemHelper(Material material) {
        super(material);
    }

    public ItemHelper(Material material, int amount) {
        super(material, amount);
    }


    public ItemHelper setDisplayName(String name) {
        ItemMeta meta = getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        setItemMeta(meta);
        return this;
    }

    public String getDisplayName() {
        ItemMeta meta = getItemMeta();
        assert meta != null;
        return meta.getDisplayName();
    }

    public ItemHelper setLore(String... lore) {
        return setLore(List.of(lore));
    }

    public List<String> getLore() {
        ItemMeta meta = getItemMeta();
        assert meta != null;
        return meta.getLore();
    }

    public ItemHelper setLore(List<String> lore) {
        ItemMeta meta = getItemMeta();
        assert meta != null;
        meta.setLore(lore);
        setItemMeta(meta);
        return this;
    }

    public static ItemHelper lootTableItem(LootTable lootTable) {
        ItemStack itemStack = switch (lootTable.getRarity()) {
            case COMMON -> Heads.COMMON_CHEST.getSkull();
            case UNCOMMON -> Heads.UNCOMMON_CHEST.getSkull();
            case RARE -> Heads.RARE_CHEST.getSkull();
            case MYTHIC -> Heads.MYTHIC_CHEST.getSkull();
            case LEGENDARY -> Heads.LEGENDARY_CHEST.getSkull();
        };
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§7§oName: §f" + lootTable.getName());
        lore.add("§7§oTyp: §b" + lootTable.getRarity().getColor() + lootTable.getRarity().name().toLowerCase());
        lore.add("§7§oItems: §f" + lootTable.getContent().size());


        return new ItemHelper(itemStack).setDisplayName(lootTable.getRarity().getColor() + lootTable.getName()).setLore(lore);
    }

    public static ItemHelper LootItemItem(LootItem item) {
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§f§oID: §d" + item.getID());
        lore.add("§f§oSpawn Chance: §d" + item.getSpawnRate() * 100 + "%");
        lore.add("§f");
        lore.add("§f§oMin Amount: §3" + item.getAmountMin());
        lore.add("§f§oMax Amount: §c" + item.getAmountMax());
        return new ItemHelper(item.getItem()).setLore(lore);
    }




}
