package cc.eumc.eusflatland.generator.populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.SimplexOctaveGenerator;
import org.jetbrains.annotations.NotNull;

public class NetherSoulSandPopulator extends BlockPopulator {

	public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {
		SimplexOctaveGenerator soulSandNoise = new SimplexOctaveGenerator(limitedRegion.getWorld(), 8);
		soulSandNoise.setScale(1 / 32.0);

		for (int x = 0; x < 16; ++x){
			for (int z = 0; z < 16; ++z){
				int blockX = x + chunkX * 16;
				int blockZ = z + chunkZ * 16;

				if (soulSandNoise.noise(blockX, blockZ, 0.5, 0.5) > 0.25D){
					for (int y = 128; y > 0; --y){
						Block block = limitedRegion.getWorld().getBlockAt(blockX, y, blockZ);

						if (block.getType() == Material.NETHERRACK && block.getRelative(BlockFace.UP).getType() == Material.AIR){
							block.setType(Material.SOUL_SAND);
						}
					}
				}
			}
		}
	}

}
