package com.unlikepaladin.pfm.blocks;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.DyeColor;

public interface DyeableFurniture {
    EnumProperty<DyeColor> COLORID = EnumProperty.of("color", DyeColor.class);
    BooleanProperty DYED = BooleanProperty.of("dyed");
}
