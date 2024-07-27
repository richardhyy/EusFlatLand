package cc.eumc.eusflatland.generator;

import cc.eumc.eusflatland.EusFlatLand;
import cc.eumc.eusflatland.FlatLandStructure;
import cc.eumc.eusflatland.generator.populator.GrassPopulator;
import cc.eumc.eusflatland.generator.populator.SaplingPopulator;
import cc.eumc.eusflatland.generator.populator.StructurePopulator;
import cc.eumc.eusflatland.generator.populators.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class FlatLandGenerator extends ChunkGenerator {
    private final EusFlatLand plugin;
    private final List<FlatLandStructure> structures;

    public FlatLandGenerator(EusFlatLand plugin, List<FlatLandStructure> structures) {
        this.plugin = plugin;
        this.structures = structures;
    }

    private void fillBarrier(@NotNull WorldInfo worldInfo, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        if (chunkZ != plugin.getChunkZ()) {
            plugin.getLogger().warning("fillBarrier: chunkZ (" + chunkZ + ") != plugin.getChunkZ() (" + plugin.getChunkZ() + ")");
            return;
        }

        // Fill the bottom layer with barriers
        chunkData.setRegion(0, worldInfo.getMinHeight(), 0, 16, worldInfo.getMinHeight() + 1, 16, Material.BARRIER);

        // Fill the sides with barriers
        int minLandZ = plugin.getMinLandZ();
        int maxLandZ = plugin.getMaxLandZ();

        if (minLandZ > 0) {
            chunkData.setRegion(0, worldInfo.getMinHeight(), 0, 16, worldInfo.getMaxHeight(), minLandZ, Material.BARRIER);
        }

        if (maxLandZ < 15) {
            chunkData.setRegion(0, worldInfo.getMinHeight(), maxLandZ + 1, 16, worldInfo.getMaxHeight(), 16, Material.BARRIER);
        }
    }

    @Override
    public void generateNoise(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        if (chunkZ != plugin.getChunkZ()) {
            return;
        }

        super.generateNoise(worldInfo, random, chunkX, chunkZ, chunkData);

        fillBarrier(worldInfo, chunkX, chunkZ, chunkData);
    }

    @Override
    public void generateSurface(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        if (chunkZ != plugin.getChunkZ()) {
            return;
        }

        super.generateSurface(worldInfo, random, chunkX, chunkZ, chunkData);

        fillBarrier(worldInfo, chunkX, chunkZ, chunkData);
    }

    @Override
    public void generateBedrock(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        if (chunkZ != plugin.getChunkZ()) {
            return;
        }

        super.generateBedrock(worldInfo, random, chunkX, chunkZ, chunkData);

        fillBarrier(worldInfo, chunkX, chunkZ, chunkData);
    }

    @Override
    public void generateCaves(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        if (chunkZ != plugin.getChunkZ()) {
            return;
        }

        super.generateCaves(worldInfo, random, chunkX, chunkZ, chunkData);

        fillBarrier(worldInfo, chunkX, chunkZ, chunkData);
    }

    // Override these methods to control what aspects should be generated
    @Override
    public boolean shouldGenerateNoise(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ) {
        return plugin.getChunkZ() == chunkZ;
    }

    @Override
    public boolean shouldGenerateSurface(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ) {
        return plugin.getChunkZ() == chunkZ;
    }

    @Override
    public boolean shouldGenerateCaves(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ) {
        return plugin.getChunkZ() == chunkZ;
    }

    @Override
    public boolean shouldGenerateDecorations(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ) {
        return plugin.getChunkZ() == chunkZ;
    }

    @Override
    public boolean shouldGenerateMobs(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ) {
        return plugin.getChunkZ() == chunkZ;
    }

    @Override
    public boolean shouldGenerateStructures(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ) {
        return plugin.getChunkZ() == chunkZ;
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return getPopulators(world.getEnvironment());
    }

    private List<BlockPopulator> getPopulators(World.Environment environment) {
        ArrayList<BlockPopulator> populators = new ArrayList<>();
//        populators.add(new PopulatorCaves(plugin));
//        populators.add(new PopulatorGravel());
//        populators.add(new PopulatorOres());
//        populators.add(new SaplingPopulator(plugin));
//        populators.add(new GrassPopulator(plugin));
//        populators.add(new PopulatorLonggrass());
//        populators.add(new PopulatorMushrooms());
//        populators.add(new StructurePopulator(plugin, structures));

        return populators;
    }
}
