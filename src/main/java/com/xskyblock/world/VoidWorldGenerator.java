package com.xskyblock.world;

import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.BlockPopulator;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class VoidWorldGenerator extends ChunkGenerator {
    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        return createChunkData(world);
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Collections.emptyList();
    }

    @Override
    public boolean canSpawn(World world, int x, int z) {
        return true;
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0, 100, 0);
    }
}