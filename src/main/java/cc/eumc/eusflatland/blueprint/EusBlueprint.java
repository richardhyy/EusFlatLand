package cc.eumc.eusflatland.blueprint;

import cc.eumc.eusflatland.util.XYZ;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.bukkit.block.Block;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class EusBlueprint implements Serializable {
    private XYZ rawOrigin;
    private String[][][] blocks;

    public static EusBlueprint loadBlueprint(File file) throws IOException {
        Gson gson = new Gson();
        return gson.fromJson(new JsonReader(new FileReader(file)), EusBlueprint.class);
    }

    public static void saveBlueprint(Path path, EusBlueprint blueprint) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Files.write(path, gson.toJson(blueprint).trim().getBytes(StandardCharsets.UTF_8));
    }

    public static EusBlueprint toBlueprint(Block[] blocks) {
        if (blocks.length == 0) {
            throw new IllegalArgumentException("Input block list is empty.");
        }

        XYZ origin = new XYZ(blocks[0].getLocation());
        int maxX = origin.x;
        int maxY = origin.y;
        int maxZ = origin.z;
        // Set the origin to the bottom-left most point
        if (blocks.length > 1) {
            for (int i=1; i<blocks.length; i++) {
                XYZ current = new XYZ(blocks[i].getLocation());
                if (current.x < origin.x) {
                    origin.x = current.x;
                }
                if (current.y < origin.y) {
                    origin.y = current.y;
                }
                if (current.z < origin.z) {
                    origin.z = current.z;
                }

                if (current.x > maxX) {
                    maxX = current.x;
                }
                if (current.y > maxY) {
                    maxY = current.y;
                }
                if (current.z > maxZ) {
                    maxZ = current.z;
                }
            }
        }

        maxX -= origin.x;
        maxY -= origin.y;
        maxZ -= origin.z;

        EusBlueprint blueprint = new EusBlueprint();
        blueprint.rawOrigin = origin;
        blueprint.blocks = new String[maxX + 1][maxY + 1][maxZ + 1];
        for (Block block: blocks) {
            XYZ blockLoc = new XYZ(block.getLocation());
            blockLoc.x -= origin.x;
            blockLoc.y -= origin.y;
            blockLoc.z -= origin.z;
            blueprint.blocks[blockLoc.x][blockLoc.y][blockLoc.z] = block.getBlockData().getAsString(true);
        }

        return blueprint;
    }

    public XYZ getRawOrigin() {
        return rawOrigin;
    }

    /**
     * Should not be mutated
     * @return
     */
    public String[][][] getBlocks() {
        return blocks;
    }

    public int getXWidth() {
        return blocks.length;
    }

    public int getYWidth() {
        return blocks[0].length;
    }

    public int getZWidth() {
        return blocks[0][0].length;
    }
}
