package com.unlikepaladin.pfm.blocks;

import net.minecraft.util.StringIdentifiable;

public enum ToiletState implements StringIdentifiable {
    CLEAN("clean"),
    DIRTY("dirty"),
    FLUSHING("flushing");

    private final String name;

    ToiletState(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}
