package com.unlikepaladin.pfm.blocks.models.neoforge;

import com.unlikepaladin.pfm.client.neoforge.PaladinFurnitureModClientNeoForge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.ResourceLocation;

public class ModelHelperImpl {
    public static BlockStateModel getModelFromIdentifier(ResourceLocation id) {
        return Minecraft.getInstance().getModelManager().getStandaloneModel(PaladinFurnitureModClientNeoForge.modelKeyMap.get(id));
    }
}
