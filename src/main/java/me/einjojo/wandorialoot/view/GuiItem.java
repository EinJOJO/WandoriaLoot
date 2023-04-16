package me.einjojo.wandorialoot.view;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiItem extends ItemStack {

    private String displayName = "§7";
    private String leftClickActionDescription = "";
    private String rightClickActionDescription = "";

    private List<String> baseLore = new ArrayList<>();
    private ClickAction rightClickAction;
    private ClickAction leftClickAction;

    public GuiItem(Material material) {
        super(material);
        build();
    }

    public GuiItem(Material material, String displayName) {
        super(material);
        this.displayName = displayName;
        build();
    }

    public GuiItem(Material material, String displayName, String... baseLore) {
        super(material);
        this.displayName = displayName;
        this.baseLore.addAll(Arrays.asList(baseLore));
        build();
    }

    private void build() {
        ItemMeta meta = this.getItemMeta();
        if (meta == null) { return; }
        List<String> lore = new ArrayList<>(baseLore);
        if (rightClickAction != null) {
            lore.add("§eRight-Click §7to " + rightClickActionDescription);
        }
        if (rightClickAction != null) {
            lore.add("§6Left-Click §7to " + leftClickActionDescription);
        }
        meta.setLore(lore);
        meta.setDisplayName(displayName);
        setItemMeta(meta);

    }



    public GuiItem setBaseLore(List<String> baseLore) {
        this.baseLore = baseLore;
        build();
        return this;
    }

    public GuiItem setRightClickAction(String description) {
        this.rightClickActionDescription = description;
        build();
        return this;
    }


    public GuiItem setLeftClickAction(String description) {
        this.leftClickActionDescription = description;
        build();
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public GuiItem setDisplayName(String displayName) {
        this.displayName = displayName;
        build();
        return this;
    }


    public List<String> getBaseLore() {
        return baseLore;
    }


    @FunctionalInterface
    public interface ClickAction {
        void run(InventoryClickEvent event);
    }
}
