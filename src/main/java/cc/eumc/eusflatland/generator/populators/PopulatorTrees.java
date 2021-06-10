/*
 * Copyright 2012 s1mpl3x
 * 
 * This file is part of Nordic.
 * 
 * Nordic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Nordic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nordic If not, see <http://www.gnu.org/licenses/>.
 */
package cc.eumc.eusflatland.generator.populators;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class PopulatorTrees extends BlockPopulator {

	@Override
	public void populate(final World world, final Random random, final Chunk source) {
		final int treecount = random.nextInt(3);

		for (int t = 0; t <= treecount; t++) {
			final int tree_x = 4 + random.nextInt(8);
			final int tree_z = 4 + random.nextInt(8);

			final Block block = world.getHighestBlockAt(tree_x + source.getX() * 16, tree_z + source.getZ() * 16);
			final Location high = block.getLocation();
			if (!block.getRelative(BlockFace.DOWN).getType().equals(Material.GRASS_BLOCK)) {
				return;
			}
			if (random.nextInt(10) < 1) {
				world.generateTree(high, TreeType.TALL_REDWOOD);

			} else {
				world.generateTree(high, TreeType.REDWOOD);
			}
		}
	}
}
