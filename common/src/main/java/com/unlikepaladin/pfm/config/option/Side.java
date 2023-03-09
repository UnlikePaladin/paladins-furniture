package com.unlikepaladin.pfm.config.option;

import net.minecraft.util.StringIdentifiable;

import java.util.Objects;

public enum Side implements StringIdentifiable {
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
    public String asString() {
        return name;
    }
}

