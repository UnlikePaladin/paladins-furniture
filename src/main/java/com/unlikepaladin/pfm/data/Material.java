package com.unlikepaladin.pfm.data;

import net.minecraft.util.Identifier;

import java.util.Objects;

public abstract class Material {

    Identifier materialID;
    Material(Identifier materialID){
        this.materialID = materialID;
    }

    boolean isMaterialModded(){
        return !Objects.equals(materialID.getNamespace(), "minecraft:");
    }

    String getMaterialNamespace(){
        return materialID.getNamespace();
    }

}
