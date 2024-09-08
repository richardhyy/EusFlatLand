package cc.eumc.eusflatland.generator;

import cc.eumc.eusflatland.EusFlatLand;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlatLandGenerator extends ChunkGenerator {
    private final EusFlatLand plugin;
    private final SliceRandomizer sliceRandomizer;

    public FlatLandGenerator(EusFlatLand plugin, SliceRandomizer sliceRandomizer) {
        this.plugin = plugin;
        this.sliceRandomizer = sliceRandomizer;
    }

    private boolean isLandChunk(int chunkX, int chunkZ) {
        return sliceRandomizer.isLandSlice(chunkZ) || sliceRandomizer.isTunnel(chunkX, chunkZ);
    }

    private void fillBarrier(@NotNull WorldInfo worldInfo, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        if (!isLandChunk(chunkX, chunkZ)) {
            return;
        }

        int minHeight = worldInfo.getMinHeight();
        int maxHeight = worldInfo.getMaxHeight();

        // Fill the bottom layer with barriers
        chunkData.setRegion(0, minHeight, 0, 16, minHeight + 1, 16, Material.BARRIER);

        int minLandZ = plugin.getMinLandZ();
        int maxLandZ = plugin.getMaxLandZ();

        if (sliceRandomizer.isLandSlice(chunkZ)) {
            SliceInfo sliceInfo = sliceRandomizer.getSliceInfo(chunkZ);
            if (sliceInfo == null) {
                plugin.getLogger().warning("fillBarrier: SliceInfo is null for chunkZ " + chunkZ);
                return;
            }

            // Fill the sides with barriers, accounting for tunnels
            BlockFace[] tunnelDirections = sliceRandomizer.getTunnelAdjacentDirections(chunkZ, chunkX);

            if (containsDirection(tunnelDirections, BlockFace.NORTH)) {
                // North side is a tunnel
                chunkData.setRegion(0, minHeight, 0,
                                    minLandZ/*=X*/, maxHeight, minLandZ, Material.BARRIER);
                chunkData.setRegion(maxLandZ/*=X*/ + 1, minHeight, 0,
                                    16, maxHeight, minLandZ, Material.BARRIER);
            } else {
                // North side is not a tunnel, fill the whole side with barriers
                chunkData.setRegion(0, minHeight, 0, 16, maxHeight, minLandZ, Material.BARRIER);
            }

            if (containsDirection(tunnelDirections, BlockFace.SOUTH)) {
                // South side is a tunnel
                chunkData.setRegion(0, minHeight, maxLandZ + 1,
                                    minLandZ/*=X*/, maxHeight, 16, Material.BARRIER);
                chunkData.setRegion(maxLandZ/*=X*/ + 1, minHeight, maxLandZ + 1,
                                16, maxHeight, 16, Material.BARRIER);
            } else {
                // South side is not a tunnel, fill the whole side with barriers
                chunkData.setRegion(0, minHeight, maxLandZ + 1, 16, maxHeight, 16, Material.BARRIER);
            }
        } else if (sliceRandomizer.isTunnel(chunkX, chunkZ)) {
            // For tunnels, we need to fill barriers on the West and East sides
            chunkData.setRegion(0, minHeight, 0,
                                minLandZ/*=X*/, maxHeight, 16, Material.BARRIER);
            chunkData.setRegion(maxLandZ/*=X*/ + 1, minHeight, 0,
                            16, maxHeight, 16, Material.BARRIER);
        }
    }

    private boolean containsDirection(BlockFace[] directions, BlockFace direction) {
        for (BlockFace face : directions) {
            if (face == direction) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void generateNoise(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        if (!isLandChunk(chunkX, chunkZ)) {
            return;
        }

        super.generateNoise(worldInfo, random, chunkX, chunkZ, chunkData);
        fillBarrier(worldInfo, chunkX, chunkZ, chunkData);
    }

    @Override
    public void generateSurface(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        if (!isLandChunk(chunkX, chunkZ)) {
            return;
        }

        super.generateSurface(worldInfo, random, chunkX, chunkZ, chunkData);
        fillBarrier(worldInfo, chunkX, chunkZ, chunkData);
    }

    @Override
    public void generateBedrock(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        if (!isLandChunk(chunkX, chunkZ)) {
            return;
        }

        super.generateBedrock(worldInfo, random, chunkX, chunkZ, chunkData);
        fillBarrier(worldInfo, chunkX, chunkZ, chunkData);
    }

    @Override
    public void generateCaves(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        if (!isLandChunk(chunkX, chunkZ)) {
            return;
        }

        super.generateCaves(worldInfo, random, chunkX, chunkZ, chunkData);
        fillBarrier(worldInfo, chunkX, chunkZ, chunkData);
    }

    // Override these methods to control what aspects should be generated
    @Override
    public boolean shouldGenerateNoise(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ) {
        return isLandChunk(chunkX, chunkZ);
    }

    @Override
    public boolean shouldGenerateSurface(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ) {
        return isLandChunk(chunkX, chunkZ);
    }

    @Override
    public boolean shouldGenerateCaves(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ) {
        return isLandChunk(chunkX, chunkZ);
    }

    @Override
    public boolean shouldGenerateDecorations(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ) {
        return isLandChunk(chunkX, chunkZ);
    }

    @Override
    public boolean shouldGenerateMobs(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ) {
        return isLandChunk(chunkX, chunkZ);
    }

    @Override
    public boolean shouldGenerateStructures(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ) {
        return isLandChunk(chunkX, chunkZ);
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return new ArrayList<>(); // No populators for now
    }
}
