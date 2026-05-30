package com.unlikepaladin.pfm.blocks.models.fabric;

import com.unlikepaladin.pfm.client.fabric.PFMModelLoadingPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.Identifier;

public class ModelHelperImpl {
    public static BlockStateModel getModelFromIdentifier(Identifier id) {
        return Minecraft.getInstance().getModelManager().getModel(PFMModelLoadingPlugin.modelKeyMap.get(id));
    }
}
