package cc.eumc.eusflatland.generator.populator;

import cc.eumc.eusflatland.EusFlatLand;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class SaplingPopulator extends BlockPopulator {
    EusFlatLand plugin;

    public SaplingPopulator(EusFlatLand plugin) {
        this.plugin = plugin;
    }

    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        if (chunk.getZ() == plugin.getChunkZ() && random.nextInt(100) < 30) {
            int amount = random.nextInt(6)+1;
            for (int i = 1; i < amount; i++) {
                int X = random.nextInt(15);
                int Z = (plugin.getMaxLandZ() + plugin.getMinLandZ()) / 2;
                int Y;
                // Set Y at the top of the ground where grass exists
                for (Y = 40; chunk.getBlock(X, Y, Z).getType() != Material.GRASS_BLOCK && Y < world.getMaxHeight() - 2; Y++);
                if (chunk.getBlock(X, Y + 1, Z).getType() == Material.AIR) {
                    Block targetBlock = world.getBlockAt(chunk.getX() * 16 + X, Y + 1, chunk.getZ() * 16 + Z);
                    Material saplingMaterial;
                    switch (targetBlock.getBiome()) {
                        case JUNGLE:
                        case JUNGLE_EDGE:
                        case JUNGLE_HILLS:
                            saplingMaterial = Material.JUNGLE_SAPLING;
                            break;
                        case BAMBOO_JUNGLE:
                        case BAMBOO_JUNGLE_HILLS:
                            saplingMaterial = Material.BAMBOO_SAPLING;
                            break;
                        default:
                            saplingMaterial = Material.OAK_SAPLING;
                    }
                    targetBlock.setType(saplingMaterial);
                }
            }
        }
    }
}
