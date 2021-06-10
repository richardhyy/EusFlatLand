package cc.eumc.eusflatland.blueprint;

import cc.eumc.eusflatland.util.KeyValue;
import cc.eumc.eusflatland.util.XYZ;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.HashMap;

public class EusBlueprintOperation {
    public static EusBlueprint rangeToBlueprint(Location locationA, Location locationB) {
        if (locationA.getWorld() != locationB.getWorld()) {
            throw new IllegalArgumentException("Point A and B located in different worlds.");
        }

        XYZ pointA = new XYZ(Math.min(locationA.getBlockX(), locationB.getBlockX()),
                Math.min(locationA.getBlockY(), locationB.getBlockY()),
                Math.min(locationA.getBlockZ(), locationB.getBlockZ()));
        XYZ pointB = new XYZ(Math.max(locationA.getBlockX(), locationB.getBlockX()),
                Math.max(locationA.getBlockY(), locationB.getBlockY()),
                Math.max(locationA.getBlockZ(), locationB.getBlockZ()));
        World world = locationA.getWorld();
        Block[] blocks = new Block[(pointB.x-pointA.x+1) * (pointB.y-pointA.y+1) * (pointB.z-pointA.z+1)];
        int i = 0;
        for (int x = pointA.x; x <= pointB.x; x++) {
            for (int y = pointA.y; y <= pointB.y; y++) {
                for (int z = pointA.z; z <= pointB.z; z++) {
                    blocks[i] = world.getBlockAt(x, y, z);
                    i++;
                }
            }
        }
        return EusBlueprint.toBlueprint(blocks);
    }

    public static void placeBlueprint(EusBlueprint blueprint, Location origin) {
        String[][][] blocks = blueprint.getBlocks();
        for (int x=0; x<blocks.length; x++) {
            for (int y = 0; y < blocks[0].length; y++) {
                for (int z = 0; z < blocks[0][0].length; z++) {
                    Location location = new Location(origin.getWorld(), origin.getBlockX() + x, origin.getBlockY() + y, origin.getBlockZ() + z);
                    Block block = location.getBlock();
                    block.setBlockData(Bukkit.createBlockData(blocks[x][y][z]), true);
                }
            }
        }
//        blockMap.forEach(((xyz, materialBlockDataKeyValue) -> {
//            Location location = new Location(origin.getWorld(), origin.getBlockX() + xyz.x, origin.getBlockY() + xyz.y, origin.getBlockZ() + xyz.z);
//            Block block = location.getBlock();
//            block.setType(materialBlockDataKeyValue.getKey());
//            block.setBlockData(Bukkit.createBlockData(materialBlockDataKeyValue.getValue()), true);
//        }));
    }
}
