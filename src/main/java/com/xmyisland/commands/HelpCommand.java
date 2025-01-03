package com.xmyisland.commands;

import com.xmyisland.XMyIsland;
import com.xmyisland.utils.GuiUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends SubCommand {
    private final XMyIsland plugin;

    public HelpCommand(XMyIsland plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        player.sendMessage(GuiUtils.color("&8=== &bXMyIsland Help &8==="));
        player.sendMessage(GuiUtils.color("&b/xmi claim <name> &7- Claim your island"));
        player.sendMessage(GuiUtils.color("&b/xmi menu &7- Open island management menu"));
        player.sendMessage(GuiUtils.color("&b/xmi border <show|hide> &7- Toggle border visibility"));
        player.sendMessage(GuiUtils.color("&b/xmi trust <add|remove> <player> &7- Manage trusted players"));
        player.sendMessage(GuiUtils.color("&b/xmi help &7- Show this help message"));
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