package me.einjojo.wandorialoot.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TestGeneration extends ChestGui {
    public TestGeneration(ChestGui gui, ItemStack[] content) {
        super(3, "Test Generaton");
        setOnClose((e) -> {
            gui.show(e.getPlayer());
        });
        addPane(createPane(content));

    }

    public StaticPane createPane(ItemStack[] content) {
        StaticPane pane = new StaticPane(0, 0, 9, 3);
        for (int i = 0; i < content.length; i++) {
            if (content[i] == null) continue;

            pane.addItem(new GuiItem(content[i]), Slot.fromIndex(i));
        }
        return pane;
    }

}
