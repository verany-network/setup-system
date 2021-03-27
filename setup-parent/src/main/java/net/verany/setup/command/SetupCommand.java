package net.verany.setup.command;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.verany.api.Verany;
import net.verany.api.command.AbstractCommand;
import net.verany.api.command.CommandEntry;
import net.verany.api.gamemode.AbstractGameMode;
import net.verany.api.gamemode.GameModeWrapper;
import net.verany.api.module.VeranyProject;
import net.verany.api.setup.category.AbstractSetupCategory;
import net.verany.setup.SetupService;
import net.verany.setup.world.WorldData;
import net.verany.setup.world.WorldStatus;
import net.verany.setup.world.manager.IWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SetupCommand extends AbstractCommand implements TabCompleter {

    private final IWorldManager worldManager = SetupService.INSTANCE.getWorldManager();

    public SetupCommand(VeranyProject project) {
        super(project);

        Verany.registerCommand(project, new CommandEntry("setup", "verany.setup", this), (playerInfo, strings) -> {
            Player player = playerInfo.getPlayer();

            switch (strings.length) {
                case 1: {
                    if (strings[0].equalsIgnoreCase("info")) {
                        WorldData worldData = worldManager.getWorldData(player.getWorld().getName());
                        if (worldData == null) {
                            player.sendMessage("§cCould not find any WorldData of your world!");
                            return;
                        }
                        info(player, worldData);
                        return;
                    }
                    if (strings[0].equalsIgnoreCase("reload")) {
                        player.sendMessage("§7Reloading config§8...");
                        worldManager.reloadConfig();
                        player.sendMessage("§7Reloading complete§8!");
                        return;
                    }
                    break;
                }
                case 2: {
                    if (strings[0].equalsIgnoreCase("tp") || strings[0].equalsIgnoreCase("teleport")) {
                        World world = Bukkit.getWorld(strings[1]);
                        if (world == null) {
                            player.sendMessage("§cThis world doesn't exist! (try \"/setup import " + strings[1] + "\")");
                            return;
                        }
                        player.teleport(world.getSpawnLocation());
                        playerInfo.playSound(Sound.ENTITY_ENDERMAN_TELEPORT);
                        return;
                    }
                    if (strings[0].equalsIgnoreCase("import")) {
                        String name = strings[1];
                        WorldData importedWorld = worldManager.getWorldData(name);
                        if (importedWorld != null) {
                            player.sendMessage("§cThis world is already imported!");
                            return;
                        }
                        player.sendMessage("Importing world: " + name);
                        boolean success = worldManager.importWorld(name);
                        if (!success) {
                            player.sendMessage("§cImporting failed!");
                            return;
                        }
                        player.sendMessage("§aImporting success! (/setup tp " + name + ")");
                        return;
                    }
                    if (strings[0].equalsIgnoreCase("info")) {
                        WorldData worldData = worldManager.getWorldData(strings[1]);
                        if (worldData == null) {
                            player.sendMessage("§cThe world \"" + strings[1] + "\" couldn't be found!");
                            return;
                        }
                        info(player, worldData);
                        return;
                    }
                    if (strings[0].equalsIgnoreCase("export")) {
                        WorldData worldData = worldManager.getWorldData(strings[1]);
                        if (worldData == null) {
                            player.sendMessage("§cThe world \"" + strings[1] + "\" couldn't be found!");
                            return;
                        }
                        worldManager.exportWorld(strings[1]);
                        player.sendMessage("§7The world §b" + strings[1] + " §7has been exported§8.");
                        return;
                    }
                    break;
                }
                case 3: {
                    if (strings[0].equalsIgnoreCase("gamemode")) {
                        WorldData worldData = worldManager.getWorldData(strings[1]);
                        if (worldData == null) {
                            player.sendMessage("§cThe world \"" + strings[1] + "\" couldn't be found!");
                            return;
                        }
                        worldData.setGameMode(strings[2]);
                        worldManager.updateWorld(worldData);
                        player.sendMessage("§7Set GameMode of world §b" + strings[1] + " §7to §3" + strings[2] + "§8.");
                        return;
                    }
                    if (strings[0].equalsIgnoreCase("mapname")) {
                        WorldData worldData = worldManager.getWorldData(strings[1]);
                        if (worldData == null) {
                            player.sendMessage("§cThe world \"" + strings[1] + "\" couldn't be found!");
                            return;
                        }
                        worldData.setMapName(strings[2]);
                        worldManager.updateWorld(worldData);
                        player.sendMessage("§7Set the map name of the world §b" + strings[1] + " §7to §3" + strings[2] + "§8.");
                        return;
                    }
                    break;
                }
            }

            player.sendMessage("command help");
        });
    }

    private void info(Player player, WorldData worldData) {
        player.sendMessage("§7World name§8: §b" + worldData.getName());
        player.sendMessage("§7Map Name§8: §b" + worldData.getMapName());
        player.sendMessage("§7Status§8: " + Verany.getNameOfEnum(worldData.getStatus().name(), "§" + worldData.getStatus().getColorCore()));
        player.sendMessage("§7AutoLoad§8: §b" + worldData.isAutoLoad());
        player.sendMessage("§7GameMode§8: §b" + worldData.getGameMode());
        player.sendMessage("§7Builder§8:");
        for (String s : worldData.getBuilder()) {
            player.sendMessage("   " + s);
        }
        if (worldData.getBuilder().isEmpty())
            player.sendMessage("§cNo builder");
        player.sendMessage("§7Locations§8:");
        String[] mapInfo = worldData.getGameMode().split("_");
        if (mapInfo.length > 0) {
            try {
                AbstractSetupCategory category = SetupService.INSTANCE.getSetupObjectMap().get(GameModeWrapper.getGameModeByName(mapInfo[0])).getCategory(worldData.getMapName() + "_" + mapInfo[1]);
                for (AbstractSetupCategory.LocationData location : category.getLocations()) {
                    player.sendMessage("    " + location.getName() + (!location.getLocation().getWorld().equalsIgnoreCase("-") ? " (Set)" : ""));
                }
            } catch (Exception exception) {
                player.sendMessage("§cCould not find locations");
            }
        }
        player.sendMessage(" ");
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        if (!player.hasPermission("verany.command.setup"))
            return ImmutableList.of();

        List<String> arguments = Lists.newArrayList("reload", "info", "import", "export", "tp", "add", "remove", "list", "gamemode", "mapname");

        List<String> importedWorlds = worldManager.getImportedWorlds().stream().map(WorldData::getName).collect(Collectors.toList());
        List<String> unloadedWorlds = worldManager.getUnloadedWorlds();
        List<String> gameModes = GameModeWrapper.VALUES.stream().map(AbstractGameMode::getName).collect(Collectors.toList());
        List<String> unExportedWorlds = worldManager.getWorlds(WorldStatus.IN_WORK).stream().map(WorldData::getName).collect(Collectors.toList());

        if (strings.length == 1)
            return StringUtil.copyPartialMatches(strings[0], arguments, new ArrayList<>(arguments.size()));
        else if (strings.length == 2) {
            if (strings[0].equalsIgnoreCase("tp") || strings[0].equalsIgnoreCase("teleport") || strings[0].equalsIgnoreCase("gamemode") || strings[0].equalsIgnoreCase("mapname"))
                return StringUtil.copyPartialMatches(strings[1], importedWorlds, new ArrayList<>(importedWorlds.size()));
            else if (strings[0].equalsIgnoreCase("import"))
                return StringUtil.copyPartialMatches(strings[1], unloadedWorlds, new ArrayList<>(unloadedWorlds.size()));
            else if (strings[0].equalsIgnoreCase("export"))
                return StringUtil.copyPartialMatches(strings[1], unExportedWorlds, new ArrayList<>(unExportedWorlds.size()));
        } else if (strings.length == 3) {
            if (strings[0].equalsIgnoreCase("gamemode"))
                return StringUtil.copyPartialMatches(strings[2], gameModes, new ArrayList<>(gameModes.size()));
        }

        return ImmutableList.of();
    }
}
