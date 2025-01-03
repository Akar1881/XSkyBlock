package com.xmyisland.protection;

import com.xmyisland.XMyIsland;
import com.xmyisland.models.Island;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Iterator;

public class ExplosionProtector {
    private final XMyIsland plugin;
    private static final int PROTECTION_RADIUS = 50;

    public ExplosionProtector(XMyIsland plugin) {
        this.plugin = plugin;
    }

    public void handleExplosion(EntityExplodeEvent event) {
        Location explosionLocation = event.getLocation();
        
        // Check if explosion is near any island
        for (Island island : plugin.getIslandManager().getIslands().values()) {
            if (isNearIsland(explosionLocation, island)) {
                event.setCancelled(true);
                return;
            }
        }

        // If explosion wasn't cancelled, remove blocks that would affect islands
        Iterator<Block> blockIterator = event.blockList().iterator();
        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            if (isBlockProtected(block.getLocation())) {
                blockIterator.remove();
            }
        }
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

    private boolean isBlockProtected(Location location) {
        for (Island island : plugin.getIslandManager().getIslands().values()) {
            if (isNearIsland(location, island)) {
                return true;
            }
        }
        return false;
    }
}