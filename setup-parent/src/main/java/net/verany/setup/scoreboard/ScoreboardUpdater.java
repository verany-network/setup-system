package net.verany.setup.scoreboard;

import net.verany.api.Verany;
import net.verany.api.task.AbstractTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ScoreboardUpdater extends AbstractTask {

    public ScoreboardUpdater(long waitTime) {
        super(waitTime);
    }

    @Override
    public void run() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if(!onlinePlayer.hasMetadata("scoreboard")) return;
            GameScoreboard gameScoreboard = (GameScoreboard) onlinePlayer.getMetadata("scoreboard").get(0).value();
            gameScoreboard.update();
        }
    }
}
