package com.xmyisland.commands;

import com.xmyisland.XMyIsland;
import com.xmyisland.models.Island;
import com.xmyisland.utils.GuiUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BorderCommand extends SubCommand {
    private final XMyIsland plugin;

    public BorderCommand(XMyIsland plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(GuiUtils.color("&cUsage: /xmi border <show|hide>"));
            return;
        }

        Island island = plugin.getIslandManager().getIsland(player);
        if (island == null) {
            player.sendMessage(GuiUtils.color("&cYou don't have an island!"));
            return;
        }

        String action = args[1].toLowerCase();
        switch (action) {
            case "show":
                island.setBorderVisible(true);
                player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("border-shown")));
                break;
            case "hide":
                island.setBorderVisible(false);
                player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("border-hidden")));
                break;
            default:
                player.sendMessage(GuiUtils.color("&cUsage: /xmi border <show|hide>"));
        }
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return Arrays.asList("show", "hide");
        }
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Toggle island border visibility";
    }

    @Override
    public String getUsage() {
        return "/xmi border <show|hide>";
    }
}