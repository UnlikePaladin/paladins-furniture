package com.unlikepaladin.pfm.mixin;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(TextureAtlas.class)
public interface PFMTextureAtlasAccessor {
    @Invoker("load")
    TextureAtlasSprite invoke$loadSprite(ResourceManager container, TextureAtlasSprite.Info info, int atlasWidth, int atlasHeight, int maxLevel, int x, int y);

    @Accessor("texturesByName")
    Map<ResourceLocation, TextureAtlasSprite> pfm$getTexturesByName();
}
