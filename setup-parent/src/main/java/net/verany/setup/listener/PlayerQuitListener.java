package net.verany.setup.listener;

import net.verany.api.Verany;
import net.verany.api.event.AbstractListener;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.IPlayerInfo;
import net.verany.setup.SetupService;
import net.verany.setup.player.ISetupPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener extends AbstractListener {

    public PlayerQuitListener(VeranyProject project) {
        super(project);

        Verany.registerListener(project, PlayerQuitEvent.class, event -> {
            Player player = event.getPlayer();
            IPlayerInfo playerInfo = Verany.getPlayer(player);

            for (IPlayerInfo onlinePlayer : Verany.getOnlinePlayers()) {
                onlinePlayer.sendMessage(onlinePlayer.getPrefix(SetupService.INSTANCE.getModule()) + playerInfo.getNameWithColor() + " §7has left the server§8.");
            }

            SetupService.INSTANCE.removeMetadata(player, "scoreboard");

            Verany.removePlayer(player.getUniqueId().toString(), ISetupPlayer.class);
        });
    }
}
