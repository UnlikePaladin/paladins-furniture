package com.unlikepaladin.pfm.blocks;

import net.minecraft.util.StringRepresentable;

public enum ToiletState implements StringRepresentable {
    CLEAN("clean"),
    DIRTY("dirty"),
    FLUSHING("flushing"),
    EMPTY("empty");

    private final String name;

    ToiletState(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
