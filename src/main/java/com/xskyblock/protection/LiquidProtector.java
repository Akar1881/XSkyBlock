package com.xskyblock.protection;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockFromToEvent;

import com.xskyblock.XSkyBlock;
import com.xskyblock.models.Island;

public class LiquidProtector {
    private final XSkyBlock plugin;
    private static final BlockFace[] FACES = {
        BlockFace.NORTH, BlockFace.SOUTH, 
        BlockFace.EAST, BlockFace.WEST, 
        BlockFace.UP, BlockFace.DOWN
    };

    public LiquidProtector(XSkyBlock plugin) {
        this.plugin = plugin;
    }

    public void handleLiquidFlow(BlockFromToEvent event) {
        Block from = event.getBlock();
        Block to = event.getToBlock();

        // Check if either block is within or near an island
        Island fromIsland = plugin.getIslandManager().getIslandAt(from.getLocation());
        Island toIsland = plugin.getIslandManager().getIslandAt(to.getLocation());

        // Cancel if liquid is flowing into or out of an island
        if (fromIsland != toIsland) {
            event.setCancelled(true);
        }
    }
}