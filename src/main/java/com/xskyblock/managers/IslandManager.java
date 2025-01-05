package com.xskyblock.managers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.xskyblock.XSkyBlock;
import com.xskyblock.models.Island;
import com.xskyblock.utils.GuiUtils;
import com.xskyblock.utils.LocationUtils;
import com.xskyblock.world.VoidWorldGenerator;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class IslandManager {
    private final XSkyBlock plugin;
    private final Map<UUID, Island> islands;
    private final File dataFolder;
    private final int ISLAND_SPACING = 1000;
    private int nextIslandIndex = 0;
    private static final String WORLD_NAME = "XSkyBlock";

    public IslandManager(XSkyBlock plugin) {
        this.plugin = plugin;
        this.islands = new HashMap<>();
        this.dataFolder = new File(plugin.getDataFolder(), "islands");
        setupWorld();
        loadIslands();
    }

    public void removeIsland(UUID owner) {
        islands.remove(owner);
        File file = new File(dataFolder, owner.toString() + ".yml");
        if (file.exists()) {
            file.delete();
        }
    }

    private void setupWorld() {
        World world = plugin.getServer().getWorld(WORLD_NAME);
        if (world == null) {
            WorldCreator creator = new WorldCreator(WORLD_NAME);
            creator.environment(Environment.NORMAL);
            creator.generator(new VoidWorldGenerator());
            world = creator.createWorld();
            if (world != null) {
                world.setSpawnLocation(0, 100, 0);
                world.setKeepSpawnInMemory(false);
            }
        }
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
                // Update nextIslandIndex based on existing islands
                Location center = island.getCenter();
                if (center != null) {
                    int x = center.getBlockX();
                    int z = center.getBlockZ();
                    int index = (x / ISLAND_SPACING) + (z / ISLAND_SPACING) * (int)Math.sqrt(Integer.MAX_VALUE);
                    nextIslandIndex = Math.max(nextIslandIndex, index + 1);
                }
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

        World world = plugin.getServer().getWorld(WORLD_NAME);
        if (world == null) {
            plugin.getLogger().severe("Island world not found!");
            return false;
        }

        // Calculate grid position for new island
        int gridSize = (int) Math.sqrt(nextIslandIndex + 1);
        int x = (nextIslandIndex % gridSize) * ISLAND_SPACING;
        int z = (nextIslandIndex / gridSize) * ISLAND_SPACING;
        
        // Create base location for schematic paste
        Location islandLocation = new Location(world, x, 100, z);
        
        // Paste the schematic
        plugin.getSchematicManager().pasteSchematic(islandLocation);
        
        // Get the spawn point - this will be our new center for protection
        final Location spawnPoint = determineSpawnPoint(islandLocation);
        
        // Create and setup the island
        Island island = createAndSetupIsland(player, name, spawnPoint);
        
        // Ensure spawn is safe and teleport player
        ensureSafeSpawn(spawnPoint);
        teleportPlayerToIsland(player, spawnPoint);
        
        // Save island and increment counter
        saveIsland(island);
        nextIslandIndex++;
        
        return true;
    }

    private Location determineSpawnPoint(Location islandLocation) {
        Location spawnPoint = plugin.getSchematicManager().getRelativeSpawnPoint(islandLocation);
        if (spawnPoint == null) {
            spawnPoint = islandLocation.clone().add(0.5, 1, 0.5);
        }
        return spawnPoint;
    }

    private Island createAndSetupIsland(Player player, String name, Location spawnPoint) {
        Island island = new Island(player.getUniqueId(), name);
        island.setCenter(spawnPoint);
        island.setSize(plugin.getConfigManager().getDefaultIslandSize());
        islands.put(player.getUniqueId(), island);
        return island;
    }

    private void teleportPlayerToIsland(Player player, Location spawnPoint) {
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> 
            player.teleport(spawnPoint), 10L);
    }

    private void ensureSafeSpawn(Location location) {
        location.getBlock().setType(Material.AIR);
        location.clone().add(0, 1, 0).getBlock().setType(Material.AIR);
        
        Block block = location.clone().add(0, -1, 0).getBlock();
        if (!block.getType().isSolid()) {
            block.setType(Material.STONE);
        }
    }

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