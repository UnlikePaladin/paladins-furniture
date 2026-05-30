package com.unlikepaladin.pfm.data;

import net.minecraft.world.level.block.Block;
import net.minecraft.resources.Identifier;

import java.util.Objects;

public abstract class PFMMaterial {

    Identifier materialID;
    Block block;
    PFMMaterial(Block block){
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
