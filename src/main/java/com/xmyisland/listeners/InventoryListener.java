package com.xmyisland.listeners;

import com.xmyisland.XMyIsland;
import com.xmyisland.models.Island;
import com.xmyisland.models.Permission;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class InventoryListener implements Listener {
    private final XMyIsland plugin;

    public InventoryListener(XMyIsland plugin) {
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