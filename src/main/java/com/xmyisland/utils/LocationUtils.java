package com.xmyisland.utils;

import org.bukkit.Location;

public class LocationUtils {
    public static boolean isSameBlock(Location loc1, Location loc2) {
        return loc1.getBlockX() == loc2.getBlockX() &&
               loc1.getBlockY() == loc2.getBlockY() &&
               loc1.getBlockZ() == loc2.getBlockZ();
    }

    public static boolean isWithinBounds(Location location, Location center, int size) {
        int halfSize = size / 2;
        int minX = center.getBlockX() - halfSize;
        int maxX = center.getBlockX() + halfSize;
        int minZ = center.getBlockZ() - halfSize;
        int maxZ = center.getBlockZ() + halfSize;

        return location.getBlockX() >= minX && location.getBlockX() <= maxX &&
               location.getBlockZ() >= minZ && location.getBlockZ() <= maxZ;
    }
}