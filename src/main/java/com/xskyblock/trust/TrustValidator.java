package com.xskyblock.trust;

import org.bukkit.entity.Player;

import com.xskyblock.XSkyBlock;
import com.xskyblock.models.Island;

import java.util.UUID;

public class TrustValidator {
    private final XSkyBlock plugin;

    public TrustValidator(XSkyBlock plugin) {
        this.plugin = plugin;
    }

    public boolean canBeTrusted(Player owner, Player target) {
        // Can't trust yourself
        if (owner.getUniqueId().equals(target.getUniqueId())) {
            return false;
        }

        // Check if player already owns an island
        if (plugin.getIslandManager().hasIsland(target)) {
            return false;
        }

        // Check if player is trusted on another island
        return !isPlayerTrustedAnywhere(target.getUniqueId());
    }

    private boolean isPlayerTrustedAnywhere(UUID playerUUID) {
        return plugin.getIslandManager().getIslands().values().stream()
                .anyMatch(island -> island.isPlayerTrusted(playerUUID));
    }
}