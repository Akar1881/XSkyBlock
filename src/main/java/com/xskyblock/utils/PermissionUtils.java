package com.xskyblock.utils;

import org.bukkit.entity.Player;

import com.xskyblock.models.Island;
import com.xskyblock.models.Permission;

public class PermissionUtils {
    public static boolean hasPermission(Player player, Island island, Permission permission) {
        if (island == null) return true;
        if (island.getOwner().equals(player.getUniqueId())) return true;
        
        return island.getTrustedPlayers().containsKey(player.getUniqueId()) &&
               island.getTrustedPlayers().get(player.getUniqueId()).hasPermission(permission);
    }
}