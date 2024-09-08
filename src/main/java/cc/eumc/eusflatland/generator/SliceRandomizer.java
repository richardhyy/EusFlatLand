package cc.eumc.eusflatland.generator;

import cc.eumc.eusflatland.EusFlatLand;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SliceRandomizer {
    private final EusFlatLand plugin;
    private final Random random;
    private final int minSlices;
    private final int maxSlices;
    private final int minSliceDistance;
    private final int maxSliceDistance;
    private final int tunnelGenerationRangeX;
    private final int minTunnelCount;
    private final int maxTunnelCount;

    private List<SliceInfo> slices;
    private final Map<Integer, Integer> chunkZToSliceIndex = new HashMap<>();
    private final Map<Integer, List<Integer>> chunkXToSliceIndexWhichHasTunnelAtThisX = new HashMap<>();

    public SliceRandomizer(EusFlatLand plugin, long seed,
                           int minSlices, int maxSlices,
                           int minSliceDistance, int maxSliceDistance,
                           int tunnelGenerationRangeX,
                           int minTunnelCount, int maxTunnelCount) {
        this.plugin = plugin;
        this.random = new Random(seed);
        this.minSlices = Math.max(2, minSlices); // Minimum 2 slices
        this.maxSlices = Math.max(this.minSlices, maxSlices);
        this.minSliceDistance = minSliceDistance;
        this.maxSliceDistance = maxSliceDistance;
        this.tunnelGenerationRangeX = tunnelGenerationRangeX;
        this.minTunnelCount = Math.max(0, minTunnelCount);
        this.maxTunnelCount = Math.max(minTunnelCount, maxTunnelCount);
        generateSlicesAndTunnels();
    }

    private void generateSlicesAndTunnels() {
        slices = new ArrayList<>();
        int sliceCount = random.nextInt(maxSlices - minSlices + 1) + minSlices;
        int currentZ = 0;

        for (int i = 0; i < sliceCount; i++) {
            int tunnelCount = random.nextInt(maxTunnelCount - minTunnelCount + 1) + minTunnelCount;
            int[] tunnelXs = new int[tunnelCount];
            for (int j = 0; j < tunnelCount; j++) {
                tunnelXs[j] = random.nextInt(tunnelGenerationRangeX * 2 + 1) - tunnelGenerationRangeX;
            }
            slices.add(new SliceInfo(currentZ * (random.nextBoolean() ? 1 : -1), tunnelXs)); // Randomly generate slices on both sides

            if (i < sliceCount - 1) {
                // Generate next slice Z
                currentZ = Math.abs(currentZ) + random.nextInt(maxSliceDistance - minSliceDistance + 1) + minSliceDistance;
            }
        }

        // Sort slices by sliceZ
        slices.sort(Comparator.comparingInt(SliceInfo::getSliceZ));

        // Generate chunkZ to slice index map
        for (int i = 0; i < slices.size(); i++) {
            chunkZToSliceIndex.put(slices.get(i).getSliceZ(), i);
        }

        // Generate chunkX to slice index which has tunnel at this X map
        for (int i = 0; i < slices.size(); i++) {
            SliceInfo sliceInfo = slices.get(i);
            for (int tunnelX : sliceInfo.getTunnelXs()) {
                if (!chunkXToSliceIndexWhichHasTunnelAtThisX.containsKey(tunnelX)) {
                    chunkXToSliceIndexWhichHasTunnelAtThisX.put(tunnelX, new ArrayList<>());
                }
                chunkXToSliceIndexWhichHasTunnelAtThisX.get(tunnelX).add(i);
            }
        }

        plugin.getLogger().info("Generated slices: " + slices);
    }

    /**
     * Get the SliceInfo of the slice at the given chunkZ
     * @param chunkZ Chunk Z
     * @return SliceInfo of the slice at the given chunkZ, or null if the chunkZ is not a slice
     */
    public @Nullable SliceInfo getSliceInfo(int chunkZ) {
        if (!isLandSlice(chunkZ)) {
            return null;
        }
        return slices.get(chunkZToSliceIndex.get(chunkZ));
    }

    /**
     * Check if the given chunk is a land slice
     * @param chunkZ Chunk Z
     * @return True if the given chunk is a land slice, false otherwise
     */
    public boolean isLandSlice(int chunkZ) {
        return chunkZToSliceIndex.containsKey(chunkZ);
    }

    /**
     * Check if the given chunk is a tunnel
     * @param chunkX Chunk X
     * @param chunkZ Chunk Z
     * @return True if the given chunk is a tunnel, false otherwise
     */
    public boolean isTunnel(int chunkX, int chunkZ) {
        if (!chunkXToSliceIndexWhichHasTunnelAtThisX.containsKey(chunkX)) {
            return false;
        }

        for (int i = 0; i < slices.size() - 1; i++) {
            SliceInfo currentSlice = slices.get(i);
            SliceInfo nextSlice = slices.get(i + 1);
            if ((currentSlice.canBeTunnel(chunkX) || nextSlice.canBeTunnel(chunkX)) && chunkZ > currentSlice.getSliceZ() && chunkZ < nextSlice.getSliceZ()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the directions of the adjacent tunnels of the tunnel at the given chunkX
     * @param chunkZ Chunk Z (should be at slice)
     * @param chunkX Chunk X (should be at slice)
     * @return Array of BlockFace of the adjacent tunnels, empty array if the given chunk is not connected to any tunnel
     */
    public BlockFace[] getTunnelAdjacentDirections(int chunkZ, int chunkX) {
        List<BlockFace> directions = new ArrayList<>();
        // Check if there exists a tunnel at the given chunkX
        if (chunkXToSliceIndexWhichHasTunnelAtThisX.containsKey(chunkX)) {
            int sliceIndex = chunkZToSliceIndex.get(chunkZ);
            SliceInfo sliceInfo = slices.get(sliceIndex);
            if (sliceInfo == null) {
                plugin.getLogger().warning("getTunnelAdjacentDirections: chunkZ (" + chunkZ + ") is not a slice");
                return directions.toArray(new BlockFace[0]);
            }

            if (sliceInfo.canBeTunnel(chunkX)) {
                if (sliceIndex > 0) {
                    // Add the direction of the tunnel connecting to the previous slice
                    directions.add(BlockFace.NORTH);
                }
                if (sliceIndex < slices.size() - 1) {
                    // Add the direction of the tunnel connecting to the next slice
                    directions.add(BlockFace.SOUTH);
                }
            } else {
                if (sliceIndex > 0 && slices.get(sliceIndex - 1).canBeTunnel(chunkX)) {
                    // Add the direction of the tunnel connecting to the previous slice
                    directions.add(BlockFace.NORTH);
                }
                if (sliceIndex < slices.size() - 1 && slices.get(sliceIndex + 1).canBeTunnel(chunkX)) {
                    // Add the direction of the tunnel connecting to the next slice
                    directions.add(BlockFace.SOUTH);
                }
            }
        }
        return directions.toArray(new BlockFace[0]);
    }
}
