package com.xskyblock.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.xskyblock.XSkyBlock;
import com.xskyblock.commands.SubCommand;
import com.xskyblock.utils.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public class ForceDeleteCommand extends SubCommand {
    private final XSkyBlock plugin;

    public ForceDeleteCommand(XSkyBlock plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!player.isOp()) {
            player.sendMessage(GuiUtils.color("&cYou don't have permission to use this command!"));
            return;
        }

        plugin.getIslandDeleteManager().deleteIsland(player, true);
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Force delete an island (Admin only)";
    }

    @Override
    public String getUsage() {
        return "/xmi forcedel";
    }
}