package com.xmyisland.guis;

import com.xmyisland.XMyIsland;
import com.xmyisland.models.Island;
import com.xmyisland.utils.GuiUtils;
import com.xmyisland.utils.IslandBoundaryUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class IslandMenuGui extends BaseGui {
    private static final int EXPAND_SLOT = 11;
    private static final int TRUSTED_SLOT = 13;
    private static final int DELETE_SLOT = 15;

    public IslandMenuGui(XMyIsland plugin, Player player) {
        super(plugin, player);
    }

    @Override
    public void initialize() {
        inventory = Bukkit.createInventory(this, 27, GuiUtils.color("&8Island Management"));
        Island island = plugin.getIslandManager().getIsland(player);

        // Expand Island Button
        double nextUpgradeCost = plugin.getConfigManager().getPricePerBlock() * (island.getSize() + 2);
        inventory.setItem(EXPAND_SLOT, GuiUtils.createGuiItem(
            Material.GREEN_STAINED_GLASS_PANE,
            "&aExpand Island",
            "&7Current size: &f" + island.getSize() + "x" + island.getSize(),
            "&7Cost: &f$" + nextUpgradeCost
        ));

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
                int newSize = island.getSize() + 2;
                
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
                    }
                }).open();
                break;

            case TRUSTED_SLOT:
                new TrustedPlayersGui(plugin, player).open();
                break;

            case DELETE_SLOT:
                new ConfirmationGui(plugin, player, () -> {
                    Island islandToDelete = plugin.getIslandManager().getIsland(player);
                    double refund = calculateRefund(islandToDelete);
                    plugin.getEconomyManager().getEconomy().depositPlayer(player, refund);
                    plugin.getIslandManager().deleteIsland(player);
                    player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("island-deleted")
                        .replace("%amount%", String.valueOf(refund))));
                }).open();
                break;
        }
    }

    private double calculateRefund(Island island) {
        double totalCost = 0;
        int baseSize = 9;
        for (int size = baseSize + 2; size <= island.getSize(); size += 2) {
            totalCost += plugin.getConfigManager().getPricePerBlock() * size;
        }
        return totalCost * (plugin.getConfigManager().getRefundPercentage() / 100.0);
    }
}