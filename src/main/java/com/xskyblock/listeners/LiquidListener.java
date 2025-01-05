package com.xskyblock.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import com.xskyblock.XSkyBlock;
import com.xskyblock.protection.LiquidProtector;

public class LiquidListener implements Listener {
    private final LiquidProtector protector;

    public LiquidListener(XSkyBlock plugin) {
        this.protector = new LiquidProtector(plugin);
    }

    @EventHandler
    public void onLiquidFlow(BlockFromToEvent event) {
        protector.handleLiquidFlow(event);
    }
}