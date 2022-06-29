package com.unlikepaladin.pfm.data;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

import java.util.Objects;

public abstract class Material {

    Identifier materialID;
    Block block;
    Material(Block block){
        this.block = block;
    }

    boolean isMaterialModded(){
        return !Objects.equals(materialID.getNamespace(), "minecraft:");
    }

    String getMaterialNamespace(){
        return materialID.getNamespace();
    }

    public Block getBlock() {
        return this.block;
    }
}
