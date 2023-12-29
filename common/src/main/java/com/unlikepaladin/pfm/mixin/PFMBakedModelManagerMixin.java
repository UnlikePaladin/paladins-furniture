package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.client.PFMBakedModelManagerAccessor;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(BakedModelManager.class)
public abstract class PFMBakedModelManagerMixin implements PFMBakedModelManagerAccessor {
    @Shadow
    private Map<Identifier, BakedModel> models;

    @Shadow private BakedModel missingModel;

    @Override
    public BakedModel pfm$getModelFromNormalID(Identifier id) {
        return models.getOrDefault(id, missingModel);
    }
}
