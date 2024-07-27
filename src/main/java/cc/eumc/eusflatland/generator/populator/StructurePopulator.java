package cc.eumc.eusflatland.generator.populator;

import cc.eumc.eusflatland.EusFlatLand;
import cc.eumc.eusflatland.FlatLandStructure;
import cc.eumc.eusflatland.blueprint.EusBlueprintOperation;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class StructurePopulator extends BlockPopulator {
    EusFlatLand plugin;
    List<FlatLandStructure> structures;

    public StructurePopulator(EusFlatLand plugin, List<FlatLandStructure> structures) {
        this.plugin = plugin;
        this.structures = structures;
    }

    @Override
    public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {
        if (chunkZ != plugin.getChunkZ()) {
            return;
        }

        for (FlatLandStructure structure : structures) {
            int dice = random.nextInt(1000);
            if (dice < structure.getChance()) {
                int x = random.nextInt(15);
                int z = plugin.getMinLandZ();
                int y = limitedRegion.getHighestBlockYAt(chunkX * 16 + x, chunkZ * 16 + z);
                Material groundMaterial = limitedRegion.getBlockData(x, y, z).getMaterial();
                Biome groundBiome = limitedRegion.getBiome(x, y, z);
//                plugin.getLogger().info(String.format("%s, %s, %s : %s [%s]", x, y, z, groundMaterial, groundBiome));

                if (y > 1) {
                    // Check height
                    if (structure.getMinimumGenerationHeight() != null && structure.getMinimumGenerationHeight() > y) {
                        continue;
                    }
                    // Check ground material
                    if (structure.getPlaceOnMaterials() != null && structure.getPlaceOnMaterials().length > 0) {
                        if (Arrays.stream(structure.getPlaceOnMaterials()).noneMatch(m -> groundMaterial == m)) {
                            continue;
                        }
                    }
                    // Check ground biome
                    if (structure.getBiomes() != null && structure.getBiomes().length > 0) {
                        if (Arrays.stream(structure.getBiomes()).noneMatch(b -> groundBiome == b)) {
                            continue;
                        }
                    }

                    // Place!
                    y += structure.getYOffset() + 1;
                    final int finalY = y;
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            // Fill base
                            if (structure.getFillBaseWith() != null) {
                                int xBase = chunkX * 16;
                                int zBase = chunkZ * 16;
                                for (int _x = xBase + x; _x <= xBase + x + structure.getBlueprint().getXWidth() - 1; _x++) {
                                    for (int _z = zBase + z; _z <= zBase + z + structure.getBlueprint().getZWidth() - 1; _z++) {
                                        for (int _y = finalY - 1; _y > 2; _y--) {
                                            BlockData toFill = limitedRegion.getBlockData(_x, _y, _z);
                                            if (toFill.getMaterial() == Material.AIR || toFill.getMaterial() == Material.WATER) {
                                                limitedRegion.setType(_x, _y, _z, structure.getFillBaseWith());
                                            } else {
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            Location location = new Location(limitedRegion.getWorld(), chunkX * 16 + x, finalY, chunkZ * 16 + z);
                            EusBlueprintOperation.placeBlueprint(structure.getBlueprint(), location);
//                            plugin.getLogger().info("Structure generated at " + new XYZ(location).toString()));
                        }
                    });
                    break;
                }
            }
        }
    }
}
