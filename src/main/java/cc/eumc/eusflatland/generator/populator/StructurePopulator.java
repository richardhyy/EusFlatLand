package cc.eumc.eusflatland.generator.populator;

import cc.eumc.eusflatland.EusFlatLand;
import cc.eumc.eusflatland.FlatLandStructure;
import cc.eumc.eusflatland.blueprint.EusBlueprintOperation;
import cc.eumc.eusflatland.util.XYZ;
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
                int x = chunkX * 16 + random.nextInt(15);
                int z = chunkZ * 16 + plugin.getMinLandZ();
                int y = limitedRegion.getHighestBlockYAt(x, z);
                Material groundMaterial = limitedRegion.getBlockData(x, y, z).getMaterial();
                Biome groundBiome = limitedRegion.getBiome(x, y, z);
                plugin.getLogger().info(String.format("%s, %s, %s : %s [%s]", x, y, z, groundMaterial, groundBiome));

                if (y > 1) {
                    // Check height
                    if (structure.getMinimumGenerationHeight() != null && structure.getMinimumGenerationHeight() > y) {
                        plugin.getLogger().info("Too low");
                        continue;
                    }
                    // Check ground material
                    if (structure.getPlaceOnMaterials() != null && structure.getPlaceOnMaterials().length > 0) {
                        if (Arrays.stream(structure.getPlaceOnMaterials()).noneMatch(m -> groundMaterial == m)) {
                            plugin.getLogger().info("Ground material not match: " + groundMaterial);
                            continue;
                        }
                    }
                    // Check ground biome
                    if (structure.getBiomes() != null && structure.getBiomes().length > 0) {
                        if (Arrays.stream(structure.getBiomes()).noneMatch(b -> groundBiome == b)) {
                            plugin.getLogger().info("Ground biome not match");
                            continue;
                        }
                    }

                    // Place!
                    y += structure.getYOffset() + 1;
                    final int finalY = y;
//                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//                        @Override
//                        public void run() {
                            // Fill base
                            if (structure.getFillBaseWith() != null) {
                                for (int _x = x; _x <= x + structure.getBlueprint().getXWidth() - 1; _x++) {
                                    for (int _z = z; _z <= z + structure.getBlueprint().getZWidth() - 1; _z++) {
                                        for (int _y = finalY - 1; _y > 2; _y--) {
                                            BlockData toFill = limitedRegion.getWorld().getBlockData(_x, _y, _z);
                                            if (toFill.getMaterial() == Material.AIR || toFill.getMaterial() == Material.WATER) {
                                                limitedRegion.getWorld().setType(_x, _y, _z, structure.getFillBaseWith());
                                            } else {
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            Location location = new Location(limitedRegion.getWorld(), x, finalY, z);
                            EusBlueprintOperation.placeBlueprint(structure.getBlueprint(), location);
                            plugin.getLogger().info("Structure generated at " + new XYZ(location).toString());
//                        }
//                    });

                    structures.sort((a, b) -> random.nextInt(3) - 1);
                    break;
                } else {
                    plugin.getLogger().info("Highest block too low: " + y);
                }
            }
        }
    }
}
