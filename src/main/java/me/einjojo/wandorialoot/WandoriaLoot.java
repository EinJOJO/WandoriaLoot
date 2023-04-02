package me.einjojo.wandorialoot;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.einjojo.joslibrary.JoPlugin;
import me.einjojo.wandorialoot.chest.LootChestManager;
import me.einjojo.wandorialoot.command.SetupCommand;
import me.einjojo.wandorialoot.listener.ChunkLoadListener;
import me.einjojo.wandorialoot.listener.InteractionListener;
import me.einjojo.wandorialoot.listener.InventoryCloseListener;

public final class WandoriaLoot extends JoPlugin {

    private ProtocolManager protocolManager;
    private LootChestManager lootChestManager;
    private static WandoriaLoot instance;

    @Override
    public void onPluginEnable() {
        registerListener(
                new ChunkLoadListener(this),
                new InteractionListener(this),
                new InventoryCloseListener(this)
        );
        registerCommands(new SetupCommand());
        this.lootChestManager = new LootChestManager(this);
        lootChestManager.loadConfig();
    }

    @Override
    public void onPluginDisable() {
        lootChestManager.saveConfig();
    }

    @Override
    public void onPluginLoad() {
        instance = this;
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        setDebug(true);
    }

    @Override
    public String getPrefix() {
        return "§7[§cWandoriaLoot§7] ";
    }

    public static WandoriaLoot getInstance() {
        return instance;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public LootChestManager getLootChestManager() {
        return lootChestManager;
    }
}
