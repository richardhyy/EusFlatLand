package cc.eumc.eusflatland.wall;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomWallBlockManager {
    private final JavaPlugin plugin;
    private final NamespacedKey customWallKey;

    public CustomWallBlockManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.customWallKey = new NamespacedKey(plugin, "custom_wall_block");
    }

    public void setCustomWallBlock(Block block) {
        PersistentDataContainer container = block.getChunk().getPersistentDataContainer();
        String key = getBlockKey(block);
        container.set(customWallKey, PersistentDataType.STRING, key);
    }

    public boolean isCustomWallBlock(Block block) {
        PersistentDataContainer container = block.getChunk().getPersistentDataContainer();
        String key = getBlockKey(block);
        return container.has(customWallKey, PersistentDataType.STRING) &&
                container.get(customWallKey, PersistentDataType.STRING).equals(key);
    }

    public void removeCustomWallBlock(Block block) {
        PersistentDataContainer container = block.getChunk().getPersistentDataContainer();
        container.remove(customWallKey);
    }

    private String getBlockKey(Block block) {
        return block.getX() + "," + block.getY() + "," + block.getZ();
    }
}
