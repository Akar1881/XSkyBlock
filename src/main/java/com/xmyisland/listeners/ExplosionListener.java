package com.xmyisland.listeners;

import com.xmyisland.XMyIsland;
import com.xmyisland.protection.ExplosionProtector;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ExplosionListener implements Listener {
    private final ExplosionProtector protector;

    public ExplosionListener(XMyIsland plugin) {
        this.protector = new ExplosionProtector(plugin);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        protector.handleExplosion(event);
    }
}