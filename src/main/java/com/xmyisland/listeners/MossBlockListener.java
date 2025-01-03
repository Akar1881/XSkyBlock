package com.xmyisland.listeners;

import com.xmyisland.XMyIsland;
import com.xmyisland.protection.MossBlockProtector;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class MossBlockListener implements Listener {
    private final MossBlockProtector protector;

    public MossBlockListener(XMyIsland plugin) {
        this.protector = new MossBlockProtector(plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        protector.handleBoneMealUse(event);
    }
}