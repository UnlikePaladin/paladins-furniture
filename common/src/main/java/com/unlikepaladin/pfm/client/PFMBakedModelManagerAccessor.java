package com.unlikepaladin.pfm.client;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;

public interface PFMBakedModelManagerAccessor {
    BakedModel pfm$getModelFromNormalID(Identifier id);
}
