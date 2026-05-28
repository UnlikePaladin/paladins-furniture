package com.unlikepaladin.pfm.config.option;

import net.minecraft.util.StringRepresentable;

import java.util.Objects;

public enum Side implements StringRepresentable {
    CLIENT("client"),
    SERVER("server");

    private final String name;

    Side(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}

