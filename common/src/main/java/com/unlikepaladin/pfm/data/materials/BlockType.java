package com.unlikepaladin.pfm.data.materials;

import net.minecraft.util.StringIdentifiable;

public enum BlockType implements StringIdentifiable {
    PLANKS(null, "planks"),
    LOG(null, "log"),
    STRIPPED_LOG("stripped", "log"),
    BLOCK(null, "block");

    private final String prefix;
    private final String postfix;
    BlockType(String prefix, String postfix) {
        this.postfix = postfix;
        this.prefix = prefix;
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
