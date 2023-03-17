package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.MirrorUnbakedModel;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModelProviderFabric implements ModelResourceProvider {
    private static final Identifier[] MIRROR_MODEL_IDS = {new Identifier(PaladinFurnitureMod.MOD_ID, "block/white_mirror")};
    @Override
    public @Nullable UnbakedModel loadModelResource(Identifier resourceId, ModelProviderContext context) throws ModelProviderException {
        if (containsIdentifier(MIRROR_MODEL_IDS, resourceId)){
            return new MirrorUnbakedModel(MirrorUnbakedModel.DEFAULT_REFLECT, MirrorUnbakedModel.DEFAULT_FRAME_TEXTURE, MirrorUnbakedModel.DEFAULT_GLASS);}
        else
            return null;
    }

    public static boolean containsIdentifier(Identifier[] modelIds, Identifier comparison) {
        AtomicBoolean contains = new AtomicBoolean(false);
        Arrays.stream(modelIds).forEach(identifier -> {
            if (comparison.getPath().equals(identifier.getPath())){
                contains.set(true);
            }
        });
        return contains.get();
    }

}
