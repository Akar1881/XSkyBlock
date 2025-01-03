package com.xmyisland.listeners;

import com.xmyisland.XMyIsland;
import com.xmyisland.protection.LiquidProtector;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class LiquidListener implements Listener {
    private final LiquidProtector protector;

    public LiquidListener(XMyIsland plugin) {
        this.protector = new LiquidProtector(plugin);
    }

    @EventHandler
    public void onLiquidFlow(BlockFromToEvent event) {
        protector.handleLiquidFlow(event);
    }
}