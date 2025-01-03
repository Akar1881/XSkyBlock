package com.xmyisland.listeners;

import com.xmyisland.XMyIsland;
import com.xmyisland.protection.BlockProtector;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class BlockProtectionListener implements Listener {
    private final BlockProtector protector;

    public BlockProtectionListener(XMyIsland plugin) {
        this.protector = new BlockProtector(plugin);
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        protector.handlePistonExtend(event);
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        protector.handlePistonRetract(event);
    }

    @EventHandler
    public void onDispense(BlockDispenseEvent event) {
        protector.handleDispense(event);
    }
}