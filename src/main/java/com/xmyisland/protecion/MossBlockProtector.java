package com.xmyisland.protection;

import com.xmyisland.XMyIsland;
import com.xmyisland.models.Island;
import com.xmyisland.models.Permission;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashSet;
import java.util.Set;

public class MossBlockProtector {
    private final XMyIsland plugin;
    private static final Set<Material> PROTECTED_MATERIALS = new HashSet<>();
    
    static {
        // Add materials that should be protected from bone meal
        PROTECTED_MATERIALS.add(Material.MOSS_BLOCK);
        PROTECTED_MATERIALS.add(Material.OAK_SAPLING);
        PROTECTED_MATERIALS.add(Material.SPRUCE_SAPLING);
        PROTECTED_MATERIALS.add(Material.BIRCH_SAPLING);
        PROTECTED_MATERIALS.add(Material.JUNGLE_SAPLING);
        PROTECTED_MATERIALS.add(Material.ACACIA_SAPLING);
        PROTECTED_MATERIALS.add(Material.DARK_OAK_SAPLING);
        PROTECTED_MATERIALS.add(Material.WHEAT);
        PROTECTED_MATERIALS.add(Material.CARROTS);
        PROTECTED_MATERIALS.add(Material.POTATOES);
        PROTECTED_MATERIALS.add(Material.BEETROOTS);
        PROTECTED_MATERIALS.add(Material.BAMBOO);
        PROTECTED_MATERIALS.add(Material.SUGAR_CANE);
        PROTECTED_MATERIALS.add(Material.CACTUS);
    }

    public MossBlockProtector(XMyIsland plugin) {
        this.plugin = plugin;
    }

    public void handleBoneMealUse(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        if (event.getItem() == null || event.getItem().getType() != Material.BONE_MEAL) return;
        
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        
        // Check if the clicked block is protected
        if (PROTECTED_MATERIALS.contains(block.getType())) {
            Island island = plugin.getIslandManager().getIslandAt(block.getLocation());
            if (island != null && !hasPermission(player, island)) {
                event.setCancelled(true);
            }
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