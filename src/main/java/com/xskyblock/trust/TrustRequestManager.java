package com.xskyblock.trust;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.xskyblock.XSkyBlock;
import com.xskyblock.models.Island;
import com.xskyblock.models.IslandMember;
import com.xskyblock.utils.GuiUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrustRequestManager {
    private final XSkyBlock plugin;
    private final Map<UUID, TrustRequest> pendingRequests;

    public TrustRequestManager(XSkyBlock plugin) {
        this.plugin = plugin;
        this.pendingRequests = new HashMap<>();
    }

    public void createRequest(Player owner, Player target, Island island) {
        // Remove any existing request
        pendingRequests.remove(target.getUniqueId());
        
        // Create new request
        TrustRequest request = new TrustRequest(target.getUniqueId(), owner.getUniqueId(), island.getName());
        pendingRequests.put(target.getUniqueId(), request);

        // Send messages
        target.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("trust-request")
                .replace("%owner%", owner.getName())
                .replace("%island%", island.getName())));
        target.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("trust-request-instructions")));

        // Schedule expiration
        new BukkitRunnable() {
            @Override
            public void run() {
                if (pendingRequests.containsKey(target.getUniqueId())) {
                    TrustRequest expiredRequest = pendingRequests.get(target.getUniqueId());
                    if (expiredRequest.hasExpired()) {
                        pendingRequests.remove(target.getUniqueId());
                        target.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("trust-request-expired")));
                        Player ownerPlayer = Bukkit.getPlayer(expiredRequest.getIslandOwner());
                        if (ownerPlayer != null) {
                            ownerPlayer.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("trust-request-expired-owner")
                                    .replace("%player%", target.getName())));
                        }
                    }
                }
            }
        }.runTaskLater(plugin, 300L); // 15 seconds
    }

    public void handleResponse(Player player, boolean accept) {
        TrustRequest request = pendingRequests.get(player.getUniqueId());
        if (request == null) {
            player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("no-pending-request")));
            return;
        }

        Player owner = Bukkit.getPlayer(request.getIslandOwner());
        if (owner == null) {
            player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("owner-offline")));
            pendingRequests.remove(player.getUniqueId());
            return;
        }

        Island island = plugin.getIslandManager().getIsland(owner);
        if (island == null) {
            player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("island-not-found")));
            pendingRequests.remove(player.getUniqueId());
            return;
        }

        if (accept) {
            island.addTrustedPlayer(player.getUniqueId(), new IslandMember());
            plugin.getIslandManager().saveIslandAsync(island);
            
            player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("trust-accepted")
                    .replace("%island%", island.getName())));
            owner.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("trust-accepted-owner")
                    .replace("%player%", player.getName())));
        } else {
            player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("trust-declined")));
            owner.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("trust-declined-owner")
                    .replace("%player%", player.getName())));
        }

        pendingRequests.remove(player.getUniqueId());
    }

    public boolean hasPendingRequest(UUID playerUUID) {
        return pendingRequests.containsKey(playerUUID);
    }
}