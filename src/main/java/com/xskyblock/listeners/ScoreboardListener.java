package com.xskyblock.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.xskyblock.XSkyBlock;

import org.bukkit.event.player.PlayerChangedWorldEvent;

public class ScoreboardListener implements Listener {
    private final XSkyBlock plugin;

    public ScoreboardListener(XSkyBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getScoreboardManager().updateScoreboard(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.getPlayer().setScoreboard(
            plugin.getServer().getScoreboardManager().getNewScoreboard()
        );
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        plugin.getScoreboardManager().updateScoreboard(event.getPlayer());
    }
}