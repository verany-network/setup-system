package net.verany.setup.inventory;

import net.verany.api.Verany;
import net.verany.api.gamemode.AbstractGameMode;
import net.verany.api.gamemode.GameModeWrapper;
import net.verany.api.inventory.InventoryBuilder;
import net.verany.api.itembuilder.ItemBuilder;
import net.verany.api.player.IPlayerInfo;
import net.verany.setup.SetupService;
import net.verany.setup.world.WorldData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SetupInventory {

    private final Player player;
    private final IPlayerInfo playerInfo;

    public SetupInventory(Player player) {
        this.player = player;
        this.playerInfo = Verany.getPlayer(player);
    }

    public void setItems() {
        Inventory inventory = InventoryBuilder.builder().size(9 * 3).title("§bNavigator").event(event -> {
            event.setCancelled(true);

            if (event.getCurrentItem().getType().equals(Material.GRASS_BLOCK)) {
                Bukkit.getScheduler().runTaskLater(SetupService.INSTANCE, this::setWorldItems, 2);
            } else if (event.getCurrentItem().getType().equals(Material.FIREWORK_ROCKET)) {
                Bukkit.getScheduler().runTaskLater(SetupService.INSTANCE, this::setGamesItems, 2);
            }

        }).build().buildAndOpen(player);

        inventory.setItem(11, new ItemBuilder(Material.GRASS_BLOCK).setDisplayName("§7Worlds").build());
        inventory.setItem(15, new ItemBuilder(Material.FIREWORK_ROCKET).setDisplayName("§7Games").build());

    }

    public void setWorldItems() {
        Inventory inventory = InventoryBuilder.builder().size(9 * 6).title("§bWorlds").event(event -> {
            event.setCancelled(true);

            if (event.getCurrentItem().getType().equals(Material.CLAY_BALL)) {
                setItems();
                return;
            }

            player.performCommand("setup tp " + event.getCurrentItem().getItemMeta().getDisplayName());

        }).build().buildAndOpen(player);

        for (WorldData importedWorld : SetupService.INSTANCE.getWorldManager().getImportedWorlds()) {
            inventory.addItem(new ItemBuilder(importedWorld.getItem().equals(Material.AIR) ? SetupService.Item.QUESTION_MARK.clone() : new ItemStack(importedWorld.getItem())).setGlow(player.getWorld().equals(importedWorld.getName())).setDisplayName(importedWorld.getName()).build());
        }

        inventory.setItem(inventory.getSize() -9, new ItemBuilder(Material.CLAY_BALL).setDisplayName(playerInfo.getKey("inventory.back")).build());
    }

    public void setGamesItems() {
        Inventory inventory = InventoryBuilder.builder().size(9 * 6).title("§bGames").event(event -> {
            event.setCancelled(true);

            if (event.getCurrentItem().getType().equals(Material.CLAY_BALL)) {
                setItems();
                return;
            }

        }).build().buildAndOpen(player);

        for (AbstractGameMode value : GameModeWrapper.VALUES) {
            inventory.addItem(new ItemBuilder(Material.ARMOR_STAND).setDisplayName(value.getName()).build());
        }

        inventory.setItem(inventory.getSize() -9, new ItemBuilder(Material.CLAY_BALL).setDisplayName(playerInfo.getKey("inventory.back")).build());
    }
}
