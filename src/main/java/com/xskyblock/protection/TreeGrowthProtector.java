package com.xskyblock.protection;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.world.StructureGrowEvent;

import com.xskyblock.XSkyBlock;
import com.xskyblock.models.Island;
import com.xskyblock.models.Permission;

import java.util.HashSet;
import java.util.Set;

public class TreeGrowthProtector {
    private final XSkyBlock plugin;
    private static final Set<Material> TREE_SAPLINGS = new HashSet<>();
    
    static {
        TREE_SAPLINGS.add(Material.OAK_SAPLING);
        TREE_SAPLINGS.add(Material.SPRUCE_SAPLING);
        TREE_SAPLINGS.add(Material.BIRCH_SAPLING);
        TREE_SAPLINGS.add(Material.JUNGLE_SAPLING);
        TREE_SAPLINGS.add(Material.ACACIA_SAPLING);
        TREE_SAPLINGS.add(Material.DARK_OAK_SAPLING);
    }

    public TreeGrowthProtector(XSkyBlock plugin) {
        this.plugin = plugin;
    }

    public void handleTreeGrowth(StructureGrowEvent event) {
        // Get the player who placed the sapling
        Player player = event.getPlayer();
        if (player == null) return; // Natural growth, allow it
        
        // Get the island at the growth location
        Island island = plugin.getIslandManager().getIslandAt(event.getLocation());
        if (island == null) return; // Not in an island, allow it
        
        // Check if player has USE permission
        if (!hasPermission(player, island)) {
            event.setCancelled(true);
        }
    }

    private boolean hasPermission(Player player, Island island) {
        if (island.getOwner().equals(player.getUniqueId())) {
            return true;
        }

        return island.getTrustedPlayers().containsKey(player.getUniqueId()) &&
               island.getTrustedPlayers().get(player.getUniqueId()).hasPermission(Permission.USE);
    }
}