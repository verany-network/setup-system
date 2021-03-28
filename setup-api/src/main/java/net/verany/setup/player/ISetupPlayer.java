package net.verany.setup.player;

import net.verany.api.gamemode.AbstractGameMode;
import net.verany.api.interfaces.IDefault;

import java.util.UUID;

public interface ISetupPlayer extends IDefault<UUID> {

    AbstractGameMode getCurrentEditing();

    void setCurrentEditing(AbstractGameMode gameMode);

    void setItems();

}
