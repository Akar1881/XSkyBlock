package com.xmyisland.listeners;

import com.xmyisland.XMyIsland;
import com.xmyisland.models.Island;
import com.xmyisland.models.Permission;
import com.xmyisland.utils.AdminUtils;
import com.xmyisland.utils.GuiUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMovementListener implements Listener {
    private final XMyIsland plugin;

    public PlayerMovementListener(XMyIsland plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        
        if (to == null || (from.getBlockX() == to.getBlockX() && 
                          from.getBlockZ() == to.getBlockZ())) {
            return;
        }

        Player player = event.getPlayer();
        if (AdminUtils.canBypassProtection(player)) return;
        
        Island fromIsland = plugin.getIslandManager().getIslandAt(from);
        Island toIsland = plugin.getIslandManager().getIslandAt(to);

        // Player is entering a new island
        if (fromIsland != toIsland && toIsland != null) {
            if (!canEnterIsland(player, toIsland)) {
                event.setCancelled(true);
                player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("no-entry-permission")));
            }
        }
    }

    private boolean canEnterIsland(Player player, Island island) {
        if (island.getOwner().equals(player.getUniqueId())) {
            return true;
        }

        return island.getTrustedPlayers().containsKey(player.getUniqueId()) &&
               island.getTrustedPlayers().get(player.getUniqueId()).hasPermission(Permission.ENTER);
    }
}