package com.unlikepaladin.pfm.blocks.materials;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

public enum StoneVariant implements MaterialEnum {
    QUARTZ(Blocks.QUARTZ_BLOCK,"quartz"),
    NETHERITE(Blocks.NETHERITE_BLOCK,"netherite"),
    LIGHT_WOOD(Blocks.STRIPPED_OAK_LOG,"light_wood"),
    DARK_WOOD(Blocks.STRIPPED_DARK_OAK_LOG,"dark_wood"),
    GRANITE(Blocks.GRANITE,"granite"),
    CALCITE(Blocks.CALCITE,"calcite"),
    ANDESITE(Blocks.ANDESITE,"andesite"),
    DIORITE(Blocks.DIORITE,"diorite"),
    DEEPSLATE(Blocks.DEEPSLATE,"deepslate"),
    BLACKSTONE(Blocks.BLACKSTONE,"blackstone"),
    STONE(Blocks.STONE,"stone");

    private final String name;
    private final Block baseBlock;

    StoneVariant(Block baseBlock,String name) {
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
    @Environment(EnvType.CLIENT)
    @Override
    public Identifier getTexture(BlockType type) {
        BlockState state = baseBlock.getDefaultState();
        String id = BlockModels.getModelId(state).getPath();
        if (id.contains("quartz"))
            id = id + "_top";
        return new Identifier("block/" + id);
    }
}