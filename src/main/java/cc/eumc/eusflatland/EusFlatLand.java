package cc.eumc.eusflatland;

import cc.eumc.eusflatland.command.AdminCommandExecutor;
import cc.eumc.eusflatland.event.GrowListener;
import cc.eumc.eusflatland.event.PlayerListener;
import cc.eumc.eusflatland.generator.FlatLandGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

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
        return new FlatLandGenerator(this);
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
