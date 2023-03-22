package com.unlikepaladin.pfm.blocks.materials;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

public interface MaterialEnum extends StringIdentifiable {

    @Environment(EnvType.CLIENT)
    Identifier getTexture(BlockType type);

    Block getBaseBlock();
}
