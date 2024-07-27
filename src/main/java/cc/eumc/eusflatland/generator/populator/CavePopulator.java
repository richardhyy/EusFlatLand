package cc.eumc.eusflatland.generator.populator;

import java.util.Random;

import cc.eumc.eusflatland.EusFlatLand;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.SimplexOctaveGenerator;
import org.jetbrains.annotations.NotNull;

public class CavePopulator extends BlockPopulator {
	EusFlatLand plugin;

	private int maxCaveHeight;
	private int caveCentre;

	public CavePopulator(EusFlatLand plugin, int maxCaveHeight, int caveDistanceFromMaxHeight) {
		this.maxCaveHeight = maxCaveHeight;
		this.caveCentre = caveDistanceFromMaxHeight;
		this.plugin = plugin;
	}

	@Override
	public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {
		if (chunkZ != plugin.getChunkZ()) {
			return;
		}
		int worldChunkX = chunkX * 16;
		int worldChunkZ = chunkZ * 16;
		int x, y, z;
		SimplexOctaveGenerator noiseGenerator = new SimplexOctaveGenerator(limitedRegion.getWorld(), 8);
		noiseGenerator.setScale(1 / 32.0);
		for (x = worldChunkX; x < worldChunkX + 16; x++) {
			for (z = worldChunkZ; z < worldChunkZ + plugin.getMaxLandZ() + 16; z++) {
				for (y = limitedRegion.getWorld().getMaxHeight() - caveCentre; y > limitedRegion.getWorld().getMaxHeight() - (caveCentre + (noiseGenerator.noise(x, z, 0.5, 0.5) * ((double) maxCaveHeight / 2))); y--) {
					if (limitedRegion.getBlockData(x, y, z).getMaterial() == Material.STONE || limitedRegion.getBlockData(x, y, z).getMaterial() == Material.SANDSTONE || limitedRegion.getBlockData(x, y, z).getMaterial() == Material.DIRT) {
						limitedRegion.setType(x, y, z, Material.AIR);
					}
				}
				for (y = limitedRegion.getWorld().getMaxHeight() - caveCentre; y < limitedRegion.getWorld().getMaxHeight() - (caveCentre - (noiseGenerator.noise(x, z, 0.5, 0.5) * ((double) maxCaveHeight / 2))); y++) {
					if (limitedRegion.getBlockData(x, y, z).getMaterial() == Material.STONE || limitedRegion.getBlockData(x, y, z).getMaterial() == Material.SANDSTONE || limitedRegion.getBlockData(x, y, z).getMaterial() == Material.DIRT) {
						limitedRegion.setType(x, y, z, Material.AIR);
					}
				}
			}
		}
	}

}
