package me.einjojo.wandorialoot;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.einjojo.joslibrary.JoPlugin;

public final class WandoriaLoot extends JoPlugin {

    private ProtocolManager protocolManager;
    private static WandoriaLoot instance;

    @Override
    public void onPluginEnable() {

    }

    @Override
    public void onPluginDisable() {

    }

    @Override
    public void onPluginLoad() {
        instance = this;
        this.protocolManager = ProtocolLibrary.getProtocolManager();
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
}
