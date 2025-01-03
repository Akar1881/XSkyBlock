package com.xmyisland.protection;

import com.xmyisland.XMyIsland;
import com.xmyisland.models.Island;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wither;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class WitherProtector {
    private final XMyIsland plugin;

    public WitherProtector(XMyIsland plugin) {
        this.plugin = plugin;
    }

    public void handleWitherDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        
        // Check if damage is caused by a Wither or Wither Skull
        if (damager instanceof Wither || 
            damager.getType() == EntityType.WITHER_SKULL) {
            
            // Check if target is in an island
            Island targetIsland = plugin.getIslandManager().getIslandAt(
                event.getEntity().getLocation()
            );
            
            if (targetIsland != null) {
                event.setCancelled(true);
            }
        }
    }
}