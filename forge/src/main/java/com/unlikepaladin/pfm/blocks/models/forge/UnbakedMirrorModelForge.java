package com.unlikepaladin.pfm.blocks.models.forge;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.blocks.models.MirrorUnbakedModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class UnbakedMirrorModelForge extends MirrorUnbakedModel implements IModelGeometry<UnbakedMirrorModelForge> {
    public UnbakedMirrorModelForge(Identifier reflect, Identifier defaultFrameTexture, Identifier glass) {
        super(reflect, defaultFrameTexture, glass);
    }

    @Override
    public BakedModel bake(IModelConfiguration iModelConfiguration, ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, ModelOverrideList overrideList, Identifier identifier) {
        return super.bake(loader, textureGetter, rotationContainer, identifier);
    }

    @Override
    public Collection<SpriteIdentifier> getTextures(IModelConfiguration iModelConfiguration, Function<Identifier, UnbakedModel> function, Set<Pair<String, String>> set) {
        return super.getTextureDependencies(function, set);
    }
}