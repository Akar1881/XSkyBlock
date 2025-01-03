package com.xmyisland.protection;

import com.xmyisland.XMyIsland;
import com.xmyisland.models.Island;
import com.xmyisland.models.Permission;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

public class RedstoneProtector {
    private final XMyIsland plugin;
    private static final int PROTECTION_RADIUS = 1;

    public RedstoneProtector(XMyIsland plugin) {
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