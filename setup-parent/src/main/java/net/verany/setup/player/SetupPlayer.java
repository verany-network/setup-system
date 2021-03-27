package net.verany.setup.player;

import lombok.Getter;
import lombok.Setter;
import net.verany.api.gamemode.AbstractGameMode;
import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.module.VeranyProject;

import java.util.UUID;

@Getter
@Setter
public class SetupPlayer extends DatabaseLoader implements ISetupPlayer {

    private UUID uniqueId;

    private AbstractGameMode currentEditing;

    public SetupPlayer(VeranyProject project) {
        super(project, "players", "setup");
    }

    @Override
    public void load(UUID uuid) {
        this.uniqueId = uuid;
    }

    @Override
    public void update() {
    }

}
