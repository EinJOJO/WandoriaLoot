package me.einjojo.wandorialoot.util;

import me.einjojo.wandorialoot.loot.LootItem;
import me.einjojo.wandorialoot.loot.LootTable;
import me.einjojo.wandorialoot.loot.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        Rarity rarity = lootTable.getRarity();
        ItemStack itemStack = switch (rarity) {
            case COMMON -> Heads.COMMON_CHEST.getSkull();
            case UNCOMMON -> Heads.UNCOMMON_CHEST.getSkull();
            case RARE -> Heads.RARE_CHEST.getSkull();
            case MYTHIC -> Heads.MYTHIC_CHEST.getSkull();
            case LEGENDARY -> Heads.LEGENDARY_CHEST.getSkull();
        };
        ArrayList<String> lore = new ArrayList<>();
        ChatColor color = rarity.getColor();
        lore.add(ChatColor.ITALIC + (ChatColor.GRAY + "Name: ") + color + lootTable.getName());
        lore.add(ChatColor.ITALIC + (ChatColor.GRAY + "Typ: ") + color + lootTable.getRarity().name().toLowerCase());
        lore.add(ChatColor.ITALIC + (ChatColor.GRAY + "Items: ") + color + lootTable.getContent().size());

        //save LootTable UUID to persistent data container
        Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer()
                .set(NamespacedKey.fromString("wandorialoot:loot_table"), PersistentDataType.STRING, lootTable.getUuid().toString());

        return new ItemHelper(itemStack).setDisplayName(color + lootTable.getName()).setLore(lore);
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
