package com.unlikepaladin.pfm.blocks.materials;

import net.minecraft.util.StringIdentifiable;

import javax.annotation.Nullable;

public enum BlockType implements StringIdentifiable {
    PLANKS(null, "planks"),
    LOG(null, "log"),
    STRIPPED_LOG("stripped", "log"),
    BLOCK(null, "block");

    @Nullable
    private final String prefix;
    private final String postfix;
    BlockType(@Nullable String prefix, String postfix) {
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
