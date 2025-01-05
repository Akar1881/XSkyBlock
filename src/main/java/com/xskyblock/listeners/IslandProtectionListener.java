package com.xskyblock.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.xskyblock.XSkyBlock;
import com.xskyblock.models.Island;
import com.xskyblock.models.Permission;
import com.xskyblock.utils.AdminUtils;

public class IslandProtectionListener implements Listener {
    private final XSkyBlock plugin;

    public IslandProtectionListener(XSkyBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (AdminUtils.canBypassProtection(player)) return;
        
        Island island = plugin.getIslandManager().getIslandAt(event.getBlock().getLocation());
        if (island != null && !hasPermission(player, island, Permission.BREAK)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (AdminUtils.canBypassProtection(player)) return;
        
        Island island = plugin.getIslandManager().getIslandAt(event.getBlock().getLocation());
        if (island != null && !hasPermission(player, island, Permission.PLACE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        
        Player player = event.getPlayer();
        if (AdminUtils.canBypassProtection(player)) return;
        
        Island island = plugin.getIslandManager().getIslandAt(event.getClickedBlock().getLocation());
        if (island != null && !hasPermission(player, island, Permission.USE)) {
            event.setCancelled(true);
        }
    }

    private boolean hasPermission(Player player, Island island, Permission permission) {
        if (island.getOwner().equals(player.getUniqueId())) return true;
        
        return island.getTrustedPlayers().containsKey(player.getUniqueId()) &&
               island.getTrustedPlayers().get(player.getUniqueId()).hasPermission(permission);
    }
}