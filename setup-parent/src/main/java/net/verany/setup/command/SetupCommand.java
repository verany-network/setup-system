package net.verany.setup.command;

import net.verany.api.Verany;
import net.verany.api.command.AbstractCommand;
import net.verany.api.command.CommandEntry;
import net.verany.api.module.VeranyProject;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SetupCommand extends AbstractCommand implements TabCompleter {

    public SetupCommand(VeranyProject project) {
        super(project);

        Verany.registerCommand(project, new CommandEntry("setup", "verany.setup", this), (playerInfo, strings) -> {
            Player player = playerInfo.getPlayer();

            switch (strings.length) {
                case 1: {
                    if (strings[0].equalsIgnoreCase("info")) {
                        player.sendMessage("Current Map: " + player.getWorld().getName());
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
                    break;
                }
            }

            player.sendMessage("command help");
        });
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
