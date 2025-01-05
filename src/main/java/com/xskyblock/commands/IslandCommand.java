package com.xskyblock.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.xskyblock.XSkyBlock;
import com.xskyblock.guis.IslandNavigationGui;
import com.xskyblock.utils.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public class IslandCommand extends SubCommand {
    private final XSkyBlock plugin;

    public IslandCommand(XSkyBlock plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        new IslandNavigationGui(plugin, player).open();
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Open island navigation menu";
    }

    @Override
    public String getUsage() {
        return "/xmi island";
    }
}