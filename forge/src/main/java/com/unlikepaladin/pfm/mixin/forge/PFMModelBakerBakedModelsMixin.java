package com.unlikepaladin.pfm.mixin.forge;

import com.unlikepaladin.pfm.ducks.forge.PFModelBakerBakedExtensions;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;
@Mixin(ModelBakery.BakingResult.class)
abstract class PFMModelBakerBakedModelsMixin implements PFModelBakerBakedExtensions {
    @Unique
    @Nullable
    private Map<Identifier, BlockStateModel> pfm$extraModels;

    @Override
    @Nullable
    public Map<Identifier, BlockStateModel> pfm_getExtraModels() {
        return pfm$extraModels;
    }

    @Override
    public void pfm_setExtraModels(@Nullable Map<Identifier, BlockStateModel> extraModels) {
        this.pfm$extraModels = extraModels;
    }

}
