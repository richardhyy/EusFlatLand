///*
// * Copyright 2012 s1mpl3x
// *
// * This file is part of Nordic.
// *
// * Nordic is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * Nordic is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with Nordic If not, see <http://www.gnu.org/licenses/>.
// */
//package cc.eumc.eusflatland.generator.populators;
//
//import eu.over9000.nordic.util.XYZ;
//import org.bukkit.Chunk;
//import org.bukkit.Material;
//import org.bukkit.World;
//import org.bukkit.block.Block;
//import org.bukkit.block.BlockFace;
//import org.bukkit.generator.BlockPopulator;
//
//import java.util.*;
//
///**
// * BlockPopulator for CustomTrees.
// *
// * @author simplex
// *         based on Pandarr's CaveGen @see Populator_Caves
// */
//public class PopulatorCustomTrees extends BlockPopulator {
//
//	/**
//	 * @see BlockPopulator#populate(World,
//	 * Random, Chunk)
//	 */
//	@Override
//	public void populate(final World world, final Random random, final Chunk source) {
//
//		if (random.nextInt(100) < 2) {
//			final int x = 5 + random.nextInt(6) + source.getX() * 16;
//			final int z = 5 + random.nextInt(6) + source.getZ() * 16;
//			final Block high = world.getHighestBlockAt(x, z);
//			if (!high.getRelative(BlockFace.DOWN).getType().equals(Material.GRASS_BLOCK)) {
//				return;
//			}
//			final int maxY = high.getY();
//			if (maxY < 55) {
//				return;
//			}
//			final Set<XYZ> snake = selectBlocksForTree(world, random, x, maxY - 5, z);
//			buildTree(world, snake.toArray(new XYZ[snake.size()]));
////			for (final XYZ block : snake) {
////				world.unloadChunkRequest(block.x / 16, block.z / 16);
////			}
//		}
//	}
//
//	private static Set<XYZ> selectBlocksForTree(final World world, final Random r, final int blockX, int blockY, final int blockZ) {
//		final Set<XYZ> snakeBlocks = new HashSet<>();
//		final int height = blockY + 20 + r.nextInt(5);
//		XYZ block = new XYZ();
//		while (true) {
//			if (blockY > height) {
//				break;
//			}
//
//			blockY++;
//
//			int radius = 1;
//
//			if (blockY + 3 > height) {
//				radius = 0;
//			}
//			final int radius2 = radius * radius + 1;
//			for (int x = -radius; x <= radius; x++) {
//				for (int z = -radius; z <= radius; z++) {
//					if (x * x + z * z < radius2) {
//						block.x = blockX + x;
//						block.y = blockY;
//						block.z = blockZ + z;
//						if (snakeBlocks.add(block)) {
//							block = new XYZ();
//						}
//					}
//				}
//			}
//
//		}
//		return snakeBlocks;
//	}
//
//	private static void buildTree(final World world, final XYZ[] snakeBlocks) {
//		// cut the snake into slices, this is my lab report
//		final HashMap<Integer, ArrayList<Block>> slices = new HashMap<>();
//		//System.out.println(snakeBlocks.length);
//		for (final XYZ loc : snakeBlocks) {
//			final Block block = world.getBlockAt(loc.x, loc.y, loc.z);
//			if (block.isEmpty() && !block.isLiquid() && block.getType() != Material.BEDROCK) {
//				if (slices.containsKey(loc.y)) {
//					slices.get(loc.y).add(block);
//				} else {
//					slices.put(loc.y, new ArrayList<>());
//					slices.get(loc.y).add(block);
//				}
//			}
//		}
//
//		final ArrayList<Integer> sortedKeys = new ArrayList<>(slices.keySet());
//		Collections.sort(sortedKeys);
//		final int low = sortedKeys.get(0);
//		final int high = sortedKeys.get(sortedKeys.size() - 1);
//		//boolean buildLayer1 = false;
//		boolean buildLayer2 = false;
//		boolean buildLayer3 = false;
//		boolean buildLayer4 = false;
//		for (final Integer key : sortedKeys) {
//			final ArrayList<Block> slice = slices.get(key);
//			for (final Block b : slice) {
//				//b.setTypeIdAndData(17, (byte) 1, false);
//                                b.setType(Material.SPRUCE_LOG, false);
//			}
////			if (!buildLayer1) {
////				ArrayList<Block> toBranches = new ArrayList<Block>();
////				for (Block b : slice) {
////					if (b.getY()-low >= (high-low)-12 && checkBlockIsOnBorderOfSlice(b, slice)) {
////						toBranches.add(b);
////						buildLayer1 = true;
////					}
////				}
////				buildTreeLayer1(toBranches);
////			}
//			if (!buildLayer2) {
//				final ArrayList<Block> toBranches = new ArrayList<>();
//				for (final Block b : slice) {
//					if (b.getY() - low >= (high - low) - 8 && checkBlockIsOnBorderOfSlice(b, slice)) {
//						toBranches.add(b);
//						buildLayer2 = true;
//					}
//				}
//				buildTreeLayer2(toBranches);
//			}
//			if (!buildLayer3) {
//				final ArrayList<Block> toBranches = new ArrayList<>();
//				for (final Block b : slice) {
//					if (b.getY() - low >= (high - low) - 4 && checkBlockIsOnBorderOfSlice(b, slice)) {
//						toBranches.add(b);
//						buildLayer3 = true;
//					}
//				}
//				buildTreeLayer3(toBranches);
//			}
//			if (!buildLayer4) {
//				final ArrayList<Block> toBranches = new ArrayList<>();
//				for (final Block b : slice) {
//					if (b.getY() - low >= (high - low) && checkBlockIsOnBorderOfSlice(b, slice)) {
//						toBranches.add(b);
//						buildLayer4 = true;
//					}
//				}
//				buildTreeLayer4(toBranches);
//			}
//		}
//	}
//
////	private static void buildTreeLayer1(ArrayList<Block> blocks){
////		ArrayList<Block> branches = new ArrayList<Block>();
////
////		for (Block b : blocks) {
////			BlockFace dir = getBuildDirection(b);
////			Block handle = b.getRelative(dir);
////			handle.setTypeIdAndData(17, (byte) 1, false);
////			branches.add(handle);
////			switch (dir) {
////			case NORTH:
////				branches.add(handle.getRelative(-1, 0, 1));
////				branches.add(handle.getRelative(-1, 0, -1));
////				branches.add(handle.getRelative(-2, 0, 2));
////				branches.add(handle.getRelative(-2, 0, -2));
////				break;
////			case EAST:
////				branches.add(handle.getRelative(-1, 0, -1));
////				branches.add(handle.getRelative(1, 0, -1));
////				branches.add(handle.getRelative(-2, 0, -2));
////				branches.add(handle.getRelative(2, 0, -2));
////				break;
////			case SOUTH:
////				branches.add(handle.getRelative(1, 0, 1));
////				branches.add(handle.getRelative(1, 0, -1));
////				branches.add(handle.getRelative(2, 0, 2));
////				branches.add(handle.getRelative(2, 0, -2));
////				break;
////			case WEST:
////				branches.add(handle.getRelative(-1, 0, 1));
////				branches.add(handle.getRelative(1, 0, 1));
////				branches.add(handle.getRelative(-2, 0, 2));
////				branches.add(handle.getRelative(2, 0, 2));
////				break;
////			}
////		}
////		if (!branches.isEmpty()) {
////			for (Block branch : branches) {
////				branch.setTypeIdAndData(17, (byte) 1, false);
////				populateTreeBranch(branch, 2);
////			}
////		}
////	}
//
//	private static void buildTreeLayer2(final ArrayList<Block> blocks) {
//		final ArrayList<Block> branches = new ArrayList<>();
//
//		for (final Block b : blocks) {
//			final BlockFace dir = getBuildDirection(b);
//			final Block handle = b.getRelative(dir);
//			//handle.setTypeIdAndData(17, (byte) 1, false);
//                        handle.setType(Material.SPRUCE_LOG, false);
//			branches.add(handle);
//			switch (dir) {
//				case NORTH:
//					branches.add(handle.getRelative(-1, 0, 1));
//					branches.add(handle.getRelative(-1, 0, -1));
//					break;
//				case EAST:
//					branches.add(handle.getRelative(-1, 0, -1));
//					branches.add(handle.getRelative(1, 0, -1));
//					break;
//				case SOUTH:
//					branches.add(handle.getRelative(1, 0, 1));
//					branches.add(handle.getRelative(1, 0, -1));
//					break;
//				case WEST:
//					branches.add(handle.getRelative(-1, 0, 1));
//					branches.add(handle.getRelative(1, 0, 1));
//					break;
//			}
//		}
//		if (!branches.isEmpty()) {
//			for (final Block branch : branches) {
//				//branch.setTypeIdAndData(17, (byte) 1, false);
//                                branch.setType(Material.SPRUCE_LOG, false);
//				populateTreeBranch(branch, 2);
//			}
//		}
//	}
//
//	private static void buildTreeLayer3(final ArrayList<Block> blocks) {
//		final ArrayList<Block> branches = new ArrayList<>();
//
//		for (final Block b : blocks) {
//			final BlockFace dir = getBuildDirection(b);
//			final Block handle = b.getRelative(dir);
//			//handle.setTypeIdAndData(17, (byte) 1, false);
//                        handle.setType(Material.SPRUCE_LOG, false);
//			branches.add(handle);
//		}
//		if (!branches.isEmpty()) {
//			for (final Block branch : branches) {
//				//branch.setTypeIdAndData(17, (byte) 1, false);
//                                branch.setType(Material.SPRUCE_LOG, false);
//				populateTreeBranch(branch, 2);
//			}
//		}
//	}
//
//	private static void buildTreeLayer4(final ArrayList<Block> blocks) {
//		final ArrayList<Block> branches = new ArrayList<>();
//		for (final Block block : blocks) {
//			branches.add(block);
//		}
//		if (!branches.isEmpty()) {
//			for (final Block branch : branches) {
//				//branch.setTypeIdAndData(17, (byte) 1, false);
//                                branch.setType(Material.SPRUCE_LOG, false);
//				populateTreeBranch(branch, 2);
//			}
//		}
//	}
//
//	private static BlockFace getBuildDirection(final Block b) {
//		final BlockFace[] faces = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
//		for (final BlockFace blockFace : faces) {
//			if (!b.getRelative(blockFace).isEmpty()) {
//				return blockFace.getOppositeFace();
//			}
//		}
//		return BlockFace.SELF;
//	}
//
//	private static boolean checkBlockIsOnBorderOfSlice(final Block block, final ArrayList<Block> slice) {
//		final BlockFace[] faces = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
//		if (slice.contains(block.getRelative(faces[0]))
//				&& slice.contains(block.getRelative(faces[1]))
//				&& slice.contains(block.getRelative(faces[2]))
//				&& slice.contains(block.getRelative(faces[3]))) {
//			return false;
//		}
//		return true;
//	}
//
//	private static void populateTreeBranch(final Block block, final int radius) {
//		final int centerX = block.getX();
//		final int centerZ = block.getZ();
//		final int centerY = block.getY();
//		final World w = block.getWorld();
//
//		final int radius_check = radius * radius + 1;
//
//		for (int x = -radius; x <= radius; x++) {
//			for (int z = -radius; z <= radius; z++) {
//				for (int y = -radius; y <= radius; y++) {
//					if (x * x + y * y + z * z <= radius_check) {
//						final Block b = w.getBlockAt(centerX + x, centerY + y, centerZ + z);
//						if (b.isEmpty()) {
//							//b.setTypeIdAndData(18, (byte) 1, false);
//                                                        b.setType(Material.SPRUCE_LEAVES, false);
//						}
//					}
//				}
//			}
//		}
//	}
//}