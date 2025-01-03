package com.xmyisland.guis;

import com.xmyisland.XMyIsland;
import com.xmyisland.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ConfirmationGui extends BaseGui {
    private final Runnable onConfirm;

    public ConfirmationGui(XMyIsland plugin, Player player, Runnable onConfirm) {
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