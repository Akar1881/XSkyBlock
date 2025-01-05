package com.xskyblock.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import com.xskyblock.XSkyBlock;
import com.xskyblock.models.Island;
import com.xskyblock.utils.GuiUtils;
import com.xskyblock.utils.LocationUtils;

public class IslandBoundaryListener implements Listener {
    private final XSkyBlock plugin;
    private static final String SKYBLOCK_WORLD = "XSkyBlock";

    public IslandBoundaryListener(XSkyBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        
        // Only check in XSkyBlock world
        if (!world.getName().equals(SKYBLOCK_WORLD)) {
            return;
        }

        Location to = event.getTo();
        Location from = event.getFrom();
        
        if (to == null || (from.getBlockX() == to.getBlockX() && 
                          from.getBlockZ() == to.getBlockZ())) {
            return;
        }

        // Get both owned and trusted islands
        Island ownedIsland = plugin.getIslandManager().getIsland(player);
        Island trustedIsland = findTrustedIsland(player);
        Island currentIsland = plugin.getIslandManager().getIslandAt(to);

        // Check if player is trying to leave their current island
        if (currentIsland != null && (currentIsland.equals(ownedIsland) || currentIsland.equals(trustedIsland))) {
            if (!LocationUtils.isWithinBounds(to, currentIsland.getCenter(), currentIsland.getSize())) {
                event.setCancelled(true);
                
                // Push player back into the island
                Vector pushBack = from.toVector().subtract(to.toVector()).normalize().multiply(1.0);
                player.setVelocity(pushBack);
                
                // Show appropriate message based on whether it's their island or a trusted one
                if (currentIsland.equals(ownedIsland)) {
                    double nextUpgradeCost = plugin.getConfigManager().getPricePerBlock() * (currentIsland.getSize() + 2);
                    player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("border-reached")
                        .replace("%cost%", String.format("%.2f", nextUpgradeCost))));
                } else {
                    player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("trusted-border-reached")));
                }
            }
        }
    }

    private Island findTrustedIsland(Player player) {
        for (Island island : plugin.getIslandManager().getIslands().values()) {
            if (island.getTrustedPlayers().containsKey(player.getUniqueId())) {
                return island;
            }
        }
        return null;
    }
}