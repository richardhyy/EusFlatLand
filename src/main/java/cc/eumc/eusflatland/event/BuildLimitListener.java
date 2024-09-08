package cc.eumc.eusflatland.event;

import cc.eumc.eusflatland.EusFlatLand;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BuildLimitListener implements Listener {

    private final EusFlatLand plugin;

    public BuildLimitListener(EusFlatLand plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!isAllowedBuild(event.getPlayer(), event.getBlock().getLocation())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("You can't build outside the designated area.");
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!isAllowedBuild(event.getPlayer(), event.getBlock().getLocation())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("You can't break blocks outside the designated area.");
        }
    }

    private boolean isAllowedBuild(Player player, Location location) {
        if (player.hasPermission("flatland.allowOutsideBuild")) {
            return true;
        }

        double blockZ = location.getZ();
        double blockY = location.getY();

        return blockY <= plugin.getMaxPlayerHeight() && plugin.isOnFlatLand(location);
    }
}
