package me.einjojo.wandorialoot.loot;

import org.bukkit.Color;
import org.bukkit.Material;

public enum Rarity {

    COMMON(Material.WHITE_STAINED_GLASS_PANE, Color.WHITE), //gewöhnlich
    UNCOMMON(Material.GREEN_STAINED_GLASS_PANE, Color.GREEN), //selten
    RARE(Material.BLUE_STAINED_GLASS_PANE, Color.BLUE ), //sehr selten
    MYTHIC(Material.PURPLE_STAINED_GLASS_PANE, Color.PURPLE), //mythisch
    LEGENDARY(Material.ORANGE_STAINED_GLASS_PANE, Color.ORANGE); //legendär

    private final Material guiMaterial;
    private final Color color;
    Rarity(Material guiMaterial, Color color) {
        this.guiMaterial = guiMaterial;
        this.color = color;
    }

    public Material getGuiMaterial() {
        return guiMaterial;
    }

    public Color getColor() {
        return color;
    }
}
