package net.verany.setup.listener;

import net.verany.api.Verany;
import net.verany.api.event.AbstractListener;
import net.verany.api.event.events.PlayerLoadCompleteEvent;
import net.verany.api.gamemode.AbstractGameMode;
import net.verany.api.gamemode.GameModeWrapper;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.IPlayerInfo;
import net.verany.setup.SetupService;
import net.verany.setup.player.ISetupPlayer;
import net.verany.setup.player.SetupPlayer;
import org.bukkit.entity.Player;

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
        });
    }
}
