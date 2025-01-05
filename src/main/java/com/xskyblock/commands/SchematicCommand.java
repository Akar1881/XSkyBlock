package com.xskyblock.commands;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.Region;
import com.xskyblock.XSkyBlock;
import com.xskyblock.utils.GuiUtils;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SchematicCommand extends SubCommand implements Listener {
    private final XSkyBlock plugin;
    private final List<UUID> spawnPointSetters = new ArrayList<>();

    public SchematicCommand(XSkyBlock plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("xskyblock.admin")) {
            player.sendMessage(GuiUtils.color("&cYou don't have permission to use this command!"));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(GuiUtils.color("&cUsage: /xmi schem <change|spawn>"));
            return;
        }

        switch (args[1].toLowerCase()) {
            case "change":
                saveSchematic(player);
                break;
            case "spawn":
                giveSpawnMarker(player);
                break;
            default:
                player.sendMessage(GuiUtils.color("&cUsage: /xmi schem <change|spawn>"));
        }
    }

    private void giveSpawnMarker(Player player) {
        ItemStack marker = new ItemStack(Material.GOLDEN_SHOVEL);
        ItemMeta meta = marker.getItemMeta();
        meta.setDisplayName(GuiUtils.color("&6SpawnPoint Marker"));
        meta.setLore(Arrays.asList(
            GuiUtils.color("&7Break a block to set the"),
            GuiUtils.color("&7island spawn point")
        ));
        marker.setItemMeta(meta);
        
        player.getInventory().addItem(marker);
        spawnPointSetters.add(player.getUniqueId());
        player.sendMessage(GuiUtils.color("&aBreak a block to set the spawn point!"));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!spawnPointSetters.contains(player.getUniqueId())) return;
        
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.GOLDEN_SHOVEL) return;
        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;
        if (!item.getItemMeta().getDisplayName().equals(GuiUtils.color("&6SpawnPoint Marker"))) return;

        event.setCancelled(true);
        plugin.getSchematicManager().setSpawnPoint(event.getBlock().getLocation());
        spawnPointSetters.remove(player.getUniqueId());
        player.sendMessage(GuiUtils.color("&aSpawn point set! Type &e/xmi schem change &ato update the island schematic."));
    }

    private void saveSchematic(Player player) {
        try {
            com.sk89q.worldedit.entity.Player wePlayer = BukkitAdapter.adapt(player);
            Region region = WorldEdit.getInstance().getSessionManager()
                .get(wePlayer)
                .getSelection(wePlayer.getWorld());

            if (region == null) {
                player.sendMessage(GuiUtils.color("&cPlease make a WorldEdit selection first!"));
                return;
            }

            File dir = new File(plugin.getDataFolder(), "schematics");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
            
            try (EditSession editSession = WorldEdit.getInstance().newEditSession(wePlayer.getWorld())) {
                ForwardExtentCopy copy = new ForwardExtentCopy(
                    editSession, 
                    region, 
                    clipboard, 
                    region.getMinimumPoint()
                );
                Operations.complete(copy);
            }

            File schematic = new File(dir, "island.schem");
            try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(schematic))) {
                writer.write(clipboard);
            }

            player.sendMessage(GuiUtils.color("&aSuccessfully saved island schematic!"));
            plugin.getSchematicManager().reloadSchematic();
            
        } catch (Exception e) {
            player.sendMessage(GuiUtils.color("&cError saving schematic: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return Arrays.asList("change", "spawn");
        }
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Manage island schematics";
    }

    @Override
    public String getUsage() {
        return "/xmi schem <change|spawn>";
    }
}