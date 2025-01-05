package com.xskyblock.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.xskyblock.XSkyBlock;
import com.xskyblock.utils.GuiUtils;
import com.xskyblock.utils.IslandBoundaryUtils;

import java.util.ArrayList;
import java.util.List;

public class ClaimCommand extends SubCommand {
    private final XSkyBlock plugin;

    public ClaimCommand(XSkyBlock plugin) {
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

        // Check if the location would overlap with existing islands
        if (IslandBoundaryUtils.wouldOverlap(player.getLocation(), 
                plugin.getConfigManager().getDefaultIslandSize(), null)) {
            player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("island-overlap")));
            return;
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