package net.verany.setup.world;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.api.enumhelper.IdentifierType;
import org.bukkit.Material;

@AllArgsConstructor
@Getter
public enum WorldStatus implements IdentifierType<Material> {

    IN_WORK('6', Material.ORANGE_DYE),
    FINISHED('9', Material.GREEN_DYE);

    private final char colorCore;
    private final Material id;

}
