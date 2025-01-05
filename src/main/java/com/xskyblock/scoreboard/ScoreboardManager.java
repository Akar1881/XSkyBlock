package com.xskyblock.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.xskyblock.XSkyBlock;
import com.xskyblock.models.Island;
import com.xskyblock.utils.GuiUtils;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreboardManager {
    private final XSkyBlock plugin;
    private final YamlConfiguration config;
    private final String title;
    private final List<String> lines;

    public ScoreboardManager(XSkyBlock plugin) {
        this.plugin = plugin;
        File configFile = new File(plugin.getDataFolder(), "scoreboard.yml");
        plugin.saveResource("scoreboard.yml", false);
        this.config = YamlConfiguration.loadConfiguration(configFile);
        this.title = config.getString("title", "&bYour Server Name");
        this.lines = config.getStringList("lines");
        
        // Start scoreboard update task
        startUpdateTask();
    }

    private void startUpdateTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (isInValidWorld(player)) {
                    updateScoreboard(player);
                }
            }
        }, 20L, 20L); // Update every second
    }

    private boolean isInValidWorld(Player player) {
        World world = player.getWorld();
        World lobby = plugin.getConfigManager().getLobbyLocation().getWorld();
        return world.getName().equals("XSkyBlock") || 
               (lobby != null && world.equals(lobby));
    }

    public void updateScoreboard(Player player) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("XSkyBlock", "dummy", GuiUtils.color(title));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        int score = lines.size();
        for (String line : lines) {
            String processed = processPlaceholders(line, player);
            obj.getScore(GuiUtils.color(processed)).setScore(score--);
        }

        player.setScoreboard(board);
    }

    private String processPlaceholders(String line, Player player) {
        // Get player's island and trusted island
        Island playerIsland = plugin.getIslandManager().getIsland(player);
        Island trustedIsland = findTrustedIsland(player);

        // Replace placeholders
        line = line.replace("%xmi_onlines%", String.valueOf(getOnlinePlayersInValidWorlds()));
        
        // Player's island placeholders
        if (playerIsland != null) {
            line = line.replace("%island_name%", playerIsland.getName());
            line = line.replace("%island_size%", playerIsland.getSize() + "x" + playerIsland.getSize());
            line = line.replace("%island_players%", String.valueOf(playerIsland.getTrustedPlayers().size()));
        } else {
            line = line.replace("%island_name%", "None");
            line = line.replace("%island_size%", "0x0");
            line = line.replace("%island_players%", "0");
        }

        // Trusted island placeholders
        if (trustedIsland != null) {
            line = line.replace("%trusted_island_name%", trustedIsland.getName());
            line = line.replace("%trusted_island_size%", trustedIsland.getSize() + "x" + trustedIsland.getSize());
            line = line.replace("%trusted_island_players%", String.valueOf(trustedIsland.getTrustedPlayers().size()));
        } else {
            line = line.replace("%trusted_island_name%", "None");
            line = line.replace("%trusted_island_size%", "0x0");
            line = line.replace("%trusted_island_players%", "0");
        }

        return line;
    }

    private int getOnlinePlayersInValidWorlds() {
        return (int) Bukkit.getOnlinePlayers().stream()
            .filter(this::isInValidWorld)
            .count();
    }

    private Island findTrustedIsland(Player player) {
        return plugin.getIslandManager().getIslands().values().stream()
            .filter(island -> island.getTrustedPlayers().containsKey(player.getUniqueId()))
            .findFirst()
            .orElse(null);
    }
}