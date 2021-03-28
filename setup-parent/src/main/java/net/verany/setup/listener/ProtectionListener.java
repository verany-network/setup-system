package net.verany.setup.listener;

import net.verany.api.Verany;
import net.verany.api.event.AbstractListener;
import net.verany.api.module.VeranyProject;
import net.verany.setup.player.ISetupPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class ProtectionListener extends AbstractListener {

    public ProtectionListener(VeranyProject project) {
        super(project);

        Verany.registerListener(project, BlockPlaceEvent.class, event -> {
            event.setCancelled(true);
        });

        Verany.registerListener(project, BlockBreakEvent.class, event -> {
            event.setCancelled(true);
        });

        Verany.registerListener(project, EntityDamageEvent.class, event -> {
            event.setCancelled(true);
        });

        Verany.registerListener(project, FoodLevelChangeEvent.class, event -> {
            event.setCancelled(true);
        });

        Verany.registerListener(project, PlayerChangedWorldEvent.class, event -> {
            Player player = event.getPlayer();

            if (event.getFrom().getName().equals("world")) {
                player.setGameMode(GameMode.CREATIVE);
                player.setFlying(true);
            } else {
                if (player.getWorld().getName().equals("world")) {
                    player.setGameMode(GameMode.ADVENTURE);
                }
            }
        });

        Verany.registerListener(project, PlayerMoveEvent.class, event -> {
            if (event.getPlayer().getWorld().getName().equals("world") && event.getPlayer().getLocation().getBlockY() <= 30)
                event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
        });
    }
}
