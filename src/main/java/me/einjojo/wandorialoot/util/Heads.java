package me.einjojo.wandorialoot.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public enum Heads {
    WHITE_I("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWM5OWRmYjI3MDRlMWJkNmU3ZmFjZmI0M2IzZTZmYmFiYWYxNmViYzdlMWZhYjA3NDE3YTZjNDY0ZTFkIn19fQ=="),
    BLUE_QUESTIONMARK("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzEzZTlhNDZlZDg0YWE0OWFhY2FiOTI2ODM5NDYzMGY5OTU4ODg2ODk5NzljYjQxYzNkYjdiYjJkZGNkZSJ9fX0="),
    COMMON_CHEST("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjdhYWRmZjlkZGM1NDZmZGNlYzZlZDU5MTljYzM5ZGZhOGQwYzA3ZmY0YmM2MTNhMTlmMmU2ZDdmMjU5MyJ9fX0="),
    UNCOMMON_CHEST("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWQ2Zjk5ZjNjNWQ5ODJlYmRlOGRhNjU3ZDA2NTJhYTA4MDY0YmY2ZDZiNDk1ZmRhMjNjNmU0NzEyM2MwOTNlNyJ9fX0="),
    RARE_CHEST  ("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTlkZDc3MTVlNmRjNjQyZTUyZDVjMDJjNjI0YmZhMWE1NmYwNWNkYWJiZTljYTc4OWY1MGVjNjExODk5ZiJ9fX0="),
    MYTHIC_CHEST("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTlkZDc3MTVlNmRjNjQyZTUyZDVjMDJjNjI0YmZhMWE1NmYwNWNkYWJiZTljYTc4OWY1MGVjNjExODk5ZiJ9fX0="),
    LEGENDARY_CHEST("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzIyZDRiZTFhYmNmMzgzMmM5MTYxOTFkMjRmOTYwN2JmMTk0ZWZmOGRmYmYzYjk1MjBiZDk3MjQwZTdjOCJ9fX0="),
    GREEN_CHECK("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTc5YTVjOTVlZTE3YWJmZWY0NWM4ZGMyMjQxODk5NjQ5NDRkNTYwZjE5YTQ0ZjE5ZjhhNDZhZWYzZmVlNDc1NiJ9fX0="),
    BARRIER("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VkMWFiYTczZjYzOWY0YmM0MmJkNDgxOTZjNzE1MTk3YmUyNzEyYzNiOTYyYzk3ZWJmOWU5ZWQ4ZWZhMDI1In19fQ=="),
    MINUS("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY3ODM2ZjA0N2Q3NDFhYzRmNzM4NWY3YzNkYzQ4YmE4ZGY0MzczMmU1YWVlYzMzMjY4MjlhNzI4NDczNyJ9fX0="),
    PLUS("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWM3MzFjM2M3MjNmNjdkMmNmYjFhMTE5MmI5NDcwODZmYmEzMmFlYTQ3MmQzNDdhNWVkNWQ3NjQyZjczYiJ9fX0="),
    PERCENT("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTlmMjdkNTRlYzU1NTJjMmVkOGY4ZTE5MTdlOGEyMWNiOTg4MTRjYmI0YmMzNjQzYzJmNTYxZjllMWU2OWYifX19"),
    WHITE_ARROW_RIGHT("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTU2YTM2MTg0NTllNDNiMjg3YjIyYjdlMjM1ZWM2OTk1OTQ1NDZjNmZjZDZkYzg0YmZjYTRjZjMwYWI5MzExIn19fQ=="),
    WHITE_ARROW_LEFT("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RjOWU0ZGNmYTQyMjFhMWZhZGMxYjViMmIxMWQ4YmVlYjU3ODc5YWYxYzQyMzYyMTQyYmFlMWVkZDUifX19");

    private final String base64;
    Heads ( String base64 ) {
        this.base64 = base64;
    }

    public ItemStack getSkull() {
        return getSkull(base64);
    }

    public static ItemStack getSkull(String value) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);


        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put("textures", new Property("textures", value));

        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, gameProfile);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }


        item.setItemMeta(skullMeta);
        return item;
    }

}
