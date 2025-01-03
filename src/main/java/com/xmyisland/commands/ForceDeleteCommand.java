package com.xmyisland.commands.admin;

import com.xmyisland.XMyIsland;
import com.xmyisland.commands.SubCommand;
import com.xmyisland.models.Island;
import com.xmyisland.utils.GuiUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ForceDeleteCommand extends SubCommand {
    private final XMyIsland plugin;

    public ForceDeleteCommand(XMyIsland plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!player.isOp()) {
            player.sendMessage(GuiUtils.color("&cYou don't have permission to use this command!"));
            return;
        }

        Island island = plugin.getIslandManager().getIslandAt(player.getLocation());
        if (island == null) {
            player.sendMessage(GuiUtils.color("&cYou must be inside an island to force delete it!"));
            return;
        }

        plugin.getIslandManager().forceDeleteIsland(island);
        player.sendMessage(GuiUtils.color("&aSuccessfully force deleted the island!"));
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