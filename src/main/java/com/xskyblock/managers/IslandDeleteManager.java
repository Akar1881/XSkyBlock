package com.xskyblock.managers;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.xskyblock.XSkyBlock;
import com.xskyblock.models.Island;
import com.xskyblock.utils.GuiUtils;

public class IslandDeleteManager {
    private final XSkyBlock plugin;

    public IslandDeleteManager(XSkyBlock plugin) {
        this.plugin = plugin;
    }

    public void deleteIsland(Player player, boolean force) {
        Island island = force ? 
            plugin.getIslandManager().getIslandAt(player.getLocation()) :
            plugin.getIslandManager().getIsland(player);

        if (island == null) {
            player.sendMessage(GuiUtils.color("&cNo island found to delete!"));
            return;
        }

        // Calculate refund if not force delete
        double refund = 0;
        if (!force) {
            refund = calculateRefund(island);
            plugin.getEconomyManager().getEconomy().depositPlayer(player, refund);
        }

        // Clear island blocks and entities
        clearIslandBlocks(island);
        removeIslandEntities(island);

        // Remove all trusted players
        island.getTrustedPlayers().clear();

        // Remove from memory and delete file
        plugin.getIslandManager().removeIsland(island.getOwner());

        // Teleport to lobby
        Location lobby = plugin.getConfigManager().getLobbyLocation();
        if (lobby != null) {
            player.teleport(lobby);
        }

        // Send message
        if (force) {
            player.sendMessage(GuiUtils.color("&aSuccessfully force deleted the island!"));
        } else {
            player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("island-deleted")
                    .replace("%amount%", String.format("%.2f", refund))));
        }
    }

    private double calculateRefund(Island island) {
        double totalCost = 0;
        int baseSize = plugin.getConfigManager().getDefaultIslandSize();
        
        // Calculate cost of all expansions
        for (int size = baseSize + 2; size <= island.getSize(); size += 2) {
            totalCost += plugin.getConfigManager().getPricePerBlock() * size;
        }
        
        // Apply refund percentage
        return totalCost * (plugin.getConfigManager().getRefundPercentage() / 100.0);
    }

    private void clearIslandBlocks(Island island) {
        World world = island.getCenter().getWorld();
        int halfSize = island.getSize() / 2;
        int centerX = island.getCenter().getBlockX();
        int centerZ = island.getCenter().getBlockZ();

        // Clear blocks in island area
        for (int x = centerX - halfSize; x <= centerX + halfSize; x++) {
            for (int z = centerZ - halfSize; z <= centerZ + halfSize; z++) {
                for (int y = 0; y < world.getMaxHeight(); y++) {
                    Block block = world.getBlockAt(x, y, z);
                    block.setType(org.bukkit.Material.AIR);
                }
            }
        }
    }

    private void removeIslandEntities(Island island) {
        World world = island.getCenter().getWorld();
        int halfSize = island.getSize() / 2;
        int centerX = island.getCenter().getBlockX();
        int centerZ = island.getCenter().getBlockZ();
        Location min = new Location(world, centerX - halfSize, 0, centerZ - halfSize);
        Location max = new Location(world, centerX + halfSize, world.getMaxHeight(), centerZ + halfSize);

        // Remove entities in island area
        for (Entity entity : world.getEntities()) {
            Location loc = entity.getLocation();
            if (!(entity instanceof Player) && 
                loc.getX() >= min.getX() && loc.getX() <= max.getX() &&
                loc.getY() >= min.getY() && loc.getY() <= max.getY() &&
                loc.getZ() >= min.getZ() && loc.getZ() <= max.getZ()) {
                entity.remove();
            }
        }
    }
}