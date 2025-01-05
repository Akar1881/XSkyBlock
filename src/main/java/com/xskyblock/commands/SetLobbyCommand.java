package com.xskyblock.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.xskyblock.XSkyBlock;
import com.xskyblock.utils.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public class SetLobbyCommand extends SubCommand {
    private final XSkyBlock plugin;

    public SetLobbyCommand(XSkyBlock plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("xskyblock.admin")) {
            player.sendMessage(GuiUtils.color("&cYou don't have permission to use this command!"));
            return;
        }

        plugin.getConfigManager().setLobbyLocation(player.getLocation());
        player.sendMessage(GuiUtils.color("&aLobby location has been set!"));
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Set the lobby location";
    }

    @Override
    public String getUsage() {
        return "/xmi setlobby";
    }
}