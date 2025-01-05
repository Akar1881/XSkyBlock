package com.xskyblock.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.xskyblock.XSkyBlock;
import com.xskyblock.protection.FallingBlockProtector;

public class FallingBlockListener implements Listener {
    private final FallingBlockProtector protector;

    public FallingBlockListener(XSkyBlock plugin) {
        this.protector = new FallingBlockProtector(plugin);
    }

    @EventHandler
    public void onFallingBlockLand(EntityChangeBlockEvent event) {
        protector.handleFallingBlockLand(event);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        protector.handleExplosion(event);
    }
}