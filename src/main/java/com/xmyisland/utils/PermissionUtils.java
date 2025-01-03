package com.xmyisland.utils;

import com.xmyisland.models.Island;
import com.xmyisland.models.Permission;
import org.bukkit.entity.Player;

public class PermissionUtils {
    public static boolean hasPermission(Player player, Island island, Permission permission) {
        if (island == null) return true;
        if (island.getOwner().equals(player.getUniqueId())) return true;
        
        return island.getTrustedPlayers().containsKey(player.getUniqueId()) &&
               island.getTrustedPlayers().get(player.getUniqueId()).hasPermission(permission);
    }
}