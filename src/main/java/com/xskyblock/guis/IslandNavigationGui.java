package com.xskyblock.guis;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.xskyblock.XSkyBlock;
import com.xskyblock.models.Island;
import com.xskyblock.utils.GuiUtils;
import com.xskyblock.utils.IslandNavigationUtils;

public class IslandNavigationGui extends BaseGui {
    private static final int YOUR_ISLAND_SLOT = 11;
    private static final int TRUSTED_ISLAND_SLOT = 15;

    public IslandNavigationGui(XSkyBlock plugin, Player player) {
        super(plugin, player);
    }

    @Override
    public void initialize() {
        inventory = Bukkit.createInventory(this, 27, GuiUtils.color("&8Island Navigation"));

        // Your Island Button
        if (plugin.getIslandManager().hasIsland(player)) {
            inventory.setItem(YOUR_ISLAND_SLOT, GuiUtils.createGuiItem(
                Material.GRASS_BLOCK,
                "&aYour Island",
                "&7Click to teleport to your island"
            ));
        } else {
            inventory.setItem(YOUR_ISLAND_SLOT, GuiUtils.createGuiItem(
                Material.BARRIER,
                "&cNo Island",
                "&7You don't have an island!",
                "&7Type &f/xmi claim <name> &7to claim one"
            ));
        }

        // Trusted Island Button
        Island trustedIsland = IslandNavigationUtils.findTrustedIsland(player, plugin.getIslandManager().getIslands().values());
        if (trustedIsland != null) {
            boolean canEnter = IslandNavigationUtils.canEnterIsland(player, trustedIsland);
            inventory.setItem(TRUSTED_ISLAND_SLOT, GuiUtils.createGuiItem(
                canEnter ? Material.PLAYER_HEAD : Material.RED_STAINED_GLASS_PANE,
                canEnter ? "&bTrusted Island" : "&cNo Access",
                canEnter ? "&7Click to teleport to trusted island" : "&cYou don't have permission to enter this island"
            ));
        } else {
            inventory.setItem(TRUSTED_ISLAND_SLOT, GuiUtils.createGuiItem(
                Material.BARRIER,
                "&cNot Trusted",
                "&7You aren't trusted in any island"
            ));
        }
    }

    @Override
    public void handleClick(int slot) {
        switch (slot) {
            case YOUR_ISLAND_SLOT:
                if (plugin.getIslandManager().hasIsland(player)) {
                    Island island = plugin.getIslandManager().getIsland(player);
                    player.teleport(island.getCenter());
                    player.sendMessage(GuiUtils.color("&aTeleported to your island!"));
                } else {
                    player.sendMessage(GuiUtils.color("&cYou don't have an island! Type &f/xmi claim <name> &cto claim one."));
                }
                player.closeInventory();
                break;

            case TRUSTED_ISLAND_SLOT:
                Island trustedIsland = IslandNavigationUtils.findTrustedIsland(player, plugin.getIslandManager().getIslands().values());
                if (trustedIsland != null) {
                    if (IslandNavigationUtils.canEnterIsland(player, trustedIsland)) {
                        player.teleport(trustedIsland.getCenter());
                        player.sendMessage(GuiUtils.color("&aTeleported to trusted island!"));
                    } else {
                        player.sendMessage(GuiUtils.color("&cYou don't have permission to enter this island!"));
                    }
                } else {
                    player.sendMessage(GuiUtils.color("&cYou aren't trusted in any island!"));
                }
                player.closeInventory();
                break;
        }
    }
}