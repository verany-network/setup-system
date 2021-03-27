package net.verany.setup.world;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class WorldData {

    private final String name;
    private String mapName = "unknown";
    private String gameMode = "unknown";
    private WorldStatus status = WorldStatus.IN_WORK;
    private Material item;
    private int maxUpgraded = 0;
    private boolean autoLoad = true;
    private boolean loaded = false;
    private final List<String> builder = Collections.emptyList();
    private final List<String> flags = Collections.emptyList();

}
