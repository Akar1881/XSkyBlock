package com.xskyblock.utils;

import org.bukkit.entity.Player;
import com.xskyblock.models.Island;
import com.xskyblock.models.Permission;

import java.util.UUID;

public class IslandNavigationUtils {
    public static Island findTrustedIsland(Player player, Iterable<Island> islands) {
        UUID playerUUID = player.getUniqueId();
        for (Island island : islands) {
            if (island.getTrustedPlayers().containsKey(playerUUID)) {
                return island;
            }
        }
        return null;
    }

    public static boolean canEnterIsland(Player player, Island island) {
        if (island == null) return false;
        if (island.getOwner().equals(player.getUniqueId())) return true;
        
        return island.getTrustedPlayers().containsKey(player.getUniqueId()) &&
               island.getTrustedPlayers().get(player.getUniqueId()).hasPermission(Permission.ENTER);
    }
}