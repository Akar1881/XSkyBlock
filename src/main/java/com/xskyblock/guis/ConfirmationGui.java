package com.xskyblock.guis;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.xskyblock.XSkyBlock;
import com.xskyblock.utils.GuiUtils;

public class ConfirmationGui extends BaseGui {
    private final Runnable onConfirm;

    public ConfirmationGui(XSkyBlock plugin, Player player, Runnable onConfirm) {
        super(plugin, player);
        this.onConfirm = onConfirm;
    }

    @Override
    public void initialize() {
        inventory = Bukkit.createInventory(this, 9, GuiUtils.color("&8Confirm Action"));

        inventory.setItem(3, GuiUtils.createGuiItem(
            Material.LIME_WOOL,
            "&aConfirm",
            "&7Click to confirm"
        ));

        inventory.setItem(5, GuiUtils.createGuiItem(
            Material.RED_WOOL,
            "&cCancel",
            "&7Click to cancel"
        ));
    }

    @Override
    public void handleClick(int slot) {
        if (slot == 3) {
            onConfirm.run();
        }
        player.closeInventory();
    }
}