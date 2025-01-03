package com.xmyisland.protection;

import com.xmyisland.XMyIsland;
import com.xmyisland.models.Island;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import java.util.List;

public class BlockProtector {
    private final XMyIsland plugin;
    private static final int PROTECTION_RADIUS = 1;
    private static final BlockFace[] CHECK_FACES = {
        BlockFace.NORTH, BlockFace.SOUTH, 
        BlockFace.EAST, BlockFace.WEST,
        BlockFace.UP, BlockFace.DOWN
    };

    public BlockProtector(XMyIsland plugin) {
        this.plugin = plugin;
    }

    public void handlePistonExtend(BlockPistonExtendEvent event) {
        Block piston = event.getBlock();
        List<Block> blocks = event.getBlocks();
        BlockFace direction = event.getDirection();

        // Check if piston is near any island
        if (isNearAnyIsland(piston.getLocation())) {
            event.setCancelled(true);
            return;
        }

        // Check if any moved block would enter an island's area
        for (Block block : blocks) {
            Location newLoc = block.getLocation().add(
                direction.getModX(),
                direction.getModY(),
                direction.getModZ()
            );
            
            if (isLocationProtected(newLoc)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    public void handlePistonRetract(BlockPistonRetractEvent event) {
        Block piston = event.getBlock();
        List<Block> blocks = event.getBlocks();
        BlockFace direction = event.getDirection();

        // Check if piston is near any island
        if (isNearAnyIsland(piston.getLocation())) {
            event.setCancelled(true);
            return;
        }

        // Check if any moved block would enter an island's area
        for (Block block : blocks) {
            Location newLoc = block.getLocation().add(
                direction.getModX(),
                direction.getModY(),
                direction.getModZ()
            );
            
            if (isLocationProtected(newLoc)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    public void handleDispense(BlockDispenseEvent event) {
        Block dispenser = event.getBlock();
        
        // Check if dispenser is near any island
        if (isNearAnyIsland(dispenser.getLocation())) {
            // Cancel if dispensing water or lava
            Material type = event.getItem().getType();
            if (type == Material.WATER_BUCKET || 
                type == Material.LAVA_BUCKET || 
                type == Material.WATER || 
                type == Material.LAVA) {
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

    private boolean isLocationProtected(Location location) {
        return plugin.getIslandManager().getIslandAt(location) != null;
    }
}