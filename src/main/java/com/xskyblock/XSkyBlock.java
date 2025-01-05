package com.xskyblock;

import org.bukkit.plugin.java.JavaPlugin;

import com.xskyblock.listeners.*;
import com.xskyblock.managers.*;
import com.xskyblock.scoreboard.ScoreboardManager;
import com.xskyblock.trust.TrustRequestManager;

public class XSkyBlock extends JavaPlugin {
    private static XSkyBlock instance;
    private IslandManager islandManager;
    private ConfigManager configManager;
    private GuiManager guiManager;
    private EconomyManager economyManager;
    private CommandManager commandManager;
    private SchematicManager schematicManager;
    private IslandDeleteManager islandDeleteManager;
    private ScoreboardManager scoreboardManager;
    private TrustRequestManager trustRequestManager;

    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize managers
        this.configManager = new ConfigManager(this);
        this.economyManager = new EconomyManager(this);
        this.islandManager = new IslandManager(this);
        this.schematicManager = new SchematicManager(this);
        this.guiManager = new GuiManager(this);
        this.commandManager = new CommandManager(this);
        this.islandDeleteManager = new IslandDeleteManager(this);
        this.scoreboardManager = new ScoreboardManager(this);
        this.trustRequestManager = new TrustRequestManager(this);
        
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
        getServer().getPluginManager().registerEvents(new TreeGrowthListener(this), this);
        getServer().getPluginManager().registerEvents(new IslandBoundaryListener(this), this);
        getServer().getPluginManager().registerEvents(new ScoreboardListener(this), this);
        getServer().getPluginManager().registerEvents(new TrustChatListener(this), this);
    }

    @Override
    public void onDisable() {
        if (islandManager != null) {
            islandManager.saveAllIslands();
        }
    }

    public static XSkyBlock getInstance() {
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

    public SchematicManager getSchematicManager() {
        return schematicManager;
    }

    public IslandDeleteManager getIslandDeleteManager() {
        return islandDeleteManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public TrustRequestManager getTrustRequestManager() {
        return trustRequestManager;
    }
}