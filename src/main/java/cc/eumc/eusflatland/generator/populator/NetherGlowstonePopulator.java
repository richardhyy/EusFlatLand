package cc.eumc.eusflatland.generator.populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

public class NetherGlowstonePopulator extends BlockPopulator {

    private double getLengthSquared(double x, double y, double z) {
        return (x * x) + (y * y) + (z * z);
    }

    public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {
        if (random.nextInt(100) < 12) {
            Integer x = 5 + random.nextInt(6);
            Integer y = limitedRegion.getWorld().getMaxHeight() - random.nextInt(120) - 28;
            Integer z = 5 + random.nextInt(6);

            boolean lava = (random.nextInt(100) < 40);

            int radius = (int) Math.ceil(5D + 0.5);
            int radiusSq = radius * radius;

            for (int sx = 0; sx < radius; ++sx) {
                for (int sy = 0; sy < radius; ++sy) {
                    for (int sz = 0; sz < radius; ++sz) {
                        double length = this.getLengthSquared(sx, sy, sz);

                        if ((lava && length < radiusSq && length > radiusSq - 16) || (!lava && length < radiusSq)) {
                            limitedRegion.setType(x + sx, y + sy, z + sz, Material.GLOWSTONE);
                            limitedRegion.setType(x - sx, y + sy, z + sz, Material.GLOWSTONE);
                            limitedRegion.setType(x + sx, y - sy, z + sz, Material.GLOWSTONE);
                            limitedRegion.setType(x + sx, y + sy, z - sz, Material.GLOWSTONE);
                            limitedRegion.setType(x - sx, y - sy, z + sz, Material.GLOWSTONE);
                            limitedRegion.setType(x + sx, y - sy, z - sz, Material.GLOWSTONE);
                            limitedRegion.setType(x - sx, y + sy, z - sz, Material.GLOWSTONE);
                            limitedRegion.setType(x - sx, y - sy, z - sz, Material.GLOWSTONE);
                        } else if (length < radiusSq - 16) {
                            limitedRegion.setType(x + sx, y + sy, z + sz, Material.LAVA);
                            limitedRegion.setType(x - sx, y + sy, z + sz, Material.LAVA);
                            limitedRegion.setType(x + sx, y - sy, z + sz, Material.LAVA);
                            limitedRegion.setType(x + sx, y + sy, z - sz, Material.LAVA);
                            limitedRegion.setType(x - sx, y - sy, z + sz, Material.LAVA);
                            limitedRegion.setType(x + sx, y - sy, z - sz, Material.LAVA);
                            limitedRegion.setType(x - sx, y + sy, z - sz, Material.LAVA);
                            limitedRegion.setType(x - sx, y - sy, z - sz, Material.LAVA);
                        }
                    }
                }

                if (lava) {
                    for (int ly = 0; ly < radius; ++ly) {
                        limitedRegion.setType(x + ly, y, z, Material.LAVA);
                        limitedRegion.setType(x - ly, y, z, Material.LAVA);
                        limitedRegion.setType(x, y, z + ly, Material.LAVA);
                        limitedRegion.setType(x, y, z - ly, Material.LAVA);
                    }
                }
            }
        }
    }

}
