package com.xskyblock.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import com.xskyblock.XSkyBlock;
import com.xskyblock.protection.BlockProtector;

public class BlockProtectionListener implements Listener {
    private final BlockProtector protector;

    public BlockProtectionListener(XSkyBlock plugin) {
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