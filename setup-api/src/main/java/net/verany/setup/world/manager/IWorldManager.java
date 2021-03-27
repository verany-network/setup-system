package net.verany.setup.world.manager;

import net.verany.setup.world.WorldData;
import net.verany.setup.world.WorldStatus;

import java.util.List;

public interface IWorldManager {

    void reloadConfig();

    void save();

    void loadWorlds();

    List<WorldData> getLoadedWorlds();

    List<WorldData> getImportedWorlds();

    List<WorldData> getWorlds(WorldStatus status);

    WorldData getWorldData(String name);

    boolean importWorld(String name);

    void exportWorld(String name);

    void updateWorld(WorldData worldData);

    List<String> getUnloadedWorlds();

}
