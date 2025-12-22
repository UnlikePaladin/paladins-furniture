package com.unlikepaladin.pfm.mixin;

import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(SpriteAtlasTexture.class)
public interface PFMSpriteAtlasTextureAccessor {
    @Accessor("sprites")
    Map<Identifier, Sprite> pfm$getSprites();
}
