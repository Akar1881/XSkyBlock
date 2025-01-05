package com.xskyblock.managers;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.xskyblock.XSkyBlock;

public class EconomyManager {
    private final XSkyBlock plugin;
    private Economy economy;

    public EconomyManager(XSkyBlock plugin) {
        this.plugin = plugin;
        setupEconomy();
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    public Economy getEconomy() {
        return economy;
    }
}