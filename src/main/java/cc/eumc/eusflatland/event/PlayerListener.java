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
        if (!player.hasPermission("flatland.allowOutsideMove")) {
            Location from = e.getFrom();
            Location to = e.getTo();

            boolean needsTeleport = false;
            double newY = to.getY();
            double newZ = to.getZ();

            // Check Y coordinate
            if (Math.floor(newY) > plugin.getMaxPlayerHeight()) {
                newY = plugin.getMaxPlayerHeight();
                needsTeleport = true;
            }

            // Check Z coordinate
            int chunkZ = plugin.getChunkZ();
            int minZ = chunkZ * 16;
            int maxZ = (chunkZ + 1) * 16 - 1;

            if (newZ < minZ) {
                newZ = minZ;
                needsTeleport = true;
            } else if (newZ > maxZ) {
                newZ = maxZ;
                needsTeleport = true;
            }

            // Teleport if needed
            if (needsTeleport) {
                Location safeLocation = new Location(to.getWorld(), to.getX(), newY, newZ, to.getYaw(), to.getPitch());
                player.teleport(safeLocation);
                e.setCancelled(true);
            }
        }
    }
}
