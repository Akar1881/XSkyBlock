package com.xmyisland.commands;

import com.xmyisland.XMyIsland;
import com.xmyisland.utils.GuiUtils;
import com.xmyisland.utils.IslandBoundaryUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ClaimCommand extends SubCommand {
    private final XMyIsland plugin;

    public ClaimCommand(XMyIsland plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(GuiUtils.color("&cUsage: /xmi claim <name>"));
            return;
        }

        String name = args[1];
        
        // Check if player already has an island
        if (plugin.getIslandManager().hasIsland(player)) {
            player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("island-exists")));
            return;
        }

        // Check if player is trusted on another island
        if (plugin.getIslandManager().isPlayerTrustedAnywhere(player.getUniqueId())) {
            player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("already-trusted-elsewhere")));
            return;
        }

        // Check if the location would overlap with existing islands
        if (IslandBoundaryUtils.wouldOverlap(player.getLocation(), 
                plugin.getConfigManager().getDefaultIslandSize(), null)) {
            player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("island-overlap")));
            return;
        }

        if (plugin.getIslandManager().createIsland(player, name)) {
            player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("island-claimed")));
        }
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Claim your island";
    }

    @Override
    public String getUsage() {
        return "/xmi claim <name>";
    }
}