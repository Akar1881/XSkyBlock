package com.xskyblock.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.xskyblock.XSkyBlock;
import com.xskyblock.protection.DispenserItemProtector;
import com.xskyblock.protection.RedstoneProtector;
import com.xskyblock.protection.WitherProtector;

public class AdvancedProtectionListener implements Listener {
    private final RedstoneProtector redstoneProtector;
    private final DispenserItemProtector dispenserItemProtector;
    private final WitherProtector witherProtector;

    public AdvancedProtectionListener(XSkyBlock plugin) {
        this.redstoneProtector = new RedstoneProtector(plugin);
        this.dispenserItemProtector = new DispenserItemProtector();
        this.witherProtector = new WitherProtector(plugin);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        redstoneProtector.handleRedstonePlace(event);
    }

    @EventHandler
    public void onBlockDispense(BlockDispenseEvent event) {
        dispenserItemProtector.handleDispense(event);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        witherProtector.handleWitherDamage(event);
    }
}