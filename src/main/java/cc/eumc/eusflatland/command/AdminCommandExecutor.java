package cc.eumc.eusflatland.command;

import cc.eumc.eusflatland.EusFlatLand;
import cc.eumc.eusflatland.blueprint.EusBlueprint;
import cc.eumc.eusflatland.blueprint.EusBlueprintOperation;
import cc.eumc.eusflatland.util.XYZ;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class AdminCommandExecutor implements CommandExecutor, TabExecutor {
    EusFlatLand plugin;
    private final String adminPermissionNode = "flatland.admin";
    private final String[] commands = {"select", "save", "place"};
    private final String[] selectPointSubCommands = {"A", "B"};

    private HashMap<Player, XYZ[]> selectionMap = new HashMap<>();

    public AdminCommandExecutor(EusFlatLand plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (sender.hasPermission(adminPermissionNode)) {
            if (!(sender instanceof Player)) {
                sendMessage(sender, "You must be a player.");
                return true;
            }

            if (args.length == 2) {
                Player player = (Player) sender;

                switch (args[0].toLowerCase()) {
                    case "place":
                        try {
                            EusBlueprintOperation.placeBlueprint(EusBlueprint.loadBlueprint(new File(plugin.getBlueprintFolder() + String.format("/%s.blueprint", args[1].replace(".blueprint", "")))), player.getLocation());
                            sendMessage(sender, "§aSuccess!");
                        } catch (Exception e) {
                            e.printStackTrace();
                            sendMessage(sender, String.format("Failed placing blueprint: %s", e.getMessage()));
                        }
                        break;

                    case "save":
                        XYZ[] points = selectionMap.getOrDefault(player, new XYZ[2]);
                        if (points[0] == null || points[1] == null) {
                            sendMessage(sender, "§eSelect two points first.");
                        } else {
                            EusBlueprint blueprint = EusBlueprintOperation.rangeToBlueprint(
                                    new Location(player.getWorld(), points[0].x, points[0].y, points[0].z),
                                    new Location(player.getWorld(), points[1].x, points[1].y, points[1].z)
                            );
                            try {
                                EusBlueprint.saveBlueprint(Path.of(plugin.getBlueprintFolder() + "/" + args[1] + ".blueprint"), blueprint);
                                sendMessage(sender, String.format("Blueprint saved as %s.blueprint", args[1]));
                            } catch (IOException e) {
                                e.printStackTrace();
                                sendMessage(sender, String.format("Error saving blueprint: %s", e.getMessage()));
                            }
                        }
                        break;

                    case "select":
                        XYZ[] locations;
                        if (selectionMap.containsKey(player)) {
                            locations = selectionMap.get(player);
                        } else {
                            locations = new XYZ[2];
                        }

                        Block block = player.getTargetBlockExact(10);
                        if (block == null) {
                            sendMessage(sender, "§eNo block targeted.");
                            break;
                        }
                        XYZ simpleCoordination = new XYZ(block.getLocation());
                        switch (args[1].toUpperCase()) {
                            case "A":
                                locations[0] = simpleCoordination;
                                break;
                            case "B":
                                locations[1] = simpleCoordination;
                                break;
                            default:
                                sendMessage(sender, String.format("Selection: A(%s), B(%s)", locations[0], locations[1]));
                        }
                        selectionMap.put(player, locations);
                        sendMessage(sender, String.format("Point %s selected: %s", args[1], simpleCoordination));
                        break;
                    default:
                        sendMessage(sender, "Unknown command.");
                }
            }
        } else {
            sendMessage(sender, "Sorry.");
        }
        return true;
    }

    private void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(message);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission(adminPermissionNode)) return new ArrayList<>();

        if (args.length > 2)
            return new ArrayList<>();
        else if (args.length == 2)
            if (args[0].equalsIgnoreCase("select")) {
                return Arrays.stream(selectPointSubCommands).filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
            }
            else if (args[0].equalsIgnoreCase("place")) {
                return Arrays.stream(Objects.requireNonNull(new File(plugin.getBlueprintFolder()).list())).filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
            } else {
                return new ArrayList<>();
            }
        if (args.length == 1)
            return Arrays.stream(commands).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
        else
            return Arrays.asList(commands);
    }
}
