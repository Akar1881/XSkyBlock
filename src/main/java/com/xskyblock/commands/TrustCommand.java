package com.xskyblock.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.xskyblock.XSkyBlock;
import com.xskyblock.models.Island;
import com.xskyblock.trust.TrustValidator;
import com.xskyblock.utils.GuiUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TrustCommand extends SubCommand {
    private final XSkyBlock plugin;
    private final TrustValidator trustValidator;

    public TrustCommand(XSkyBlock plugin) {
        this.plugin = plugin;
        this.trustValidator = new TrustValidator(plugin);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(GuiUtils.color(getUsage()));
            return;
        }

        Island island = plugin.getIslandManager().getIsland(player);
        if (island == null) {
            player.sendMessage(GuiUtils.color("&cYou don't have an island!"));
            return;
        }

        String action = args[1].toLowerCase();
        String targetName = args[2];
        Player targetPlayer = Bukkit.getPlayer(targetName);

        if (targetPlayer == null) {
            player.sendMessage(GuiUtils.color("&cPlayer not found!"));
            return;
        }

        switch (action) {
            case "add":
                if (!trustValidator.canBeTrusted(player, targetPlayer)) {
                    if (player.getUniqueId().equals(targetPlayer.getUniqueId())) {
                        player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("cannot-trust-self")));
                    } else {
                        player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("already-trusted-elsewhere")));
                    }
                    return;
                }
                
                // Create trust request
                plugin.getTrustRequestManager().createRequest(player, targetPlayer, island);
                player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("trust-request-sent")
                        .replace("%player%", targetPlayer.getName())));
                break;

            case "remove":
                if (island.isPlayerTrusted(targetPlayer.getUniqueId())) {
                    island.removeTrustedPlayer(targetPlayer.getUniqueId());
                    plugin.getIslandManager().saveIslandAsync(island);
                    player.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("player-untrusted")
                            .replace("%player%", targetPlayer.getName())));
                    targetPlayer.sendMessage(GuiUtils.color(plugin.getConfigManager().getMessage("untrusted-notification")
                            .replace("%island%", island.getName())));
                }
                break;

            default:
                player.sendMessage(GuiUtils.color(getUsage()));
        }
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return Arrays.asList("add", "remove");
        } else if (args.length == 3) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Manage trusted players on your island";
    }

    @Override
    public String getUsage() {
        return "/xmi trust <add|remove> <player>";
    }
}