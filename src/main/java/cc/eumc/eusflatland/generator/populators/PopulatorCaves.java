package cc.eumc.eusflatland.generator.populators;

import cc.eumc.eusflatland.EusFlatLand;
import cc.eumc.eusflatland.util.XYZ;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * BlockPopulator for snake-based caves.
 *
 * @author Pandarr
 *         modified by simplex
 */
public class PopulatorCaves extends BlockPopulator {
	EusFlatLand plugin;
	static Material[] AsAir = new Material[] {Material.AIR, Material.BARRIER};

	public PopulatorCaves(EusFlatLand plugin) {
		this.plugin = plugin;
	}

	/**
	 * @see BlockPopulator#populate(World,
	 * Random, Chunk)
	 */
	@Override
	public void populate(final World world, final Random random, final Chunk source) {

		if (random.nextInt(100) < 3) {
			final int x = 4 + random.nextInt(8) + source.getX() * 16;
			final int z = Math.max(plugin.getMinLandZ(), random.nextInt(plugin.getMaxLandZ() + 1));
			int maxY = world.getHighestBlockYAt(x, z);
			if (maxY < 16) {
				maxY = 32;
			}

			final int y = random.nextInt(maxY);
			final Set<XYZ> snake = selectBlocksForCave(world, random, x, y, z);
			buildCave(world, snake.toArray(new XYZ[snake.size()]));
			for (final XYZ block : snake) {
				world.unloadChunkRequest(block.x / 16, block.z / 16);
			}
		}
	}

	static Set<XYZ> selectBlocksForCave(final World world, final Random random, int blockX, int blockY, int blockZ) {
		final Set<XYZ> snakeBlocks = new HashSet<>();

		int airHits = 0;
		XYZ block = new XYZ();
		while (true) {
			if (airHits > 1200) {
				break;
			}

			if (random.nextInt(20) == 0) {
				blockY++;
			} else if (blockIs(world.getBlockAt(blockX, blockY + 2, blockZ), AsAir)) {
				blockY += 2;
			} else if (blockIs(world.getBlockAt(blockX + 2, blockY, blockZ), AsAir)) {
				blockX++;
			} else if (blockIs(world.getBlockAt(blockX - 2, blockY, blockZ), AsAir)) {
				blockX--;
//			} else if (blockIs(world.getBlockAt(blockX, blockY, blockZ + 2), AsAir)) {
//				blockZ++;
//			} else if (blockIs(world.getBlockAt(blockX, blockY, blockZ - 2), AsAir)) {
//				blockZ--;
			} else if (blockIs(world.getBlockAt(blockX + 1, blockY, blockZ), AsAir)) {
				blockX++;
			} else if (blockIs(world.getBlockAt(blockX - 1, blockY, blockZ), AsAir)) {
				blockX--;
//			} else if (blockIs(world.getBlockAt(blockX, blockY, blockZ + 1), AsAir)) {
//				blockZ++;
//			} else if (blockIs(world.getBlockAt(blockX, blockY, blockZ - 1), AsAir)) {
//				blockZ--;
			} else if (random.nextBoolean()) {
//				if (random.nextBoolean()) {
					blockX++;
//				} else {
//					blockZ++;
//				}
			} else {
//				if (random.nextBoolean()) {
					blockX--;
//				} else {
//					blockZ--;
//				}
			}

			Material type = world.getBlockAt(blockX, blockY, blockZ).getType();
			if (type != Material.AIR && type != Material.BARRIER && !snakeBlocks.contains(new XYZ(blockX, blockY, blockZ))) {
				final int radius = 1 + random.nextInt(2);
				final int radius2 = radius * radius + 1;
				for (int x = -radius; x <= radius; x++) {
					for (int y = -radius; y <= radius; y++) {
						for (int z = -radius; z <= radius; z++) {
							if (x * x + y * y + z * z <= radius2 && y >= 0 && y < 128) {
								if (blockIs(world.getBlockAt(blockX + x, blockY + y, blockZ + z), AsAir)) {
									airHits++;
								} else {
									block.x = blockX + x;
									block.y = blockY + y;
									block.z = blockZ + z;
									if (snakeBlocks.add(block)) {
										block = new XYZ();
									}
								}
							}
						}
					}
				}
			} else {
				airHits++;
			}
		}

		return snakeBlocks;
	}

	static boolean blockIs(Block block, Material[] materials) {
		return Arrays.stream(materials).anyMatch(type -> type == block.getType());
	}

	static void buildCave(final World world, final XYZ[] snakeBlocks) {
		for (final XYZ loc : snakeBlocks) {
			final Block block = world.getBlockAt(loc.x, loc.y, loc.z);
			if (!block.isEmpty() && !block.isLiquid() && block.getType() != Material.BEDROCK) {
				block.setType(Material.AIR, false);
			}
		}
	}
}