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
import org.jetbrains.annotations.NotNull;

public class NetherFirePopulator extends BlockPopulator {

	private double getDistanceSquared(double x, double z){
		return (x * x) + (z * z);
	}

	private void ignite(Block block, Random random){
		if (block.getType() == Material.AIR && block.getRelative(BlockFace.DOWN).getType() == Material.NETHERRACK && random.nextInt(100) < 25){
			block.setType(Material.FIRE);
		}
	}

	public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {
//		if (random.nextInt(100) < 25){
//			int x = 5 + random.nextInt(6);
//			int z = 5 + random.nextInt(6);
//
//			int radius = (int) Math.ceil(6D + 0.5);
//			int radiusSq = radius * radius;
//
//			for (int sx = 0; sx < radius; ++sx){
//				for (int sz = 0; sz < radius; ++sz){
//					if (this.getDistanceSquared(sx, sz) < radiusSq){
//						for (int y = 100; y > 30; --y){
//							this.ignite(chunk.getBlock(x + sx, y, z + sz), random);
//							this.ignite(chunk.getBlock(x + sx, y, z - sz), random);
//							this.ignite(chunk.getBlock(x - sx, y, z + sz), random);
//							this.ignite(chunk.getBlock(x - sx, y, z - sz), random);
//						}
//					}
//				}
//			}
//		}
	}

}
