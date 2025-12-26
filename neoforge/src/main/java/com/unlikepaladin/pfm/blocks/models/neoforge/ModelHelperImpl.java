package com.unlikepaladin.pfm.blocks.models.neoforge;

import com.unlikepaladin.pfm.client.neoforge.PaladinFurnitureModClientNeoForge;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.util.Identifier;

public class ModelHelperImpl {
    public static BlockStateModel getModelFromIdentifier(Identifier id) {
        return MinecraftClient.getInstance().getBakedModelManager().getStandaloneModel(PaladinFurnitureModClientNeoForge.modelKeyMap.get(id));
    }
}
