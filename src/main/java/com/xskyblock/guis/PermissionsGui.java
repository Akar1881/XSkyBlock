package com.xskyblock.guis;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.xskyblock.XSkyBlock;
import com.xskyblock.models.Island;
import com.xskyblock.models.IslandMember;
import com.xskyblock.models.Permission;
import com.xskyblock.utils.GuiUtils;

import java.util.UUID;

public class PermissionsGui extends BaseGui {
    private final UUID targetPlayer;

    public PermissionsGui(XSkyBlock plugin, Player player, UUID targetPlayer) {
        super(plugin, player);
        this.targetPlayer = targetPlayer;
    }

    @Override
    public void initialize() {
        inventory = Bukkit.createInventory(this, 27, GuiUtils.color("&8Manage Permissions"));
        Island island = plugin.getIslandManager().getIsland(player);
        IslandMember member = island.getTrustedPlayers().get(targetPlayer);

        if (member == null) {
            player.closeInventory();
            return;
        }

        int slot = 0;
        for (Permission permission : Permission.values()) {
            boolean hasPermission = member.hasPermission(permission);
            inventory.setItem(slot++, GuiUtils.createGuiItem(
                hasPermission ? Material.LIME_DYE : Material.GRAY_DYE,
                (hasPermission ? "&a" : "&c") + permission.getDescription(),
                "&7Click to toggle"
            ));
        }

        // Kick Player Button
        inventory.setItem(26, GuiUtils.createGuiItem(
            Material.DARK_OAK_DOOR,
            "&cKick Player",
            "&7Remove player from trusted list"
        ));
    }

    @Override
    public void handleClick(int slot) {
        Island island = plugin.getIslandManager().getIsland(player);
        IslandMember member = island.getTrustedPlayers().get(targetPlayer);

        if (member == null) {
            player.closeInventory();
            return;
        }

        if (slot < Permission.values().length) {
            Permission permission = Permission.values()[slot];
            member.setPermission(permission, !member.hasPermission(permission));
            plugin.getIslandManager().saveIslandAsync(island); // Save changes
            
            // Update the clicked slot immediately
            boolean hasPermission = member.hasPermission(permission);
            inventory.setItem(slot, GuiUtils.createGuiItem(
                hasPermission ? Material.LIME_DYE : Material.GRAY_DYE,
                (hasPermission ? "&a" : "&c") + permission.getDescription(),
                "&7Click to toggle"
            ));
            
            player.updateInventory(); // Force client to update the inventory view
        } else if (slot == 26) {
            new ConfirmationGui(plugin, player, () -> {
                island.removeTrustedPlayer(targetPlayer);
                plugin.getIslandManager().saveIslandAsync(island); // Save changes
                player.closeInventory();
                player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("player-untrusted")
                    .replace("%player%", Bukkit.getOfflinePlayer(targetPlayer).getName())));
            }).open();
        }
    }
}