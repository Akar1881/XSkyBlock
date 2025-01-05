package com.xskyblock.trust;

import org.bukkit.entity.Player;
import java.util.UUID;

public class TrustRequest {
    private final UUID targetPlayer;
    private final UUID islandOwner;
    private final String islandName;
    private final long timestamp;

    public TrustRequest(UUID targetPlayer, UUID islandOwner, String islandName) {
        this.targetPlayer = targetPlayer;
        this.islandOwner = islandOwner;
        this.islandName = islandName;
        this.timestamp = System.currentTimeMillis();
    }

    public UUID getTargetPlayer() {
        return targetPlayer;
    }

    public UUID getIslandOwner() {
        return islandOwner;
    }

    public String getIslandName() {
        return islandName;
    }

    public boolean hasExpired() {
        return System.currentTimeMillis() - timestamp > 15000; // 15 seconds
    }
}