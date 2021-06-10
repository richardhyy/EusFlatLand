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
import org.bukkit.generator.BlockPopulator;
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
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        if (chunk.getZ() != plugin.getChunkZ()) {
            return;
        }

        for (FlatLandStructure structure : structures) {
            int dice = random.nextInt(1000);
            if (dice < structure.getChance()) {
                int x = random.nextInt(15);
                int z = plugin.getMinLandZ();
                int y = world.getHighestBlockYAt(chunk.getX() * 16 + x, chunk.getZ() * 16 + z);
                Material groundMaterial = chunk.getBlock(x, y, z).getType();
                Biome groundBiome = chunk.getBlock(x, y, z).getBiome();
//                plugin.getLogger().info(String.format("%s, %s, %s : %s [%s]", x, y, z, groundMaterial, groundBiome));

                if (y > 1) {
                    // Check ground material
                    if (structure.getPlaceOnMaterials() != null && structure.getPlaceOnMaterials().length > 0) {
                        if (Arrays.stream(structure.getPlaceOnMaterials()).noneMatch(m -> groundMaterial == m)) {
//                            plugin.getLogger().info("X - Check ground material: " + structure.getPlaceOnMaterials().toString());
                            continue;
                        }
                    }
                    // Check ground biome
                    if (structure.getBiomes() != null && structure.getBiomes().length > 0) {
                        if (Arrays.stream(structure.getBiomes()).noneMatch(b -> groundBiome == b)) {
//                            plugin.getLogger().info("X - Check ground biomes: " + structure.getBiomes().toString());
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
//                                XYZ fillLocation = new XYZ(chunk.getX() * 16 + x, finalY - 1, chunk.getZ() * 16 + z);
                                int xBase = chunk.getX() * 16;
                                int zBase = chunk.getZ() * 16;
                                for (int _x = xBase + x; _x <= xBase + x + structure.getBlueprint().getXWidth() - 1; _x++) {
                                    for (int _z = zBase + z; _z <= zBase + z + structure.getBlueprint().getZWidth() - 1; _z++) {
                                        for (int _y = finalY - 1; _y > 2; _y--) {
                                            Block toFill = world.getBlockAt(_x, _y, _z);
                                            if (toFill.getType() == Material.AIR || toFill.getType() == Material.WATER) {
                                                toFill.setType(structure.getFillBaseWith());
                                            } else {
                                                break;
                                            }
//                                            fillLocation.y--;
                                        }
//                                        fillLocation.z++;
                                    }
//                                    fillLocation.x++;
                                }
                            }
                            Location location = new Location(world, chunk.getX() * 16 + x, finalY, chunk.getZ() * 16 + z);
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
