package cc.eumc.eusflatland.event;

import cc.eumc.eusflatland.EusFlatLand;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerListener implements Listener {
    EusFlatLand plugin;

    public PlayerListener(EusFlatLand plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (!player.hasPermission("flatland.allowOutside")) {
            Location location = player.getLocation();
            if (Math.floor(location.getY()) > plugin.getMaxPlayerHeight()) {
                location.setY(plugin.getMaxPlayerHeight());
                player.teleport(location);
            }
            if (location.getZ() < plugin.getMinLandZ()) {
                location.setZ(plugin.getMinLandZ());
                player.teleport(location);
            } else if (location.getZ() > plugin.getMaxLandZ() + 1) {
                location.setZ(plugin.getMaxLandZ());
                player.teleport(location);
            }
        }
    }
}
