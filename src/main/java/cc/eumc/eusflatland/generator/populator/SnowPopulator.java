package cc.eumc.eusflatland.generator.populator;

import java.util.Random;

import cc.eumc.eusflatland.EusFlatLand;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

public class SnowPopulator extends BlockPopulator {
    EusFlatLand plugin;

    public SnowPopulator(EusFlatLand plugin) {
        this.plugin = plugin;
    }

    public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {
        for (int x = 0; x < 16; ++x) {
            for (int z = plugin.getMinLandZ(); z < plugin.getMaxLandZ() + 1; ++z) {
                int y = limitedRegion.getHighestBlockYAt(chunkX + x, chunkZ + z);
                Biome biome = limitedRegion.getBiome(chunkX + x, y, chunkZ + z);

                if (biome == Biome.ICE_SPIKES || biome == Biome.SNOWY_BEACH || biome == Biome.SNOWY_PLAINS
                        || biome == Biome.TAIGA || biome == Biome.SNOWY_TAIGA || biome == Biome.FROZEN_OCEAN
                        || biome == Biome.FROZEN_RIVER) {
                    if (y > 5) {
                        Block block = limitedRegion.getWorld().getBlockAt(x, y, z);

                        if (block.getRelative(BlockFace.DOWN).getType() != Material.AIR) {
                            block.setType(Material.SNOW);
                        }
                    }
                }
            }
        }
    }

}
