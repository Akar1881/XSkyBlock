package com.xmyisland.managers;

import com.xmyisland.XMyIsland;
import com.xmyisland.guis.IslandMenuGui;
import com.xmyisland.guis.PermissionsGui;
import com.xmyisland.guis.TrustedPlayersGui;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GuiManager {
    private final XMyIsland plugin;

    public GuiManager(XMyIsland plugin) {
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