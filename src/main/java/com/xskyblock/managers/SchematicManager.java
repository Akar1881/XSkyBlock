package com.xskyblock.managers;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.xskyblock.XSkyBlock;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;

public class SchematicManager {
    private final XSkyBlock plugin;
    private Clipboard islandSchematic;
    private Location referencePoint;
    private Location spawnOffset;
    private final File spawnConfig;

    public SchematicManager(XSkyBlock plugin) {
        this.plugin = plugin;
        this.spawnConfig = new File(plugin.getDataFolder(), "spawn.yml");
        loadSchematic();
        loadSpawnPoint();
    }

    private void loadSpawnPoint() {
        if (!spawnConfig.exists()) return;
        YamlConfiguration config = YamlConfiguration.loadConfiguration(spawnConfig);
        if (config.contains("reference")) {
            this.referencePoint = config.getLocation("reference");
        }
        if (config.contains("spawn")) {
            Location spawn = config.getLocation("spawn");
            if (referencePoint != null && spawn != null) {
                this.spawnOffset = new Location(
                    spawn.getWorld(),
                    spawn.getX() - referencePoint.getX(),
                    spawn.getY() - referencePoint.getY(),
                    spawn.getZ() - referencePoint.getZ()
                );
            }
        }
    }

    public void setSpawnPoint(Location location) {
        try {
            com.sk89q.worldedit.entity.Player wePlayer = BukkitAdapter.adapt(location.getWorld().getPlayers().get(0));
            Region region = WorldEdit.getInstance().getSessionManager()
                .get(wePlayer)
                .getSelection(wePlayer.getWorld());

            if (region != null) {
                BlockVector3 min = region.getMinimumPoint();
                this.referencePoint = new Location(
                    location.getWorld(),
                    min.getX(),
                    min.getY(),
                    min.getZ()
                );
                
                this.spawnOffset = new Location(
                    location.getWorld(),
                    location.getX() - referencePoint.getX(),
                    location.getY() - referencePoint.getY(),
                    location.getZ() - referencePoint.getZ()
                );

                YamlConfiguration config = YamlConfiguration.loadConfiguration(spawnConfig);
                config.set("reference", referencePoint);
                config.set("spawn", location);
                try {
                    config.save(spawnConfig);
                } catch (Exception e) {
                    plugin.getLogger().severe("Failed to save spawn point!");
                    e.printStackTrace();
                }
            }
        } catch (IncompleteRegionException e) {
            plugin.getLogger().warning("No WorldEdit selection found!");
        }
    }

    private void loadSchematic() {
        File schematic = new File(plugin.getDataFolder(), "schematics/island.schem");
        if (!schematic.exists()) {
            plugin.getLogger().warning("No island schematic found! Please create one using /xmi schem change");
            return;
        }

        try {
            ClipboardFormat format = ClipboardFormats.findByFile(schematic);
            try (ClipboardReader reader = format.getReader(new FileInputStream(schematic))) {
                islandSchematic = reader.read();
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to load island schematic!");
            e.printStackTrace();
        }
    }

    public void pasteSchematic(Location location) {
        if (islandSchematic == null) {
            loadSchematic();
            if (islandSchematic == null) {
                return;
            }
        }

        try {
            try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(location.getWorld()))) {
                Operation operation = new ClipboardHolder(islandSchematic)
                    .createPaste(editSession)
                    .to(BlockVector3.at(location.getX(), location.getY(), location.getZ()))
                    .build();
                    
                Operations.complete(operation);
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to paste island schematic!");
            e.printStackTrace();
        }
    }

    public void reloadSchematic() {
        loadSchematic();
    }

    public Location getRelativeSpawnPoint(Location islandLocation) {
        if (spawnOffset == null) {
            return islandLocation.clone().add(0.5, 1, 0.5);
        }

        return islandLocation.clone().add(
            spawnOffset.getX(),
            spawnOffset.getY(),
            spawnOffset.getZ()
        ).add(0.5, 0, 0.5);
    }
}