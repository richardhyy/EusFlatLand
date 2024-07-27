package cc.eumc.eusflatland.generator.populator;

import cc.eumc.eusflatland.EusFlatLand;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class GrassPopulator extends BlockPopulator {
    EusFlatLand plugin;

    public GrassPopulator(EusFlatLand plugin) {
        this.plugin = plugin;
    }

    @Override
    public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {
        if (chunkZ == plugin.getChunkZ() && random.nextInt(10) < 7) {
            int amount = random.nextInt(16)+1;
            for (int i = 1; i < amount; i++) {
                int X = random.nextInt(15);
                int Z = random.nextInt(plugin.getMaxLandZ()) + plugin.getMinLandZ();
                int Y;
                for (Y = 40; limitedRegion.getBlockData(X, Y, Z).getMaterial() != Material.GRASS_BLOCK && Y < limitedRegion.getWorld().getMaxHeight() - 2; Y++);
                if (limitedRegion.getBlockData(X, Y + 1, Z).getMaterial() == Material.AIR) {
                        switch (limitedRegion.getBiome(X, Y, Z)) {
                        case FOREST:
                        case BIRCH_FOREST:
                        case CRIMSON_FOREST:
                        case DARK_FOREST:
                        case WARPED_FOREST:
                            if (random.nextInt(10) < 7) {
                                // Grass land decorations for forests
                                if (random.nextBoolean()) {
                                    limitedRegion.setType(chunkX * 16 + X, Y, chunkZ * 16 + Z, Material.PODZOL);
                                } else {
                                    limitedRegion.setType(chunkX * 16 + X, Y, chunkZ * 16 + Z, Material.COBBLESTONE);
                                }
                                break;
                            }
                        default:
                            limitedRegion.setType(chunkX * 16 + X, Y + 1, chunkZ * 16 + Z, Material.SHORT_GRASS);
                        }
                }
            }
        }
    }
}
