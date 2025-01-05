package com.xskyblock.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.xskyblock.XSkyBlock;
import com.xskyblock.guis.BaseGui;

public class GuiListener implements Listener {
    private final XSkyBlock plugin;

    public GuiListener(XSkyBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof BaseGui) {
            event.setCancelled(true);
            if (event.getRawSlot() < 0 || event.getRawSlot() >= event.getInventory().getSize()) {
                return;
            }
            ((BaseGui) event.getInventory().getHolder()).handleClick(event.getRawSlot());
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof BaseGui) {
            // Handle any cleanup if needed
        }
    }
}