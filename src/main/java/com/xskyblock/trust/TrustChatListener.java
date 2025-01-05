package com.xskyblock.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.xskyblock.XSkyBlock;

public class TrustChatListener implements Listener {
    private final XSkyBlock plugin;

    public TrustChatListener(XSkyBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().toLowerCase();

        if (plugin.getTrustRequestManager().hasPendingRequest(player.getUniqueId())) {
            if (message.equals("accept")) {
                event.setCancelled(true);
                // Run on next tick since we're in an async event
                plugin.getServer().getScheduler().runTask(plugin, () -> 
                    plugin.getTrustRequestManager().handleResponse(player, true));
            } else if (message.equals("cancel")) {
                event.setCancelled(true);
                // Run on next tick since we're in an async event
                plugin.getServer().getScheduler().runTask(plugin, () -> 
                    plugin.getTrustRequestManager().handleResponse(player, false));
            }
        }
    }
}