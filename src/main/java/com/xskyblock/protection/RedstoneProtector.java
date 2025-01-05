package com.xskyblock.protection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

import com.xskyblock.XSkyBlock;
import com.xskyblock.models.Island;
import com.xskyblock.models.Permission;

public class RedstoneProtector {
    private final XSkyBlock plugin;
    private static final int PROTECTION_RADIUS = 1;

    public RedstoneProtector(XSkyBlock plugin) {
        this.plugin = plugin;
    }

    public void handleRedstonePlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Material type = block.getType();
        
        if (type == Material.REDSTONE || type == Material.REDSTONE_BLOCK) {
            if (isNearAnyIsland(block.getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    private boolean isNearAnyIsland(Location location) {
        for (Island island : plugin.getIslandManager().getIslands().values()) {
            if (isNearIsland(location, island)) {
                return true;
            }
        }
        return false;
    }

    private boolean isNearIsland(Location location, Island island) {
        Location center = island.getCenter();
        if (center == null || !center.getWorld().equals(location.getWorld())) {
            return false;
        }

        double distance = location.distance(center);
        int islandRadius = island.getSize() / 2;
        return distance <= (islandRadius + PROTECTION_RADIUS);
    }
}