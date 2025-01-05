package com.xskyblock.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.xskyblock.XSkyBlock;
import com.xskyblock.protection.MossBlockProtector;

public class MossBlockListener implements Listener {
    private final MossBlockProtector protector;

    public MossBlockListener(XSkyBlock plugin) {
        this.protector = new MossBlockProtector(plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        protector.handleBoneMealUse(event);
    }
}