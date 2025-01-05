package com.xskyblock.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.xskyblock.XSkyBlock;
import com.xskyblock.utils.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public class LobbyCommand extends SubCommand {
    private final XSkyBlock plugin;

    public LobbyCommand(XSkyBlock plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        Location lobby = plugin.getConfigManager().getLobbyLocation();
        if (lobby == null) {
            player.sendMessage(GuiUtils.color("&cLobby has not been set! Ask an admin to set it using /xmi setlobby"));
            return;
        }

        player.teleport(lobby);
        player.sendMessage(GuiUtils.color("&aTeleported to the lobby!"));
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Teleport to the lobby";
    }

    @Override
    public String getUsage() {
        return "/xmi lobby";
    }
}