package com.xskyblock.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

import com.xskyblock.XSkyBlock;
import com.xskyblock.protection.TreeGrowthProtector;

public class TreeGrowthListener implements Listener {
    private final TreeGrowthProtector protector;

    public TreeGrowthListener(XSkyBlock plugin) {
        this.protector = new TreeGrowthProtector(plugin);
    }

    @EventHandler
    public void onStructureGrow(StructureGrowEvent event) {
        protector.handleTreeGrowth(event);
    }
}