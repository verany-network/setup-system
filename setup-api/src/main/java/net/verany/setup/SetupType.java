package net.verany.setup;

import java.util.Arrays;

public enum SetupType {

    LOCATION,
    WORLD,
    //TYPES,
    SKULL,
    SIGN;

    public static SetupType fromName(String name) {
        return Arrays.stream(values()).filter(setupType -> setupType.name().equals(name)).findFirst().orElse(null);
    }

}
