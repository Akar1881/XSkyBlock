package com.xskyblock.guis;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.xskyblock.XSkyBlock;
import com.xskyblock.models.Island;
import com.xskyblock.utils.GuiUtils;
import com.xskyblock.utils.IslandBoundaryUtils;

public class IslandMenuGui extends BaseGui {
    private static final int EXPAND_SLOT = 11;
    private static final int TRUSTED_SLOT = 13;
    private static final int DELETE_SLOT = 15;

    public IslandMenuGui(XSkyBlock plugin, Player player) {
        super(plugin, player);
    }

    @Override
    public void initialize() {
        inventory = Bukkit.createInventory(this, 27, GuiUtils.color("&8Island Management"));
        Island island = plugin.getIslandManager().getIsland(player);

        // Expand Island Button
        double nextUpgradeCost = plugin.getConfigManager().getPricePerBlock() * (island.getSize() + 2);
        int maxSize = plugin.getConfigManager().getMaxIslandSize();
        
        if (island.getSize() >= maxSize) {
            // Show max size reached button
            inventory.setItem(EXPAND_SLOT, GuiUtils.createGuiItem(
                Material.RED_STAINED_GLASS_PANE,
                "&cMaximum Size Reached",
                "&7Your island is at maximum size",
                "&7Current size: &f" + island.getSize() + "x" + island.getSize(),
                "&7Maximum size: &f" + maxSize + "x" + maxSize
            ));
        } else {
            // Show expand button
            inventory.setItem(EXPAND_SLOT, GuiUtils.createGuiItem(
                Material.GREEN_STAINED_GLASS_PANE,
                "&aExpand Island",
                "&7Current size: &f" + island.getSize() + "x" + island.getSize(),
                "&7Cost: &f$" + nextUpgradeCost,
                "&7Maximum size: &f" + maxSize + "x" + maxSize
            ));
        }

        // Trusted Players Button
        inventory.setItem(TRUSTED_SLOT, GuiUtils.createGuiItem(
            Material.PLAYER_HEAD,
            "&bTrusted Players",
            "&7Click to manage trusted players"
        ));

        // Delete Island Button
        inventory.setItem(DELETE_SLOT, GuiUtils.createGuiItem(
            Material.RED_WOOL,
            "&cDelete Island",
            "&7Click to delete your island",
            "&7You will receive a &f" + plugin.getConfigManager().getRefundPercentage() + "% &7refund"
        ));
    }

    @Override
    public void handleClick(int slot) {
        switch (slot) {
            case EXPAND_SLOT:
                Island island = plugin.getIslandManager().getIsland(player);
                int maxSize = plugin.getConfigManager().getMaxIslandSize();
                
                // Check if island is already at max size
                if (island.getSize() >= maxSize) {
                    player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("max-size-reached")));
                    player.closeInventory();
                    return;
                }
                
                int newSize = island.getSize() + 2;
                
                // Double check the new size against max size
                if (newSize > maxSize) {
                    player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("max-size-reached")));
                    player.closeInventory();
                    return;
                }
                
                // Check if expansion would overlap with other islands
                if (IslandBoundaryUtils.wouldOverlap(island.getCenter(), newSize, island)) {
                    player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("expand-overlap")));
                    player.closeInventory();
                    return;
                }

                new ConfirmationGui(plugin, player, () -> {
                    double cost = plugin.getConfigManager().getPricePerBlock() * newSize;
                    
                    if (plugin.getEconomyManager().getEconomy().withdrawPlayer(player, cost).transactionSuccess()) {
                        island.setSize(newSize);
                        plugin.getIslandManager().saveIslandAsync(island);
                        player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("island-expanded")
                            .replace("%size%", String.valueOf(newSize))));
                    } else {
                        player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("insufficient-funds")));
                    }
                }).open();
                break;

            case TRUSTED_SLOT:
                new TrustedPlayersGui(plugin, player).open();
                break;

            case DELETE_SLOT:
                new ConfirmationGui(plugin, player, () -> {
                    plugin.getIslandDeleteManager().deleteIsland(player, false);
                    player.closeInventory();
                }).open();
                break;
        }
    }
}