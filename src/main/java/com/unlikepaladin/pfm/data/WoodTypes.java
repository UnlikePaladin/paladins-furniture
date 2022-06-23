package com.unlikepaladin.pfm.data;

import net.minecraft.util.Identifier;

public class WoodTypes extends Material {
    Identifier plankName;
    Identifier logType;

    WoodTypes(Identifier materialID, Identifier plankName, Identifier logType) {
        super(materialID);
        this.plankName = plankName;
        this.logType = logType;
    }

}
