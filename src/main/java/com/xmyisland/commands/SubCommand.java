package com.xmyisland.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class SubCommand {
    public abstract void execute(Player player, String[] args);
    public abstract List<String> getTabCompletions(CommandSender sender, String[] args);
    public abstract String getDescription();
    public abstract String getUsage();
}