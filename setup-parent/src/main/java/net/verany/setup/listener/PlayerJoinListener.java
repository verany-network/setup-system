package net.verany.setup.listener;

import net.verany.api.Verany;
import net.verany.api.event.AbstractListener;
import net.verany.api.event.events.PlayerLoadCompleteEvent;
import net.verany.api.gamemode.AbstractGameMode;
import net.verany.api.gamemode.GameModeWrapper;
import net.verany.api.hotbar.HotbarItem;
import net.verany.api.itembuilder.ItemBuilder;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.IPlayerInfo;
import net.verany.setup.SetupService;
import net.verany.setup.inventory.SetupInventory;
import net.verany.setup.player.ISetupPlayer;
import net.verany.setup.player.SetupPlayer;
import net.verany.setup.scoreboard.GameScoreboard;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerJoinListener extends AbstractListener {

    public PlayerJoinListener(VeranyProject project) {
        super(project);

        Verany.registerListener(project, PlayerLoadCompleteEvent.class, event -> {
            Player player = event.getPlayer();
            IPlayerInfo playerInfo = Verany.getPlayer(player);

            ISetupPlayer setupPlayer = new SetupPlayer(project);
            setupPlayer.load(player.getUniqueId());
            Verany.setPlayer(ISetupPlayer.class, setupPlayer);

            for (IPlayerInfo onlinePlayer : Verany.getOnlinePlayers()) {
                onlinePlayer.sendMessage(onlinePlayer.getPrefix(SetupService.INSTANCE.getModule()) + playerInfo.getNameWithColor() + " §7has joined the server§8.");
            }

            setupPlayer.setItems();

            if (!player.getWorld().getName().equals("world")) {
                player.setGameMode(GameMode.CREATIVE);
            } else {
                player.setGameMode(GameMode.ADVENTURE);
            }

            new GameScoreboard(player);
        });
    }
}
