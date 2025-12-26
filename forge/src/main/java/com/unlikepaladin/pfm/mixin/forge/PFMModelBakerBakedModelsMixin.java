package com.unlikepaladin.pfm.mixin.forge;

import com.unlikepaladin.pfm.ducks.forge.PFModelBakerBakedExtensions;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBaker;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;
@Mixin(ModelBaker.BakedModels.class)
abstract class PFMModelBakerBakedModelsMixin implements PFModelBakerBakedExtensions {
    @Unique
    @Nullable
    private Map<Identifier, BakedModel> pfm$extraModels;

    @Override
    @Nullable
    public Map<Identifier, BakedModel> pfm_getExtraModels() {
        return pfm$extraModels;
    }

    @Override
    public void pfm_setExtraModels(@Nullable Map<Identifier, BakedModel> extraModels) {
        this.pfm$extraModels = extraModels;
    }

}
