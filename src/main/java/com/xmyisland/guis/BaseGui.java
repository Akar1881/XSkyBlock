package com.xmyisland.guis;

import com.xmyisland.XMyIsland;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class BaseGui implements InventoryHolder {
    protected final XMyIsland plugin;
    protected final Player player;
    protected Inventory inventory;

    public BaseGui(XMyIsland plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public abstract void initialize();
    public abstract void handleClick(int slot);

    public void open() {
        initialize();
        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}