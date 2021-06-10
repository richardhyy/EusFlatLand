package cc.eumc.eusflatland.generator;

import cc.eumc.eusflatland.EusFlatLand;
import cc.eumc.eusflatland.FlatLandStructure;
import cc.eumc.eusflatland.generator.populator.GrassPopulator;
import cc.eumc.eusflatland.generator.populator.SaplingPopulator;
import cc.eumc.eusflatland.generator.populator.StructurePopulator;
import cc.eumc.eusflatland.generator.populators.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class FlatLandGenerator extends ChunkGenerator {
    EusFlatLand plugin;
    List<FlatLandStructure> structures;
//    int currentHeight = 64;

    public FlatLandGenerator(EusFlatLand plugin, List<FlatLandStructure> structures) {
        this.plugin = plugin;
        this.structures = structures;
    }

    @Override
    public @NotNull ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biome) {
        ChunkData chunk = createChunkData(world);

        if (chunkZ == plugin.getChunkZ()) {
//            SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
//            generator.setScale(0.005D);
            ChunkData vanillaChunkData = createVanillaChunkData(world, chunkX, chunkZ);
            for (int x = 0; x < 16; x++) {
                for (int z = plugin.getMinLandZ() - 1; z < plugin.getMaxLandZ() + 2; z++) {
                    for (int y = world.getMinHeight(); y < world.getMaxHeight(); y++) {
                        if (z == plugin.getMinLandZ() - 1 || z == plugin.getMaxLandZ() + 1) { // Barrier
                            chunk.setBlock(x, y, z, Material.BARRIER);
                        } else if (y == world.getMinHeight()) {// || y == world.getMaxHeight() - 1) { // Floor & Ceil barriers
                            chunk.setBlock(x, y, z, Material.BARRIER);
                        } else {
                            chunk.setBlock(x, y, z, vanillaChunkData.getBlockData(x, y, z));
//                            int height1 = (int) (generator.noise(chunkX*16+x, chunkZ*16+z, 0.5D, 0.5D)*15D+50D);
//                            int height2 = (int) (generator.noise(chunkX*16+x, chunkZ*16+z, 0.8D, 0.7D)*15D);
//                            currentHeight = Math.min(height1 + height2, world.getMaxHeight() - 2);
//                            chunk.setBlock(x, currentHeight, z, Material.GRASS_BLOCK);
//                            chunk.setBlock(x, currentHeight-1, z, Material.DIRT);
//                            for (int i = currentHeight-2; i > 0; i--)
//                                chunk.setBlock(x, i, z, Material.STONE);
//                            chunk.setBlock(x, 0, z, Material.BEDROCK);
                        }
                    }
                }
            }
        }
        return chunk;
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return getPopulators(world.getEnvironment());
    }

    private List<BlockPopulator> getPopulators(World.Environment environment) {
        ArrayList<BlockPopulator> populators = new ArrayList<>();
        populators.add(new PopulatorCaves(plugin));
        populators.add(new PopulatorGravel());
        populators.add(new PopulatorOres());
        populators.add(new SaplingPopulator(plugin));
        populators.add(new GrassPopulator(plugin));
        populators.add(new PopulatorLonggrass());
        populators.add(new PopulatorMushrooms());
        populators.add(new StructurePopulator(plugin, structures));
//        switch (environment) {
//            case NORMAL:
////                populators.add(new BiomeTreePopulator());
//                populators.add(new CavePopulator(plugin,16, 200));
//                populators.add(new CavePopulator(plugin,4, 180));
//                populators.add(new CavePopulator(plugin,12, 160));
//                populators.add(new CavePopulator(plugin,7, 140));
//                populators.add(new OrePopulator(plugin, Material.COAL_ORE, Material.STONE, 8, 8));
//                populators.add(new OrePopulator(plugin, Material.IRON_ORE, Material.STONE, 6, 6));
//                populators.add(new OrePopulator(plugin, Material.LAPIS_ORE, Material.STONE, 2, 4));
//                populators.add(new OrePopulator(plugin, Material.GOLD_ORE, Material.STONE, 4, 4));
//                populators.add(new OrePopulator(plugin, Material.DIAMOND_ORE, Material.STONE, 3, 4));
//                populators.add(new OrePopulator(plugin, Material.REDSTONE_ORE, Material.STONE, 4, 16));
//                populators.add(new OrePopulator(plugin, Material.EMERALD_ORE, Material.STONE, 1, 3));
//                populators.add(new SnowPopulator(plugin));
//                break;
//            case THE_END:
//                populators.add(new EndTowerPopulator());
//                break;
//            case NETHER:
//                populators.add(new CavePopulator(plugin,5, 32));
//                populators.add(new OrePopulator(plugin, Material.QUARTZ, Material.NETHERRACK, 16, 8));
//                populators.add(new NetherSoulSandPopulator());
//                populators.add(new NetherFirePopulator());
//                populators.add(new NetherGlowstonePopulator());
//                break;
//        }

        return populators;
    }
}
