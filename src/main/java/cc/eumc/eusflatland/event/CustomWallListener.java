package cc.eumc.eusflatland.event;

import cc.eumc.eusflatland.EusFlatLand;
import cc.eumc.eusflatland.wall.CustomWallBlockManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CustomWallListener implements Listener {

    private final EusFlatLand plugin;
    private final CustomWallBlockManager customWallBlockManager;

    public CustomWallListener(EusFlatLand plugin) {
        this.plugin = plugin;
        this.customWallBlockManager = new CustomWallBlockManager(plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getPlayer().isSneaking()) return;

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        int minLandZ = plugin.getMinLandZ();
        if (clickedBlock.getZ() == minLandZ - 1) {
            Player player = event.getPlayer();
            boolean shouldCancel = handleRightClick(player, clickedBlock);
            event.setCancelled(shouldCancel);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        int minLandZ = plugin.getMinLandZ();
        if (block.getZ() == minLandZ - 1) {
            Player player = event.getPlayer();
            boolean shouldCancel = handleBlockBreak(player, block);
            event.setCancelled(shouldCancel);
        }
    }

    /**
     * Handle right click event
     * @param player Player
     * @param block The block clicked
     * @return Whether the event should be cancelled
     */
    private boolean handleRightClick(Player player, Block block) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (block.getType() != Material.BARRIER) {
            return false;
        } else if (customWallBlockManager.isCustomWallBlock(block)) {
            if (itemInHand.getType() == Material.AIR) {
                // Rotate the block
                return rotateBlock(block);
            }
            return true;
        }

        if (itemInHand.getType().isBlock()
                && itemInHand.getType().isSolid()
                && itemInHand.getType() != Material.BARRIER
                && itemInHand.getType() != Material.AIR) {
            block.setType(itemInHand.getType());
            customWallBlockManager.setCustomWallBlock(block);
            if (player.getGameMode() != GameMode.CREATIVE) {
                itemInHand.setAmount(itemInHand.getAmount() - 1);
            }
            return true;
        }

        return false;
    }

    /**
     * Handle block break event
     * @param player Player
     * @param block The block to be broken
     * @return Whether the event should be cancelled
     */
    private boolean handleBlockBreak(Player player, Block block) {
        if (customWallBlockManager.isCustomWallBlock(block)) {
            Material blockType = block.getType();
            block.setType(Material.BARRIER);
            customWallBlockManager.removeCustomWallBlock(block);
            if (player.getGameMode() != GameMode.CREATIVE) {
                block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(blockType));
            }
            return true;
        } else if (block.getType() == Material.BARRIER
                && player.getGameMode() == GameMode.CREATIVE
                && !player.isSneaking()) {
            // If the player is in creative mode, they can break the barrier if they are sneaking
            return true;  // Cancel the event
        }
        return false;
    }

    private boolean rotateBlock(Block block) {
        if (block.getBlockData() instanceof Rotatable rotatable) {
            BlockFace currentFace = rotatable.getRotation();
            BlockFace[] faces = BlockFace.values();
            int currentIndex = java.util.Arrays.asList(faces).indexOf(currentFace);
            BlockFace newFace = faces[(currentIndex + 1) % faces.length];
            rotatable.setRotation(newFace);
            block.setBlockData(rotatable);
            return true;
        } else if (block.getBlockData() instanceof Directional directional) {
            BlockFace currentFace = directional.getFacing();
            BlockFace[] faces = directional.getFaces().toArray(new BlockFace[0]);
            int currentIndex = java.util.Arrays.asList(faces).indexOf(currentFace);
            BlockFace newFace = faces[(currentIndex + 1) % faces.length];
            directional.setFacing(newFace);
            block.setBlockData(directional);
            return true;
        }
        return false;
    }

}
