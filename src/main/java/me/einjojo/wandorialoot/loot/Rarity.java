package me.einjojo.wandorialoot.loot;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;

public enum Rarity {

    COMMON(Material.WHITE_STAINED_GLASS_PANE, ChatColor.WHITE), //gewöhnlich
    UNCOMMON(Material.GREEN_STAINED_GLASS_PANE, ChatColor.GREEN), //selten
    RARE(Material.BLUE_STAINED_GLASS_PANE, ChatColor.BLUE ), //sehr selten
    MYTHIC(Material.PURPLE_STAINED_GLASS_PANE, ChatColor.DARK_PURPLE), //mythisch
    LEGENDARY(Material.ORANGE_STAINED_GLASS_PANE, ChatColor.YELLOW); //legendär

    private final Material guiMaterial;
    private final ChatColor color;
    Rarity(Material guiMaterial, ChatColor color) {
        this.guiMaterial = guiMaterial;
        this.color = color;
    }

    public Material getGuiMaterial() {
        return guiMaterial;
    }

    public ChatColor getColor() {
        return color;
    }
}
