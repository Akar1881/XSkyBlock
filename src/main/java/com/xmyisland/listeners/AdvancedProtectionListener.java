package com.xmyisland.listeners;

import com.xmyisland.XMyIsland;
import com.xmyisland.protection.RedstoneProtector;
import com.xmyisland.protection.DispenserItemProtector;
import com.xmyisland.protection.WitherProtector;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class AdvancedProtectionListener implements Listener {
    private final RedstoneProtector redstoneProtector;
    private final DispenserItemProtector dispenserItemProtector;
    private final WitherProtector witherProtector;

    public AdvancedProtectionListener(XMyIsland plugin) {
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