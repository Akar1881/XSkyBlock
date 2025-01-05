package com.xskyblock.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.xskyblock.XSkyBlock;
import com.xskyblock.protection.ExplosionProtector;

public class ExplosionListener implements Listener {
    private final ExplosionProtector protector;

    public ExplosionListener(XSkyBlock plugin) {
        this.protector = new ExplosionProtector(plugin);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        protector.handleExplosion(event);
    }
}