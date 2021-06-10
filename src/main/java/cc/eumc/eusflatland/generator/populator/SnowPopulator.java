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

public class SnowPopulator extends BlockPopulator {
	EusFlatLand plugin;

	public SnowPopulator(EusFlatLand plugin) {
		this.plugin = plugin;
	}

	public void populate(World world, Random random, Chunk chunk){
		int chunkX = chunk.getX() * 16;
		int chunkZ = chunk.getZ() * 16;
		
		for (int x = 0; x < 16; ++x){
			for (int z = plugin.getMinLandZ(); z < plugin.getMaxLandZ() + 1; ++z){
				Biome biome = world.getBiome(chunkX + x, chunkZ + z);
				
				if (biome == Biome.ICE_SPIKES || biome == Biome.SNOWY_MOUNTAINS || biome == Biome.TAIGA || biome == Biome.TAIGA_HILLS || biome == Biome.FROZEN_OCEAN || biome == Biome.FROZEN_RIVER){
					int y = world.getHighestBlockYAt(chunkX + x, chunkZ + z);
					
					if (y > 5){
						Block block = chunk.getBlock(x, y, z);
						
						if (block.getRelative(BlockFace.DOWN).getType() != Material.AIR){
							block.setType(Material.SNOW);
						}
					}
				}
			}
		}
	}
	
}
