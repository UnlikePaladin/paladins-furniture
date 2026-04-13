package com.unlikepaladin.pfm.blocks;

import net.minecraft.util.StringRepresentable;

public enum MiddleShape implements StringRepresentable {
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

    public String getSerializedName() {
        return this.name;
    }

}
