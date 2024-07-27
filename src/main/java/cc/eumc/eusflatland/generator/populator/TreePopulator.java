package cc.eumc.eusflatland.generator.populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

public class TreePopulator extends BlockPopulator {

	private TreeType treeType;
	private int treesPerChunk;

	public TreePopulator(TreeType treeType, int treesPerChunk) {
		this.treeType = treeType;
		this.treesPerChunk = treesPerChunk;
	}

	@Override
	public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {

		for (int i = 0; i < treesPerChunk; i++) {
			Location location = new Location(
					limitedRegion.getWorld(),
					chunkX + random.nextInt(16),
					limitedRegion.getHighestBlockYAt(chunkX + random.nextInt(16), chunkZ + random.nextInt(16)),
					chunkZ + random.nextInt(16));
			limitedRegion.generateTree(location, random, treeType);
		}
	}

}
