package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.ducks.PFMSpriteContentExtensions;
import net.minecraft.client.renderer.texture.SpriteContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SpriteContents.class)
public class PFMSpriteContentsMixin implements PFMSpriteContentExtensions {

    @Unique
    boolean pfm$initialized = true;

    @Override
    public boolean pfm$isInitialized() {
        return pfm$initialized;
    }

    @Override
    public void pfm$setInitialized(boolean initialized) {
        this.pfm$initialized = initialized;
    }
}
