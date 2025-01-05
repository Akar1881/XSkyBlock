package com.xskyblock.listeners;

import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

import com.xskyblock.XSkyBlock;
import com.xskyblock.models.Island;
import com.xskyblock.models.Permission;

public class InventoryListener implements Listener {
    private final XSkyBlock plugin;

    public InventoryListener(XSkyBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        if (!(event.getInventory().getHolder() instanceof Container)) return;
        
        Player player = (Player) event.getPlayer();
        Container container = (Container) event.getInventory().getHolder();
        Island island = plugin.getIslandManager().getIslandAt(container.getLocation());
        
        if (island != null) {
            if (!hasPermission(player, island, Permission.OPEN_CHESTS)) {
                event.setCancelled(true);
            }
        }
    }

    private boolean hasPermission(Player player, Island island, Permission permission) {
        if (island.getOwner().equals(player.getUniqueId())) {
            return true;
        }

        return island.getTrustedPlayers().containsKey(player.getUniqueId()) &&
               island.getTrustedPlayers().get(player.getUniqueId()).hasPermission(permission);
    }
}