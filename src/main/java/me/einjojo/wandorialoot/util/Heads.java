package me.einjojo.wandorialoot.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public enum Heads {
    WHITE_I("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWM5OWRmYjI3MDRlMWJkNmU3ZmFjZmI0M2IzZTZmYmFiYWYxNmViYzdlMWZhYjA3NDE3YTZjNDY0ZTFkIn19fQ=="),
    BLUE_QUESTIONMARK("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzEzZTlhNDZlZDg0YWE0OWFhY2FiOTI2ODM5NDYzMGY5OTU4ODg2ODk5NzljYjQxYzNkYjdiYjJkZGNkZSJ9fX0="),
    COMMON_CHEST("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjdhYWRmZjlkZGM1NDZmZGNlYzZlZDU5MTljYzM5ZGZhOGQwYzA3ZmY0YmM2MTNhMTlmMmU2ZDdmMjU5MyJ9fX0="),
    UNCOMMON_CHEST("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWQ2Zjk5ZjNjNWQ5ODJlYmRlOGRhNjU3ZDA2NTJhYTA4MDY0YmY2ZDZiNDk1ZmRhMjNjNmU0NzEyM2MwOTNlNyJ9fX0="),
    RARE_CHEST  ("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTlkZDc3MTVlNmRjNjQyZTUyZDVjMDJjNjI0YmZhMWE1NmYwNWNkYWJiZTljYTc4OWY1MGVjNjExODk5ZiJ9fX0="),
    MYTHIC_CHEST("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTlkZDc3MTVlNmRjNjQyZTUyZDVjMDJjNjI0YmZhMWE1NmYwNWNkYWJiZTljYTc4OWY1MGVjNjExODk5ZiJ9fX0="),
    LEGENDARY_CHEST("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzIyZDRiZTFhYmNmMzgzMmM5MTYxOTFkMjRmOTYwN2JmMTk0ZWZmOGRmYmYzYjk1MjBiZDk3MjQwZTdjOCJ9fX0="),
    WHITE_ARROW_RIGHT("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTU2YTM2MTg0NTllNDNiMjg3YjIyYjdlMjM1ZWM2OTk1OTQ1NDZjNmZjZDZkYzg0YmZjYTRjZjMwYWI5MzExIn19fQ=="),
    WHITE_ARROW_LEFT("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RjOWU0ZGNmYTQyMjFhMWZhZGMxYjViMmIxMWQ4YmVlYjU3ODc5YWYxYzQyMzYyMTQyYmFlMWVkZDUifX19");
    private final String base64;
    Heads ( String base64 ) {
        this.base64 = base64;
    }

    public ItemStack getSkull() {
        return getSkull(base64);
    }

    public static ItemStack getSkull(String base64) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", base64));

        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(profile.getId()));
        skull.setItemMeta(skullMeta);

        return skull;
    }

}
