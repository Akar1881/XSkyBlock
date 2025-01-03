package com.xmyisland.listeners;

import com.xmyisland.XMyIsland;
import com.xmyisland.protection.FallingBlockProtector;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class FallingBlockListener implements Listener {
    private final FallingBlockProtector protector;

    public FallingBlockListener(XMyIsland plugin) {
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