package cc.eumc.eusflatland.generator.populator;

import cc.eumc.eusflatland.EusFlatLand;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class GrassPopulator extends BlockPopulator {
    EusFlatLand plugin;

    public GrassPopulator(EusFlatLand plugin) {
        this.plugin = plugin;
    }

    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        if (chunk.getZ() == plugin.getChunkZ() && random.nextInt(10) < 7) {
            int amount = random.nextInt(16)+1;
            for (int i = 1; i < amount; i++) {
                int X = random.nextInt(15);
                int Z = random.nextInt(plugin.getMaxLandZ()) + plugin.getMinLandZ();
                int Y;
                for (Y = 40; chunk.getBlock(X, Y, Z).getType() != Material.GRASS_BLOCK && Y < world.getMaxHeight() - 2; Y++);
                if (chunk.getBlock(X, Y + 1, Z).getType() == Material.AIR) {
                        switch (chunk.getBlock(X, Y, Z).getBiome()) {
                        case FOREST:
                        case BIRCH_FOREST:
                        case BIRCH_FOREST_HILLS:
                        case CRIMSON_FOREST:
                        case DARK_FOREST:
                        case TALL_BIRCH_FOREST:
                        case DARK_FOREST_HILLS:
                        case WARPED_FOREST:
                            if (random.nextInt(10) < 7) {
                                // Grass land decorations for forests
                                if (random.nextBoolean()) {
                                    world.getBlockAt(chunk.getX() * 16 + X, Y, chunk.getZ() * 16 + Z).setType(Material.PODZOL);
                                } else {
                                    world.getBlockAt(chunk.getX() * 16 + X, Y, chunk.getZ() * 16 + Z).setType(Material.COBBLESTONE);
                                }
                                break;
                            }
                        default:
                            world.getBlockAt(chunk.getX() * 16 + X, Y + 1, chunk.getZ() * 16 + Z).setType(Material.GRASS);
                        }
                }
            }
        }
    }
}
