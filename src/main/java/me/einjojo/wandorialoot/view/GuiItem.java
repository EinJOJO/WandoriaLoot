package me.einjojo.wandorialoot.view;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiItem extends ItemStack {
    private String displayName = "";
    private String leftClickActionDescription = "";
    private String rightClickActionDescription = "";

    private List<String> baseLore = new ArrayList<>();
    private Runnable rightClickAction;
    private Runnable leftClickAction;

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

    public void build() {
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
        return this;
    }

    public GuiItem setRightClickAction(Runnable rightClickAction, String description) {
        this.rightClickAction = rightClickAction;
        this.rightClickActionDescription = description;
        return this;
    }

    public GuiItem setLeftClickAction(Runnable leftClickAction, String description) {
        this.leftClickAction = leftClickAction;
        this.leftClickActionDescription = description;
        return this;
    }

    public List<String> getBaseLore() {
        return baseLore;
    }

    public Runnable getLeftClickAction() {
        return leftClickAction;
    }

    public Runnable getRightClickAction() {
        return rightClickAction;
    }
}
