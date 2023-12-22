package com.unlikepaladin.pfm.data.materials;

import net.minecraft.util.StringIdentifiable;

public enum BlockType implements StringIdentifiable {
    PLANKS( "planks"),
    LOG( "log"),
    LOG_TOP( "log_top"),
    PRIMARY( "primary"),
    SECONDARY( "secondary"),
    STRIPPED_LOG("stripped", "log"),
    STRIPPED_LOG_TOP("stripped", "log_top"),
    BLOCK( "block");

    private final String prefix;
    private final String postfix;
    BlockType(String prefix, String postfix) {
        this.postfix = postfix;
        this.prefix = prefix;
    }
    BlockType(String postfix) {
        this(null, postfix);
    }

    public String getPostfix() {
        return postfix;
    }

    public String getPrefix() {
        if (prefix != null)
            return prefix;
        return "";
    }

    @Override
    public String asString() {
        return getPrefix() + "_" + getPostfix();
    }
}
