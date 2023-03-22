package com.unlikepaladin.pfm.blocks.materials;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

public enum WoodVariant implements MaterialEnum {
        OAK(Blocks.OAK_PLANKS, "oak"),
        SPRUCE(Blocks.SPRUCE_PLANKS, "spruce"),
        BIRCH(Blocks.BIRCH_PLANKS, "birch"),
        JUNGLE(Blocks.JUNGLE_PLANKS, "jungle"),
        ACACIA(Blocks.ACACIA_PLANKS, "acacia"),
        DARK_OAK(Blocks.DARK_OAK_PLANKS, "dark_oak"),
        CRIMSON(Blocks.CRIMSON_PLANKS, "crimson"),
        WARPED(Blocks.WARPED_PLANKS, "warped");

        private final String type;
        private final Block baseBlock;

        WoodVariant(Block baseBlock, String type) {
            this.type = type;
            this.baseBlock = baseBlock;
        }

    @Override
    public String asString() {
        return this.type;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public Identifier getTexture(BlockType type) {
        String path = "block/";
        String prefix = type.getPrefix().trim().isBlank() ? "" : type.getPrefix() + "_";
        String postfix = this == CRIMSON || this == WARPED ? type.getPostfix().replace("log", "stem"): type.getPostfix();
        return new Identifier("minecraft",path + prefix + this.type + "_" + postfix);
    }

    public String getType() {
            return this.type;
    }

    @Override
    public Block getBaseBlock() {
        return this.baseBlock;
    }

    public String toString() {
            return this.type;
        }
}