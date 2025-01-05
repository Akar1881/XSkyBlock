package com.xskyblock.managers;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import com.xskyblock.XSkyBlock;

public class ConfigManager {
    private final XSkyBlock plugin;
    private FileConfiguration config;

    public ConfigManager(XSkyBlock plugin) {
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

    public int getMaxIslandSize() {
        return config.getInt("settings.max-island-size", 101);
    }

    public Location getLobbyLocation() {
        return config.getLocation("lobby.location");
    }

    public void setLobbyLocation(Location location) {
        config.set("lobby.location", location);
        plugin.saveConfig();
    }

    public String getMessage(String path) {
        return config.getString("messages." + path, "Message not found: " + path);
    }
}