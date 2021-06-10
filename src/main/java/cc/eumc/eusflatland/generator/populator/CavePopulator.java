package cc.eumc.eusflatland.generator.populator;

import java.util.Random;

import cc.eumc.eusflatland.EusFlatLand;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

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
	public void populate(World world, Random random, Chunk source) {
		if (source.getZ() != plugin.getChunkZ()) {
			return;
		}
		int worldChunkX = source.getX() * 16;
		int worldChunkZ = source.getZ() * 16;
		int x, y, z;
		SimplexOctaveGenerator noiseGenerator = new SimplexOctaveGenerator(world, 8);
		noiseGenerator.setScale(1 / 32.0);
		for (x = worldChunkX; x < worldChunkX + 16; x++) {
			for (z = worldChunkZ; z < worldChunkZ + plugin.getMaxLandZ() + 16; z++) {
				for (y = world.getMaxHeight() - caveCentre; y > world.getMaxHeight() - (caveCentre + (noiseGenerator.noise(x, z, 0.5, 0.5) * (maxCaveHeight / 2))); y--) {
					if (world.getBlockAt(x, y, z).getType() == Material.STONE || world.getBlockAt(x, y, z).getType() == Material.SANDSTONE || world.getBlockAt(x, y, z).getType() == Material.DIRT) {
						world.getBlockAt(x, y, z).setType(Material.AIR);
					}
				}
				for (y = world.getMaxHeight() - caveCentre; y < world.getMaxHeight() - (caveCentre - (noiseGenerator.noise(x, z, 0.5, 0.5) * (maxCaveHeight / 2))); y++) {
					if (world.getBlockAt(x, y, z).getType() == Material.STONE || world.getBlockAt(x, y, z).getType() == Material.SANDSTONE || world.getBlockAt(x, y, z).getType() == Material.DIRT) {
						world.getBlockAt(x, y, z).setType(Material.AIR);
					}
				}
			}
		}
	}

}
