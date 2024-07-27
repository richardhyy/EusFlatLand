package cc.eumc.eusflatland.generator.populator;

import cc.eumc.eusflatland.EusFlatLand;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class SaplingPopulator extends BlockPopulator {
    EusFlatLand plugin;

    public SaplingPopulator(EusFlatLand plugin) {
        this.plugin = plugin;
    }

    @Override
    public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {
        if (chunkZ == plugin.getChunkZ() && random.nextInt(100) < 30) {
            int amount = random.nextInt(6)+1;
            for (int i = 1; i < amount; i++) {
                int X = random.nextInt(15);
                int Z = (plugin.getMaxLandZ() + plugin.getMinLandZ()) / 2;
                int Y;
                // Set Y at the top of the ground where grass exists
                for (Y = 40; limitedRegion.getBlockData(X, Y, Z).getMaterial() != Material.GRASS_BLOCK && Y < limitedRegion.getWorld().getMaxHeight() - 2; Y++);
                if (limitedRegion.getBlockData(X, Y + 1, Z).getMaterial() == Material.AIR) {
                    Block targetBlock = limitedRegion.getWorld().getBlockAt(chunkX * 16 + X, Y + 1, chunkZ * 16 + Z);
                    Material saplingMaterial = switch (targetBlock.getBiome()) {
                        case JUNGLE, SPARSE_JUNGLE -> Material.JUNGLE_SAPLING;
                        case BAMBOO_JUNGLE -> Material.BAMBOO_SAPLING;
                        default -> Material.OAK_SAPLING;
                    };
                    targetBlock.setType(saplingMaterial);
                }
            }
        }
    }
}
