package net.verany.setup.scoreboard;

import net.verany.api.Verany;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.scoreboard.IScoreboardBuilder;
import net.verany.api.scoreboard.ScoreboardBuilder;
import net.verany.setup.SetupService;
import net.verany.setup.world.WorldData;
import org.bukkit.entity.Player;

public class GameScoreboard {

    private final Player player;
    private final IPlayerInfo playerInfo;
    private IScoreboardBuilder scoreboardBuilder;

    public GameScoreboard(Player player) {
        this.player = player;
        this.playerInfo = Verany.getPlayer(player);

        this.scoreboardBuilder = new ScoreboardBuilder(player);

        SetupService.INSTANCE.setMetadata(player, "scoreboard", this);

        setScores();
    }

    private void setScores() {
        WorldData worldData = getWorldData(player.getWorld().getName());
        scoreboardBuilder.setTitle("§b§lSetup§3§lService");

        scoreboardBuilder.setSlot(6, " ");
        scoreboardBuilder.setSlot(5, "World: ");
        scoreboardBuilder.setSlot(4, worldData.getName());
        scoreboardBuilder.setSlot(3, " ");
        scoreboardBuilder.setSlot(2, "Status: ");
        scoreboardBuilder.setSlot(1, Verany.getNameOfEnum(worldData.getStatus().name(), "§" + worldData.getStatus().getColorCore()));
        scoreboardBuilder.setSlot(0, " ");
    }

    public void update() {
        WorldData worldData = getWorldData(player.getWorld().getName());

        scoreboardBuilder.setSlot(6, " ");
        scoreboardBuilder.setSlot(5, "World: ");
        scoreboardBuilder.setSlot(4,worldData.getName());
        scoreboardBuilder.setSlot(3, " ");
        scoreboardBuilder.setSlot(2, "Status: ");
        scoreboardBuilder.setSlot(1, Verany.getNameOfEnum(worldData.getStatus().name(), "§" + worldData.getStatus().getColorCore()));
        scoreboardBuilder.setSlot(0, " ");
    }

    private WorldData getWorldData(String name) {
        return SetupService.INSTANCE.getWorldManager().getWorldData(name);
    }
}
