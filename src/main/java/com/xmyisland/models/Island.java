package com.xmyisland.models;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import com.xmyisland.XMyIsland;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Island {
    private final UUID owner;
    private String name;
    private int size;
    private Location center;
    private final Map<UUID, IslandMember> trustedPlayers;
    private boolean borderVisible;
    private BukkitRunnable borderTask;

    public Island(UUID owner, String name) {
        this.owner = owner;
        this.name = name;
        this.size = 9;
        this.trustedPlayers = new HashMap<>();
        this.borderVisible = true;
        startBorderTask();
    }

    public static Island fromConfig(YamlConfiguration config) {
        UUID owner = UUID.fromString(config.getString("owner"));
        String name = config.getString("name");
        Island island = new Island(owner, name);
        
        island.setSize(config.getInt("size", 9));
        island.setBorderVisible(config.getBoolean("border-visible", true));
        
        if (config.contains("center")) {
            island.setCenter(config.getLocation("center"));
            island.startBorderTask();
        }
        
        if (config.contains("trusted-players")) {
            for (String key : config.getConfigurationSection("trusted-players").getKeys(false)) {
                UUID playerId = UUID.fromString(key);
                IslandMember member = new IslandMember();
                
                for (String perm : config.getStringList("trusted-players." + key + ".permissions")) {
                    member.setPermission(Permission.valueOf(perm), true);
                }
                
                island.addTrustedPlayer(playerId, member);
            }
        }
        
        return island;
    }

    private void startBorderTask() {
        if (borderTask != null) {
            borderTask.cancel();
        }

        if (center == null) return;

        borderTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!borderVisible) return;

                World world = center.getWorld();
                if (world == null) return;

                int halfSize = size / 2;
                int minX = center.getBlockX() - halfSize;
                int maxX = center.getBlockX() + halfSize;
                int minZ = center.getBlockZ() - halfSize;
                int maxZ = center.getBlockZ() + halfSize;

                // Show particles at y levels around the player
                for (int y = center.getBlockY() - 1; y <= center.getBlockY() + 2; y++) {
                    // Draw lines along X axis
                    for (int x = minX; x <= maxX; x++) {
                        world.spawnParticle(Particle.SCRAPE, x, y, minZ, 1, 0, 0, 0, 0);
                        world.spawnParticle(Particle.SCRAPE, x, y, maxZ, 1, 0, 0, 0, 0);
                    }
                    // Draw lines along Z axis
                    for (int z = minZ; z <= maxZ; z++) {
                        world.spawnParticle(Particle.SCRAPE, minX, y, z, 1, 0, 0, 0, 0);
                        world.spawnParticle(Particle.SCRAPE, maxX, y, z, 1, 0, 0, 0, 0);
                    }
                }
            }
        };

        borderTask.runTaskTimer(XMyIsland.getInstance(), 0L, 20L);
    }

    public void saveToConfig(YamlConfiguration config) {
        config.set("owner", owner.toString());
        config.set("name", name);
        config.set("size", size);
        config.set("border-visible", borderVisible);
        
        if (center != null) {
            config.set("center", center);
        }
        
        for (Map.Entry<UUID, IslandMember> entry : trustedPlayers.entrySet()) {
            String path = "trusted-players." + entry.getKey().toString();
            config.set(path + ".permissions", entry.getValue().getActivePermissions());
        }
    }

    public UUID getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        if (borderTask != null) {
            borderTask.cancel();
        }
        startBorderTask();
    }

    public Location getCenter() {
        return center;
    }

    public void setCenter(Location center) {
        this.center = center;
        startBorderTask();
    }

    public Map<UUID, IslandMember> getTrustedPlayers() {
        return trustedPlayers;
    }

    public boolean isBorderVisible() {
        return borderVisible;
    }

    public void setBorderVisible(boolean borderVisible) {
        this.borderVisible = borderVisible;
    }

    public void addTrustedPlayer(UUID playerUUID, IslandMember member) {
        trustedPlayers.put(playerUUID, member);
    }

    public void removeTrustedPlayer(UUID playerUUID) {
        trustedPlayers.remove(playerUUID);
    }

    public boolean isPlayerTrusted(UUID playerUUID) {
        return trustedPlayers.containsKey(playerUUID);
    }

    public void cleanup() {
        if (borderTask != null) {
            borderTask.cancel();
            borderTask = null;
        }
    }
}