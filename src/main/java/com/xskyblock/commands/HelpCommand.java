package com.xskyblock.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.xskyblock.XSkyBlock;
import com.xskyblock.utils.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends SubCommand {
    private final XSkyBlock plugin;

    public HelpCommand(XSkyBlock plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        player.sendMessage(GuiUtils.color("&8&l=== &b&lXSkyBlock Help &8&l==="));
        
        // Island Creation and Management
        player.sendMessage(GuiUtils.color("&b&lIsland Management:"));
        player.sendMessage(GuiUtils.color("&b/xmi claim <name> &7- Create your own island"));
        player.sendMessage(GuiUtils.color("&b/xmi menu &7- Open island management menu"));
        player.sendMessage(GuiUtils.color("&b/xmi island &7- Open island navigation menu"));
        
        // Trust System
        player.sendMessage(GuiUtils.color("\n&b&lTrust System:"));
        player.sendMessage(GuiUtils.color("&b/xmi trust add <player> &7- Trust a player to your island"));
        player.sendMessage(GuiUtils.color("&b/xmi trust remove <player> &7- Remove a trusted player"));
        
        // Navigation
        player.sendMessage(GuiUtils.color("\n&b&lNavigation:"));
        player.sendMessage(GuiUtils.color("&b/xmi lobby &7- Teleport to the server lobby"));
        
        // Visual Settings
        player.sendMessage(GuiUtils.color("\n&b&lVisual Settings:"));
        player.sendMessage(GuiUtils.color("&b/xmi border show &7- Show island border"));
        player.sendMessage(GuiUtils.color("&b/xmi border hide &7- Hide island border"));
        
        // Admin Commands
        if (player.hasPermission("xskyblock.admin")) {
            player.sendMessage(GuiUtils.color("\n&c&lAdmin Commands:"));
            player.sendMessage(GuiUtils.color("&c/xmi setlobby &7- Set the lobby location"));
            player.sendMessage(GuiUtils.color("&c/xmi forcedel &7- Force delete an island"));
            player.sendMessage(GuiUtils.color("&c/xmi schem change &7- Save current selection as island schematic"));
            player.sendMessage(GuiUtils.color("&c/xmi schem spawn &7- Set island spawn point"));
        }
        
        player.sendMessage(GuiUtils.color("\n&8&l=== &7Page 1/1 &8&l==="));
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Show help message";
    }

    @Override
    public String getUsage() {
        return "/xmi help";
    }
}