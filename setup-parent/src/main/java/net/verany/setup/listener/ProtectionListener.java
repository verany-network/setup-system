package net.verany.setup.listener;

import net.verany.api.Verany;
import net.verany.api.event.AbstractListener;
import net.verany.api.module.VeranyProject;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class ProtectionListener extends AbstractListener {

    public ProtectionListener(VeranyProject project) {
        super(project);

        Verany.registerListener(project, BlockPlaceEvent.class, event -> {
            event.setCancelled(true);
        });

        Verany.registerListener(project, BlockBreakEvent.class, event -> {
            event.setCancelled(true);
        });
    }
}
