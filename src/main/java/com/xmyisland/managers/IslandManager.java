package com.xmyisland.managers;

import com.xmyisland.XMyIsland;
import com.xmyisland.models.Island;
import com.xmyisland.utils.GuiUtils;
import com.xmyisland.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class IslandManager {
    private final XMyIsland plugin;
    private final Map<UUID, Island> islands;
    private final File dataFolder;

    public IslandManager(XMyIsland plugin) {
        this.plugin = plugin;
        this.islands = new HashMap<>();
        this.dataFolder = new File(plugin.getDataFolder(), "islands");
        loadIslands();
    }

    public void loadIslands() {
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
            return;
        }

        File[] files = dataFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) return;

        for (File file : files) {
            try {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                Island island = Island.fromConfig(config);
                islands.put(island.getOwner(), island);
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to load island from file: " + file.getName());
                e.printStackTrace();
            }
        }
    }

    public void saveAllIslands() {
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        for (Island island : islands.values()) {
            saveIsland(island);
        }
    }

    private void saveIsland(Island island) {
        File file = new File(dataFolder, island.getOwner().toString() + ".yml");
        YamlConfiguration config = new YamlConfiguration();
        island.saveToConfig(config);
        
        try {
            config.save(file);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to save island for player: " + island.getOwner());
            e.printStackTrace();
        }
    }

    public boolean hasIsland(Player player) {
        return islands.containsKey(player.getUniqueId());
    }

    public Island getIsland(Player player) {
        return islands.get(player.getUniqueId());
    }

    public Island getIslandAt(Location location) {
        for (Island island : islands.values()) {
            if (LocationUtils.isWithinBounds(location, island.getCenter(), island.getSize())) {
                return island;
            }
        }
        return null;
    }

    public boolean createIsland(Player player, String name) {
        if (hasIsland(player)) {
            return false;
        }

        Island island = new Island(player.getUniqueId(), name);
        island.setCenter(player.getLocation());
        islands.put(player.getUniqueId(), island);
        saveIsland(island);
        return true;
    }

    // Add this method to save island after modifications
    public void saveIslandAsync(Island island) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> saveIsland(island));
    }

    public Map<UUID, Island> getIslands() {
        return Collections.unmodifiableMap(islands);
    }

    public void deleteIsland(Player player) {
        Island island = islands.get(player.getUniqueId());
        if (island != null) {
            // Calculate refund
            double refund = calculateRefund(island);
            
            // Give refund to player
            plugin.getEconomyManager().getEconomy().depositPlayer(player, refund);
            
            // Cleanup island resources
            island.cleanup();
            
            // Remove all trusted players
            island.getTrustedPlayers().clear();
            
            // Remove from memory
            islands.remove(player.getUniqueId());
            
            // Delete file
            File file = new File(dataFolder, player.getUniqueId().toString() + ".yml");
            if (file.exists()) {
                file.delete();
            }
            
            // Notify player
            player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("island-deleted")
                    .replace("%amount%", String.valueOf(refund))));
        }
    }

    private double calculateRefund(Island island) {
        double totalCost = 0;
        int baseSize = plugin.getConfigManager().getDefaultIslandSize();
        
        // Calculate cost of all expansions
        for (int size = baseSize + 2; size <= island.getSize(); size += 2) {
            totalCost += plugin.getConfigManager().getPricePerBlock() * size;
        }
        
        // Apply refund percentage
        return totalCost * (plugin.getConfigManager().getRefundPercentage() / 100.0);
    }

    public void forceDeleteIsland(Island island) {
        if (island != null) {
            // Cleanup island resources
            island.cleanup();
            
            // Remove from memory
            islands.remove(island.getOwner());
            
            // Delete file
            File file = new File(dataFolder, island.getOwner().toString() + ".yml");
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public boolean isPlayerTrustedAnywhere(UUID playerUUID) {
        for (Island island : islands.values()) {
            if (island.getTrustedPlayers().containsKey(playerUUID)) {
                return true;
            }
        }
        return false;
    }
}