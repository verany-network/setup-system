package net.verany.setup.player;

import lombok.Getter;
import lombok.Setter;
import net.verany.api.Verany;
import net.verany.api.gamemode.AbstractGameMode;
import net.verany.api.hotbar.HotbarItem;
import net.verany.api.itembuilder.ItemBuilder;
import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.IPlayerInfo;
import net.verany.setup.inventory.SetupInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

@Getter
@Setter
public class SetupPlayer extends DatabaseLoader implements ISetupPlayer {

    private UUID uniqueId;
    private IPlayerInfo playerInfo;
    private Player player;

    private AbstractGameMode currentEditing;

    public SetupPlayer(VeranyProject project) {
        super(project, "players", "setup");
    }

    @Override
    public void load(UUID uuid) {
        this.uniqueId = uuid;
        this.playerInfo = Verany.getPlayer(uuid);
        this.player = Bukkit.getPlayer(uuid);
    }

    @Override
    public void update() {
    }

    @Override
    public void setItems() {
        player.getInventory().clear();

        playerInfo.setItem(4, new HotbarItem(new ItemBuilder(Material.COMPASS).setDisplayName("§bNavigator"), player) {
            @Override
            public void onInteract(PlayerInteractEvent event) {
                if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
                    new SetupInventory(player).setItems();
            }

            @Override
            public void onDrop(PlayerDropItemEvent event) {
                event.setCancelled(true);
            }

            @Override
            public void onClick(InventoryClickEvent event) {
                event.setCancelled(true);
            }
        });
    }
}
