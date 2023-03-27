package com.unlikepaladin.pfm.blocks.materials;

import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.util.Identifier;

public enum ExtraCounterVariants implements MaterialEnum {
    DARK_CONCRETE(Blocks.GRAY_CONCRETE,"dark_concrete"),
    CONCRETE(PaladinFurnitureModBlocksItems.RAW_CONCRETE,"concrete"),
    SMOOTH_STONE(PaladinFurnitureModBlocksItems.RAW_CONCRETE,"smooth_stone"),
    DEEPSLATE_TILE(Blocks.DEEPSLATE_TILES,"deepslate_tile");

    private final String name;
    private final Block baseBlock;

    ExtraCounterVariants(Block baseBlock,String name) {
        this.name = name;
        this.baseBlock = baseBlock;
    }

    @Override
    public String asString() {
        return name;
    }

    @Override
    public Block getBaseBlock() {
        return baseBlock;
    }

    @Override
    public boolean isNetherWood() {
        return false;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public Identifier getTexture(BlockType type) {
        BlockState state = baseBlock.getDefaultState();
        Identifier id = BlockModels.getModelId(state);
        return new Identifier(id.getNamespace(), "block/" + id.getPath());
    }
}