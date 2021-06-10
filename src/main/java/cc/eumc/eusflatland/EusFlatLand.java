package cc.eumc.eusflatland;

import cc.eumc.eusflatland.blueprint.EusBlueprint;
import cc.eumc.eusflatland.command.AdminCommandExecutor;
import cc.eumc.eusflatland.event.GrowListener;
import cc.eumc.eusflatland.event.PlayerListener;
import cc.eumc.eusflatland.generator.FlatLandGenerator;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Location;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

public final class EusFlatLand extends JavaPlugin {
    final int chunkZ = 0;
    final int minLandZ = 1; // No less than 1
    final int maxLandZ = 3; // No more than 14
    final int maxPlayerHeight = 245;

    private String blueprintFolder;

    public String getBlueprintFolder() {
        return blueprintFolder;
    }

    @Override
    public void onEnable() {
        this.blueprintFolder = getDataFolder() + "/blueprints";

        File blueprintFolderFile = new File(blueprintFolder);
        if (!blueprintFolderFile.exists()) {
            blueprintFolderFile.mkdirs();
        }

        getServer().getPluginManager().registerEvents(new GrowListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getCommand("flatland").setExecutor(new AdminCommandExecutor(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public @NotNull ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        List<FlatLandStructure> structures = new ArrayList<>();
        try {
            structures.addAll(loadStructures());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new FlatLandGenerator(this, structures);
    }

    private @NotNull List<FlatLandStructure> loadStructures() throws Exception {
        File structureMeta = new File(getDataFolder() + "/structureMeta.json");
        if (!structureMeta.exists()) {
            try {
                extractResource("default_structures.json", structureMeta.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        JsonElement jelement = new JsonParser().parse(new FileReader(structureMeta));
        JsonObject jobject = jelement.getAsJsonObject();

        Set<Map.Entry<String, JsonElement>> structureElements = jobject.entrySet();
        List<FlatLandStructure> structures = new ArrayList<>();
        for (Map.Entry<String, JsonElement> structureKV : structureElements) {
            File blueprintFile = new File(blueprintFolder + "/" + structureKV.getKey());
            if (!blueprintFile.exists()) {
                // TODO: NOT EXIST MSG
                extractResource(structureKV.getKey(), blueprintFile.toPath());
                continue;
            }
            EusBlueprint blueprint = EusBlueprint.loadBlueprint(blueprintFile);
            JsonObject value = structureKV.getValue().getAsJsonObject();
            FlatLandStructure structure = new Gson().fromJson(value.toString(), FlatLandStructure.class);
            structure.blueprint = blueprint;
            structures.add(structure);
            getLogger().info("Loaded: " + structureKV.getKey() + ": " + structureKV.getValue());
        }

        return structures;
    }

    private void extractResource(String filename, Path path) throws IOException {
        InputStream in = getResource(filename);
        Files.copy(Objects.requireNonNull(in), path, StandardCopyOption.REPLACE_EXISTING);
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public int getMinLandZ() {
        return minLandZ;
    }

    public int getMaxLandZ() {
        return maxLandZ;
    }

    public int getMaxPlayerHeight() {
        return maxPlayerHeight;
    }

    public boolean isOnFlatLand(Location location) {
        return isOnFlatLand(0, 0, location.getBlockZ());
    }

    public boolean isOnFlatLand(int x, int y, int z) {
        return (z >= minLandZ && z <= maxLandZ);
    }
}
