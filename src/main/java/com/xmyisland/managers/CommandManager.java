package com.xmyisland.managers;

import com.xmyisland.XMyIsland;
import com.xmyisland.commands.*;
import com.xmyisland.commands.admin.ForceDeleteCommand;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager implements CommandExecutor, TabCompleter {
    private final XMyIsland plugin;
    private final Map<String, SubCommand> commands;

    public CommandManager(XMyIsland plugin) {
        this.plugin = plugin;
        this.commands = new HashMap<>();
        registerCommands();
    }

    private void registerCommands() {
        commands.put("claim", new ClaimCommand(plugin));
        commands.put("menu", new MenuCommand(plugin));
        commands.put("border", new BorderCommand(plugin));
        commands.put("trust", new TrustCommand(plugin));
        commands.put("help", new HelpCommand(plugin));
        commands.put("forcedel", new ForceDeleteCommand(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            commands.get("help").execute(player, args);
            return true;
        }

        SubCommand subCommand = commands.get(args[0].toLowerCase());
        if (subCommand == null) {
            commands.get("help").execute(player, args);
            return true;
        }

        subCommand.execute(player, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(commands.keySet());
        }
        
        SubCommand subCommand = commands.get(args[0].toLowerCase());
        if (subCommand != null) {
            return subCommand.getTabCompletions(sender, args);
        }

        return new ArrayList<>();
    }
}