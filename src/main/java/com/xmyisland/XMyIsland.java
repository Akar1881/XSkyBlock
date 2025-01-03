package com.xmyisland;

import com.xmyisland.managers.*;
import com.xmyisland.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

public class XMyIsland extends JavaPlugin {
    private static XMyIsland instance;
    private IslandManager islandManager;
    private ConfigManager configManager;
    private GuiManager guiManager;
    private EconomyManager economyManager;
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize managers
        this.configManager = new ConfigManager(this);
        this.economyManager = new EconomyManager(this);
        this.islandManager = new IslandManager(this);
        this.guiManager = new GuiManager(this);
        this.commandManager = new CommandManager(this);
        
        // Register commands
        getCommand("xmi").setExecutor(commandManager);
        getCommand("xmi").setTabCompleter(commandManager);

        // Register listeners
        registerListeners();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new GuiListener(this), this);
        getServer().getPluginManager().registerEvents(new IslandProtectionListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMovementListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityInteractionListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        getServer().getPluginManager().registerEvents(new ExplosionListener(this), this);
        getServer().getPluginManager().registerEvents(new LiquidListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockProtectionListener(this), this);
        getServer().getPluginManager().registerEvents(new AdvancedProtectionListener(this), this);
        getServer().getPluginManager().registerEvents(new FallingBlockListener(this), this);
        getServer().getPluginManager().registerEvents(new TreeGrowthListener(this), this); // Added new listener
    }

    @Override
    public void onDisable() {
        if (islandManager != null) {
            islandManager.saveAllIslands();
        }
    }

    public static XMyIsland getInstance() {
        return instance;
    }

    public IslandManager getIslandManager() {
        return islandManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }
}