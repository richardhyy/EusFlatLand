package cc.eumc.eusflatland.generator;

import java.util.Arrays;

public class SliceInfo {
    private final int sliceZ;
    private final int[] tunnelXs;

    public SliceInfo(int sliceZ, int[] tunnelXs) {
        this.sliceZ = sliceZ;
        this.tunnelXs = tunnelXs;
    }

    public int getSliceZ() { return sliceZ; }
    public int[] getTunnelXs() { return tunnelXs; }

    public boolean canBeTunnel(int chunkX) {
        for (int tunnelX : tunnelXs) {
            if (chunkX == tunnelX) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "SliceInfo{" +
                "sliceZ=" + sliceZ +
                ", tunnelXs=" + Arrays.toString(tunnelXs) +
                '}';
    }
}

