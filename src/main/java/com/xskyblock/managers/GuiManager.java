package com.xskyblock.managers;

import org.bukkit.entity.Player;

import com.xskyblock.XSkyBlock;
import com.xskyblock.guis.IslandMenuGui;
import com.xskyblock.guis.PermissionsGui;
import com.xskyblock.guis.TrustedPlayersGui;

import java.util.UUID;

public class GuiManager {
    private final XSkyBlock plugin;

    public GuiManager(XSkyBlock plugin) {
        this.plugin = plugin;
    }

    public void openIslandMenu(Player player) {
        new IslandMenuGui(plugin, player).open();
    }

    public void openPermissionsGui(Player player, UUID targetPlayer) {
        new PermissionsGui(plugin, player, targetPlayer).open();
    }

    public void openTrustedPlayersGui(Player player) {
        new TrustedPlayersGui(plugin, player).open();
    }
}