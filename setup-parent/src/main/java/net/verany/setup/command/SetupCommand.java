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
import net.verany.setup.SetupType;
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

import java.io.File;
import java.util.*;
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
                            player.sendMessage(playerInfo.getPrefix(SetupService.INSTANCE.getModule()) + "§cCould not find any WorldData of your world!");
                            return;
                        }
                        info(player, worldData);
                        return;
                    }
                    if (strings[0].equalsIgnoreCase("reload")) {
                        player.sendMessage(playerInfo.getPrefix(SetupService.INSTANCE.getModule()) + "§7Reloading config§8...");
                        worldManager.reloadConfig();
                        player.sendMessage(playerInfo.getPrefix(SetupService.INSTANCE.getModule()) + "§7Reloading complete§8!");
                        return;
                    }
                    break;
                }
                case 2: {
                    if (strings[0].equalsIgnoreCase("tp") || strings[0].equalsIgnoreCase("teleport")) {
                        World world = Bukkit.getWorld(strings[1]);
                        if (world == null) {
                            player.sendMessage(playerInfo.getPrefix(SetupService.INSTANCE.getModule()) + "§cThis world doesn't exist! (try \"/setup import " + strings[1] + "\")");
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
                            player.sendMessage(playerInfo.getPrefix(SetupService.INSTANCE.getModule()) + "§cThis world is already imported!");
                            return;
                        }
                        player.sendMessage(playerInfo.getPrefix(SetupService.INSTANCE.getModule()) + "§7Importing world: " + name);
                        boolean success = worldManager.importWorld(name);
                        if (!success) {
                            player.sendMessage(playerInfo.getPrefix(SetupService.INSTANCE.getModule()) + "§cImporting failed!");
                            return;
                        }
                        player.sendMessage(playerInfo.getPrefix(SetupService.INSTANCE.getModule()) + "§aImporting success! (/setup tp " + name + ")");
                        return;
                    }
                    if (strings[0].equalsIgnoreCase("info")) {
                        WorldData worldData = worldManager.getWorldData(strings[1]);
                        if (worldData == null) {
                            player.sendMessage(playerInfo.getPrefix(SetupService.INSTANCE.getModule()) + "§cThe world \"" + strings[1] + "\" couldn't be found!");
                            return;
                        }
                        info(player, worldData);
                        return;
                    }
                    if (strings[0].equalsIgnoreCase("export")) {
                        WorldData worldData = worldManager.getWorldData(strings[1]);
                        if (worldData == null) {
                            player.sendMessage(playerInfo.getPrefix(SetupService.INSTANCE.getModule()) + "§cThe world \"" + strings[1] + "\" couldn't be found!");
                            return;
                        }
                        worldManager.exportWorld(strings[1]);
                        player.sendMessage(playerInfo.getPrefix(SetupService.INSTANCE.getModule()) + "§7The world §b" + strings[1] + " §7has been exported§8.");
                        return;
                    }
                    if (strings[0].equalsIgnoreCase("list")) {
                        SetupType type = SetupType.fromName(strings[1].toUpperCase());
                        if (type == null) {

                            return;
                        }
                        switch (type) {
                            case WORLD: {
                                player.sendMessage(playerInfo.getPrefix(SetupService.INSTANCE.getModule()) + "§7Worlds§8:");
                                for (WorldData importedWorld : worldManager.getImportedWorlds()) {
                                    player.sendMessage("    " + importedWorld.getName());
                                }
                                break;
                            }
                            /*case TYPES: {
                                player.sendMessage(playerInfo.getPrefix(SetupService.INSTANCE.getModule()) + "§7Types§8:");
                                for (SetupType value : SetupType.values()) {
                                    player.sendMessage("    " + Verany.getNameOfEnum(value.name(), ""));
                                }
                                break;
                            }*/
                        }
                        return;
                    }
                    break;
                }
                case 3: {
                    /*if (strings[0].equalsIgnoreCase("gamemode")) {
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
                    }*/
                    break;
                }
            }

            player.sendMessage(playerInfo.getPrefix(SetupService.INSTANCE.getModule()) + "Command Help");
            player.sendMessage("  §8• §b/setup help §8▬§7▬ §7Shows this command help§8.");
            player.sendMessage("  §8• §b/setup reload §8▬§7▬ §7Reload the config & games§8.");
            player.sendMessage("  §8• §b/setup info (<map>) §8▬§7▬ §7Show info of map§8.");
            player.sendMessage("  §8• §b/setup import <world> §8▬§7▬ §7Import a worlds from the worlds folder§8.");
            player.sendMessage("  §8• §b/setup export <world> §8▬§7▬ §7Finish a map§8.");
            player.sendMessage("  §8• §b/setup tp <world> §8▬§7▬ §7Teleport to a map§8.");
            player.sendMessage("  §8• §b/setup set <type> <name> §8▬§7▬ §7Set a type§8.");
            player.sendMessage("  §8• §b/setup remove <name> §8▬§7▬ §7Remove a location§8.");
            player.sendMessage("  §8• §b/setup list (<type>) §8▬§7▬ §7List all types§8.");
            player.sendMessage(" ");
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
            player.sendMessage("    " + s);
        }
        if (worldData.getBuilder().isEmpty())
            player.sendMessage("§cNo builder");
        player.sendMessage("§7Locations§8:");
        String[] mapInfo = worldData.getGameMode().split("_");
        if (mapInfo.length > 0) {
            try {
                AbstractSetupCategory category = SetupService.INSTANCE.getSetupObjectMap().get(GameModeWrapper.getGameModeByName(mapInfo[0])).getCategory(worldData.getMapName() + "_" + mapInfo[1]);
                for (AbstractSetupCategory.LocationData location : category.getLocations()) {
                    player.sendMessage("   " + location.getName() + (!location.getLocation().getWorld().equalsIgnoreCase("-") ? " (Set)" : ""));
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

        List<String> arguments = Lists.newArrayList("reload", "info", "import", "export", "tp", "set", "remove", "list");

        List<String> importedWorlds = worldManager.getImportedWorlds().stream().map(WorldData::getName).collect(Collectors.toList());
        List<String> gameModes = GameModeWrapper.VALUES.stream().map(AbstractGameMode::getName).collect(Collectors.toList());
        List<String> unExportedWorlds = worldManager.getWorlds(WorldStatus.IN_WORK).stream().map(WorldData::getName).collect(Collectors.toList());
        List<String> setupTypes = Arrays.stream(SetupType.values()).map(setupType -> setupType.name().toLowerCase()).collect(Collectors.toList());

        if (strings.length == 1)
            return StringUtil.copyPartialMatches(strings[0], arguments, new ArrayList<>(arguments.size()));
        else if (strings.length == 2) {
            if (strings[0].equalsIgnoreCase("tp") || strings[0].equalsIgnoreCase("info") || strings[0].equalsIgnoreCase("teleport") || strings[0].equalsIgnoreCase("gamemode") || strings[0].equalsIgnoreCase("mapname"))
                return StringUtil.copyPartialMatches(strings[1], importedWorlds, new ArrayList<>(importedWorlds.size()));
            else if (strings[0].equalsIgnoreCase("import")) {
                List<String> unloadedWorlds = worldManager.getUnloadedWorlds("worlds");
                String[] folders = strings[1].split("/");
                if (folders.length > 1) {
                    StringBuilder fileBuilder = new StringBuilder();
                    for (int i = 0; i < folders.length - 1; i++) {
                        fileBuilder.append(folders[i]).append("/");
                    }
                    File file = new File("worlds/" + fileBuilder);
                    File[] files = file.listFiles();
                    if (files != null) {
                        unloadedWorlds = Arrays.stream(files).filter(File::isDirectory).map(world -> fileBuilder + world.getName()).collect(Collectors.toList());
                        return StringUtil.copyPartialMatches(strings[1], unloadedWorlds, new ArrayList<>(unloadedWorlds.size()));
                    }
                }
                return StringUtil.copyPartialMatches(strings[1], unloadedWorlds, new ArrayList<>(unloadedWorlds.size()));
            } else if (strings[0].equalsIgnoreCase("export"))
                return StringUtil.copyPartialMatches(strings[1], unExportedWorlds, new ArrayList<>(unExportedWorlds.size()));
            else if (strings[0].equalsIgnoreCase("list"))
                return StringUtil.copyPartialMatches(strings[1], setupTypes, new ArrayList<>(setupTypes.size()));
        } else if (strings.length == 3) {
            if (strings[0].equalsIgnoreCase("gamemode"))
                return StringUtil.copyPartialMatches(strings[2], gameModes, new ArrayList<>(gameModes.size()));
        }

        return ImmutableList.of();
    }
}
