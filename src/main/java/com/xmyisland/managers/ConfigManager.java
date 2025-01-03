package com.xmyisland.managers;

import com.xmyisland.XMyIsland;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final XMyIsland plugin;
    private FileConfiguration config;

    public ConfigManager(XMyIsland plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
    }

    public double getPricePerBlock() {
        return config.getDouble("settings.price-per-one-block", 20.0);
    }

    public int getRefundPercentage() {
        return config.getInt("settings.delete-island-refund-percentage", 50);
    }

    public int getDefaultIslandSize() {
        return config.getInt("settings.default-island-size", 9);
    }

    public String getMessage(String path) {
        return config.getString("messages." + path, "Message not found: " + path);
    }
}