package com.xskyblock.scoreboard;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.xskyblock.models.Island;

public class ScoreboardUtils {
    public static boolean isInValidWorld(Player player, World lobbyWorld) {
        World world = player.getWorld();
        return world.getName().equals("XSkyBlock") || 
               (lobbyWorld != null && world.equals(lobbyWorld));
    }

    public static String formatIslandInfo(Island island) {
        if (island == null) {
            return "None";
        }
        return island.getName();
    }

    public static String formatSize(Island island) {
        if (island == null) {
            return "0x0";
        }
        return island.getSize() + "x" + island.getSize();
    }

    public static String formatPlayerCount(Island island) {
        if (island == null) {
            return "0";
        }
        return String.valueOf(island.getTrustedPlayers().size());
    }
}