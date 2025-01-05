package com.xskyblock.protection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.xskyblock.XSkyBlock;
import com.xskyblock.models.Island;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FallingBlockProtector {
    private final XSkyBlock plugin;
    private static final Set<Material> PROTECTED_MATERIALS = new HashSet<>();
    
    static {
        PROTECTED_MATERIALS.add(Material.SAND);
        PROTECTED_MATERIALS.add(Material.RED_SAND);
        PROTECTED_MATERIALS.add(Material.GRAVEL);
        PROTECTED_MATERIALS.add(Material.ANVIL);
        PROTECTED_MATERIALS.add(Material.DRAGON_EGG);
        // Add concrete powder variants
        for (Material material : Material.values()) {
            if (material.name().endsWith("CONCRETE_POWDER")) {
                PROTECTED_MATERIALS.add(material);
            }
        }
    }

    public FallingBlockProtector(XSkyBlock plugin) {
        this.plugin = plugin;
    }

    public void handleFallingBlockLand(EntityChangeBlockEvent event) {
        if (!(event.getEntity() instanceof FallingBlock)) return;
        
        Location landingLocation = event.getBlock().getLocation();
        Island targetIsland = plugin.getIslandManager().getIslandAt(landingLocation);
        
        if (targetIsland != null && PROTECTED_MATERIALS.contains(event.getTo())) {
            event.setCancelled(true);
            event.getEntity().remove();
        }
    }

    public void handleExplosion(EntityExplodeEvent event) {
        Iterator<org.bukkit.block.Block> blockIterator = event.blockList().iterator();
        
        while (blockIterator.hasNext()) {
            org.bukkit.block.Block block = blockIterator.next();
            
            // Check if the block is protected and near an island
            if (PROTECTED_MATERIALS.contains(block.getType())) {
                Location blockLocation = block.getLocation();
                Island nearbyIsland = plugin.getIslandManager().getIslandAt(blockLocation);
                
                if (nearbyIsland != null) {
                    blockIterator.remove();
                }
            }
        }
    }
}