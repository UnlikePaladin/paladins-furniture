package com.unlikepaladin.pfm.blocks;

import net.minecraft.util.StringIdentifiable;

public enum MiddleShape implements StringIdentifiable {
    LEFT("left"),
    RIGHT("right"),
    MIDDLE("middle"),
    SINGLE("single");

    private final String name;
    MiddleShape(String name) {
        this.name = name;
    }
    public String toString() {
        return this.name;
    }

    public String asString() {
        return this.name;
    }

}
