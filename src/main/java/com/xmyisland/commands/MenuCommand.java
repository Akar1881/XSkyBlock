package com.xmyisland.commands;

import com.xmyisland.XMyIsland;
import com.xmyisland.utils.GuiUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MenuCommand extends SubCommand {
    private final XMyIsland plugin;

    public MenuCommand(XMyIsland plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!plugin.getIslandManager().hasIsland(player)) {
            player.sendMessage(GuiUtils.color("&cYou don't have an island! Use /xmi claim <name> to claim one."));
            return;
        }

        plugin.getGuiManager().openIslandMenu(player);
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Open the island management menu";
    }

    @Override
    public String getUsage() {
        return "/xmi menu";
    }
}