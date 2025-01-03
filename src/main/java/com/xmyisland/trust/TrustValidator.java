package com.xmyisland.trust;

import com.xmyisland.XMyIsland;
import com.xmyisland.models.Island;
import org.bukkit.entity.Player;
import java.util.UUID;

public class TrustValidator {
    private final XMyIsland plugin;

    public TrustValidator(XMyIsland plugin) {
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