package net.verany.setup;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import net.verany.api.Verany;
import net.verany.api.config.IngameConfig;
import net.verany.api.database.DatabaseManager;
import net.verany.api.gamemode.AbstractGameMode;
import net.verany.api.gamemode.GameModeWrapper;
import net.verany.api.json.JsonConfig;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyProject;
import net.verany.api.setup.AbstractSetupObject;
import net.verany.api.setup.SetupObject;
import net.verany.api.skull.SkullBuilder;
import net.verany.setup.command.SetupCommand;
import net.verany.setup.listener.PlayerJoinListener;
import net.verany.setup.listener.PlayerQuitListener;
import net.verany.setup.listener.ProtectionListener;
import net.verany.setup.scoreboard.ScoreboardUpdater;
import net.verany.setup.world.WorldManager;
import net.verany.setup.world.manager.IWorldManager;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
@VeranyModule(name = "SetupService", prefix = "SetupService", version = "2021.3.1", authors = {"tylix"}, user = "tylix", host = "159.69.63.105", password = "RxNqA18HB56SS7GW", databases = {"setup"})
public class SetupService extends VeranyProject {

    public static SetupService INSTANCE;

    private final Map<AbstractGameMode, AbstractSetupObject> setupObjectMap = new HashMap<>();

    private IWorldManager worldManager;

    @Override
    public void onEnable() {
        INSTANCE = this;

        Verany.loadModule(this);

        init();
    }

    @Override
    public void init() {
        IngameConfig.PLAYER_COLLISION.setValue(false);
        IngameConfig.TAB_LIST.setValue(true);
        IngameConfig.TAB_LIST_FORMAT.setValue("{0}{1} §8▏ §7");
        IngameConfig.TAB_LIST_CLAN.setValue(true);
        IngameConfig.CHAT_FORMAT.setValue(" §8◗§7◗ {0}{1} §8▏ §7{2} §8• §f{3}");

        loadGames();

        Verany.addTask(new ScoreboardUpdater(1000));

        worldManager = new WorldManager(new JsonConfig(new File("plugins/setup/worlds.json")));
        worldManager.loadWorlds();

        initCommands();
        initListener();
    }

    private void initListener() {
        new PlayerJoinListener(this);
        new PlayerQuitListener(this);
        new ProtectionListener(this);
    }

    private void initCommands() {
        new SetupCommand(this);
    }

    @Override
    public void onDisable() {
        worldManager.save();
        getConnection().disconnect();
    }

    private void loadGames() {
        for (AbstractGameMode value : GameModeWrapper.VALUES) {
            for (String databaseName : value.getDatabaseNames()) {
                getConnection().getDatabaseManagers().add(new DatabaseManager(getModule().user(), getModule().host(), getModule().password(), databaseName));

                MongoCollection<Document> collection = getConnection().getCollection(databaseName, "locations");
                if (collection == null) return;
                AbstractSetupObject setupObject = new SetupObject(this, databaseName);
                setupObjectMap.put(value, setupObject);

            }
        }
    }

    public static class Item {
        public static final ItemStack QUESTION_MARK = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmFkYzA0OGE3Y2U3OGY3ZGFkNzJhMDdkYTI3ZDg1YzA5MTY4ODFlNTUyMmVlZWQxZTNkYWYyMTdhMzhjMWEifX19").build();
    }
}
