package net.verany.setup.world;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.api.item.VeranyItem;
import net.verany.api.json.AbstractJsonConfig;
import net.verany.api.loader.LoadObject;
import net.verany.api.loader.config.ConfigLoader;
import net.verany.api.loader.database.DatabaseLoadObject;
import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.module.VeranyProject;
import net.verany.setup.SetupService;
import net.verany.setup.world.manager.IWorldManager;
import org.bukkit.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class WorldManager extends DatabaseLoader implements IWorldManager {

    public WorldManager(VeranyProject project) {
        super(project, "worlds", "setup");
    }

    @Override
    public void reloadConfig() {
        remove(getInfo(WorldConfig.class));
        loadWorlds();
    }

    @Override
    public void loadWorlds() {
        load(new LoadInfo<>("worlds", WorldConfig.class, new WorldConfig()));

        for (WorldData importedWorld : getImportedWorlds()) {
            if (!importedWorld.isAutoLoad()) continue;
            createWorld(importedWorld.getName());
        }
    }

    private void createWorld(String name) {
        World world = Bukkit.createWorld(new WorldCreator(name));
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
    }

    @Override
    public List<WorldData> getLoadedWorlds() {
        if (getDataOptional(WorldConfig.class).isEmpty()) return new ArrayList<>();
        return getDataOptional(WorldConfig.class).get().getImportedWorlds().stream().filter(WorldData::isLoaded).collect(Collectors.toList());
    }

    @Override
    public List<WorldData> getImportedWorlds() {
        if (getDataOptional(WorldConfig.class).isEmpty()) return new ArrayList<>();
        return getDataOptional(WorldConfig.class).get().getImportedWorlds();
    }

    @Override
    public List<WorldData> getWorlds(WorldStatus status) {
        if (getDataOptional(WorldConfig.class).isEmpty()) return new ArrayList<>();
        return getImportedWorlds().stream().filter(worldData -> worldData.getStatus().equals(status)).collect(Collectors.toList());
    }

    @Override
    public WorldData getWorldData(String name) {
        return getImportedWorlds().stream().filter(worldData -> worldData.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public boolean importWorld(String name) {
        if (getWorldData(name) != null) return false;

        createWorld(name);

        getImportedWorlds().add(new WorldData(name, name, "unknown", WorldStatus.IN_WORK, Material.AIR, 0, true, true));
        save("worlds");

        return true;
    }

    @Override
    public void exportWorld(String name) {
        WorldData worldData = getWorldData(name);
        if (worldData == null) return;
        worldData.setStatus(WorldStatus.FINISHED);
        updateWorld(worldData);
    }

    @Override
    public void updateWorld(WorldData worldData) {
        getImportedWorlds().remove(getWorldData(worldData.getName()));
        getImportedWorlds().add(worldData);
        save("worlds");
    }

    @Override
    public List<String> getUnloadedWorlds(String pathname) {
        List<String> toReturn = new ArrayList<>();
        for (File file : new File(pathname).listFiles()) {
            String worldName = file.getName();
            if (getWorldData(worldName) == null || (getWorldData(worldName) != null && !getWorldData(worldName).isLoaded()))
                toReturn.add(worldName);
        }
        return toReturn;
    }

    @Getter
    public static class WorldConfig extends DatabaseLoadObject {
        private final List<WorldData> importedWorlds = new CopyOnWriteArrayList<>();

        public WorldConfig() {
            super("worlds");
        }
    }

}
