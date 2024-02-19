package com.unlikepaladin.pfm.config.option;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.nbt.*;

public class ConfigOptionTypes {
//
    private static final ConfigOptionType<?>[] VALUES = new ConfigOptionType[]{NullConfigOption.TYPE, BooleanConfigOption.TYPE};

    public static ConfigOptionType<?> byId(int id) {
        if (id < 0 || id >= VALUES.length) {
            PaladinFurnitureMod.GENERAL_LOGGER.warn("Invalid by ID Call: " + id);
            return ConfigOptionType.createInvalid(id);
        }
        return VALUES[id];
    }
}
