package com.xmyisland.utils;

import com.xmyisland.XMyIsland;
import com.xmyisland.models.Island;
import org.bukkit.Location;

public class IslandBoundaryUtils {
    public static boolean wouldOverlap(Location center, int size, Island excludeIsland) {
        int halfSize = size / 2;
        int minX = center.getBlockX() - halfSize;
        int maxX = center.getBlockX() + halfSize;
        int minZ = center.getBlockZ() - halfSize;
        int maxZ = center.getBlockZ() + halfSize;

        for (Island otherIsland : XMyIsland.getInstance().getIslandManager().getIslands().values()) {
            if (otherIsland.equals(excludeIsland)) continue;
            
            Location otherCenter = otherIsland.getCenter();
            int otherHalfSize = otherIsland.getSize() / 2;
            
            // Calculate other island boundaries
            int otherMinX = otherCenter.getBlockX() - otherHalfSize;
            int otherMaxX = otherCenter.getBlockX() + otherHalfSize;
            int otherMinZ = otherCenter.getBlockZ() - otherHalfSize;
            int otherMaxZ = otherCenter.getBlockZ() + otherHalfSize;

            // Check for overlap
            if (!(minX > otherMaxX || maxX < otherMinX || 
                  minZ > otherMaxZ || maxZ < otherMinZ)) {
                return true;
            }
        }
        return false;
    }
}