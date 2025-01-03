package com.xmyisland.listeners;

import com.xmyisland.XMyIsland;
import com.xmyisland.protection.TreeGrowthProtector;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

public class TreeGrowthListener implements Listener {
    private final TreeGrowthProtector protector;

    public TreeGrowthListener(XMyIsland plugin) {
        this.protector = new TreeGrowthProtector(plugin);
    }

    @EventHandler
    public void onStructureGrow(StructureGrowEvent event) {
        protector.handleTreeGrowth(event);
    }
}