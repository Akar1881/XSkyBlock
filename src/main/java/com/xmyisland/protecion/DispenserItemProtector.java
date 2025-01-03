package com.xmyisland.protection;

import com.xmyisland.XMyIsland;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockDispenseEvent;

import java.util.HashSet;
import java.util.Set;

public class DispenserItemProtector {
    private static final Set<Material> BLOCKED_MATERIALS = new HashSet<>();
    
    static {
        // Add falling blocks
        BLOCKED_MATERIALS.add(Material.SAND);
        BLOCKED_MATERIALS.add(Material.RED_SAND);
        BLOCKED_MATERIALS.add(Material.GRAVEL);
        BLOCKED_MATERIALS.add(Material.ANVIL);
        BLOCKED_MATERIALS.add(Material.DRAGON_EGG);
        // Add concrete powder variants
        for (Material material : Material.values()) {
            if (material.name().endsWith("CONCRETE_POWDER")) {
                BLOCKED_MATERIALS.add(material);
            }
        }
    }

    public void handleDispense(BlockDispenseEvent event) {
        Material type = event.getItem().getType();
        if (BLOCKED_MATERIALS.contains(type)) {
            event.setCancelled(true);
        }
    }
}