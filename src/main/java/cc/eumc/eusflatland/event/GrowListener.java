package cc.eumc.eusflatland.event;

import cc.eumc.eusflatland.EusFlatLand;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.world.StructureGrowEvent;

public class GrowListener implements Listener {
    EusFlatLand plugin;

    public GrowListener(EusFlatLand plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void structureGrowEvent(StructureGrowEvent e) {
        e.getBlocks().forEach(blockState -> {
            if (!plugin.isOnFlatLand(blockState.getLocation())) {
                blockState.setBlockData(e.getWorld().getBlockAt(blockState.getLocation()).getBlockData());
            }
        });
    }
}
