package me.einjojo.wandorialoot.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import me.einjojo.joslibrary.util.ItemBuilder;
import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.gui.table.LootTableGui;
import me.einjojo.wandorialoot.loot.LootItem;
import me.einjojo.wandorialoot.util.Heads;
import me.einjojo.wandorialoot.util.ItemHelper;
import me.einjojo.wandorialoot.util.PlayerChatInput;
import me.einjojo.wandorialoot.util.Sounds;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LootItemConfigurator extends ChestGui {

    private final LootItem lootItem;
    private boolean inputMode;
    private int max;
    private int min;
    private float spawnRate;
    StaticPane cp;
    private boolean saved;

    public LootItemConfigurator(LootItem lootItem, LootTableGui returnPoint) {
        super(4, "LootItem einstellen");
        this.lootItem = lootItem;
        this.max = lootItem.getAmountMax();
        this.min = lootItem.getAmountMin();
        this.spawnRate = lootItem.getSpawnRate();
        setSaved(true);
        setOnTopClick(e -> e.setCancelled(true));
        setOnClose((e) -> {
            if (inputMode) return;
            returnPoint.returnToView(e.getPlayer());
            if (saved) {
                return;
            }
            e.getPlayer().sendMessage("§cAbgebrochen");
            Sounds.CANCEL.play(e.getPlayer());
        });
        addPanes();
        update();
    }

    public void setInputMode(boolean inputMode) {
        this.inputMode = inputMode;
        if (inputMode) {
            getViewers().forEach((HumanEntity::closeInventory));
        }
    }

    public void addPanes() {
        addPane(background());
        addPane(item());
        addPane(control());
    }

    public OutlinePane item() {
        OutlinePane pane = new OutlinePane(4, 1, 1, 1);

        pane.addItem(new GuiItem(new ItemBuilder(lootItem.getItem()).setLore(ItemHelper.getLootItemLore(lootItem)).build()));
        pane.setRepeat(true);
        pane.setPriority(Pane.Priority.LOW);
        return pane;
    }

    public void afterError(HumanEntity humanEntity) {
        show(humanEntity);
        Sounds.ERROR.play(humanEntity);
        setInputMode(false);
    }

    public StaticPane control() {
        if (cp == null) cp = new StaticPane(0, 2, 9, 1);
        cp.clear();
        cp.addItem(new GuiItem(new ItemBuilder(Heads.BARRIER.getSkull()).setName("§cAbbrechen").build(), (e) -> e.getWhoClicked().closeInventory()), Slot.fromIndex(1));
        cp.addItem(new GuiItem(new ItemBuilder(Heads.MINUS.getSkull()).setName("§3Minimum ändern").addLore("§7Aktuell: §3" + min).build(), (e -> {
            setInputMode(true);
            new PlayerChatInput(WandoriaLoot.getInstance(), (Player) e.getWhoClicked(), "§3Minimum §7eingeben", (input -> {
                if (input == null) return;
                try {
                    int i = Integer.parseInt(input);
                    if (i < 1) {
                        throw new IllegalArgumentException("§cDie Zahl muss größer als 0 sein");
                    }
                    if (i > max) {
                        max = min;
                    }

                    min = i;
                    control();
                    update();
                    show(e.getWhoClicked());
                    setSaved(false);
                } catch (NumberFormatException ex) {
                    e.getWhoClicked().sendMessage("§cBitte gebe eine Zahl ein");
                    afterError(e.getWhoClicked());
                } catch (Exception ex) {
                    e.getWhoClicked().sendMessage(ex.getMessage());
                    afterError(e.getWhoClicked());
                }
                setInputMode(false);
            }));
        })), Slot.fromIndex(3));
        cp.addItem(new GuiItem(new ItemBuilder(Heads.PLUS.getSkull()).setName("§cMaximum ändern").addLore("§7Aktuell: §c" + max).build(), (e -> {
            setInputMode(true);
            new PlayerChatInput(WandoriaLoot.getInstance(), (Player) e.getWhoClicked(), "§cMaximum §7eingeben", (input -> {
                if (input == null) {
                    afterError(e.getWhoClicked());
                    return;
                }
                try {
                    int i = Integer.parseInt(input);
                    if (i < 1) {
                        throw new IllegalArgumentException("§cDie Zahl muss größer als 0 sein");
                    }
                    if (i < min) {
                        throw new IllegalArgumentException("§cDie Zahl muss größer als das Minimum sein");
                    }
                    max = i;
                    control();
                    update();
                    show(e.getWhoClicked());
                    setSaved(false);
                } catch (NumberFormatException ex) {
                    e.getWhoClicked().sendMessage("§cBitte gebe eine Zahl ein");
                    afterError(e.getWhoClicked());
                } catch (IllegalArgumentException ex) {
                    e.getWhoClicked().sendMessage(ex.getMessage());
                    afterError(e.getWhoClicked());
                }
                setInputMode(false);
            }));
        })), Slot.fromIndex(4));
        cp.addItem(new GuiItem(new ItemBuilder(Heads.PERCENT.getSkull()).setName("§fSpawnrate ändern").addLore("§7Aktuell: §f" + spawnRate * 100 + "%").build(), (e -> {
            setInputMode(true);
            new PlayerChatInput(WandoriaLoot.getInstance(), (Player) e.getWhoClicked(), "§fSpawnrate §7eingeben", (input -> {
                if (input == null) {
                    afterError(e.getWhoClicked());
                    return;
                }
                try {
                    float i = Float.parseFloat(input);
                    if (i < 0) {
                        throw new IllegalArgumentException("§cDie Zahl muss größer als 0 sein");
                    }
                    if (i > 100) {
                        throw new IllegalArgumentException("§cDie Zahl muss kleiner als 100 sein");
                    }
                    spawnRate = i / 100f;
                    show(e.getWhoClicked());
                    control();
                    setSaved(false);
                    update();
                } catch (NumberFormatException ex) {
                    e.getWhoClicked().sendMessage("§cBitte gebe eine Zahl ein");
                    afterError(e.getWhoClicked());
                } catch (IllegalArgumentException ex) {
                    e.getWhoClicked().sendMessage(ex.getMessage());
                    afterError(e.getWhoClicked());
                }
                setInputMode(false);
            }));
        })), Slot.fromIndex(5));
        cp.addItem(new GuiItem(new ItemBuilder(Heads.GREEN_CHECK.getSkull()).setName("§aSpeichern").setLore(List.of(
                String.format("§7Min: §d%d", min),
                String.format("§7Max: §d%d", max),
                String.format("Spawnrate: §d%f", spawnRate)
        )).build(), (e) -> {
            lootItem.setAmountMax(max);
            lootItem.setAmountMin(min);
            lootItem.setSpawnRate(spawnRate);
            setSaved(true);
            e.getWhoClicked().sendMessage("§aGespeichert");
            Sounds.GUI_CLICK.play(e.getWhoClicked());
            e.getWhoClicked().closeInventory();
        }), Slot.fromIndex(7));
        return cp;
    }

    public OutlinePane background() {
        OutlinePane pane = new OutlinePane(0, 0, 9, 4);
        pane.addItem(new GuiItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§8").build()));
        pane.setRepeat(true);
        pane.setPriority(Pane.Priority.LOWEST);
        return pane;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
        update();
    }
}
