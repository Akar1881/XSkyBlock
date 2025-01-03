package com.xmyisland.guis;

import com.xmyisland.XMyIsland;
import com.xmyisland.models.Island;
import com.xmyisland.models.IslandMember;
import com.xmyisland.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Map;
import java.util.UUID;

public class TrustedPlayersGui extends BaseGui {
    public TrustedPlayersGui(XMyIsland plugin, Player player) {
        super(plugin, player);
    }

    @Override
    public void initialize() {
        inventory = Bukkit.createInventory(this, 36, GuiUtils.color("&8Trusted Players"));
        Island island = plugin.getIslandManager().getIsland(player);

        int slot = 0;
        for (Map.Entry<UUID, IslandMember> entry : island.getTrustedPlayers().entrySet()) {
            UUID trustedId = entry.getKey();
            String trustedName = Bukkit.getOfflinePlayer(trustedId).getName();
            
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(trustedId));
            meta.setDisplayName(GuiUtils.color("&b" + trustedName));
            head.setItemMeta(meta);
            
            inventory.setItem(slot++, head);
        }
    }

    @Override
    public void handleClick(int slot) {
        Island island = plugin.getIslandManager().getIsland(player);
        if (slot >= 0 && slot < island.getTrustedPlayers().size()) {
            UUID trustedId = (UUID) island.getTrustedPlayers().keySet().toArray()[slot];
            new PermissionsGui(plugin, player, trustedId).open();
        }
    }
}