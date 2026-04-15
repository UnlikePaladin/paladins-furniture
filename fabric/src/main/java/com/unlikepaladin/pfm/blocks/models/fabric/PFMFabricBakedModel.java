package com.unlikepaladin.pfm.blocks.models.fabric;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.client.fabric.PFMBakedModelParticleExtension;
import com.unlikepaladin.pfm.client.model.PFMBakedModelGetQuadsExtension;
import com.unlikepaladin.pfm.client.model.PFMBakedModelSetPropertiesExtension;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public abstract class PFMFabricBakedModel extends AbstractBakedModel implements FabricBakedModel, PFMBakedModelParticleExtension, PFMBakedModelSetPropertiesExtension {
    protected BlockState blockState;
    protected VariantBase<?> variant;

    public PFMFabricBakedModel(ModelState settings, List<BakedModel> bakedModels) {
        super(settings, bakedModels);
    }

    public void pushTextureTransform(QuadEmitter context, TextureAtlasSprite sprite) {
        context.pushTransform(quad -> {
            TextureAtlasSprite originalSprite = SpriteFinder.get(Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS)).find(quad, 0);
            if (originalSprite.contents().name() != sprite.contents().name()) {
                for (int index = 0; index < 4; index++) {
                    float frameU = originalSprite.getUOffset(quad.u(index));
                    float frameV = originalSprite.getVOffset(quad.v(index));
                    quad.uv(index, sprite.getU(frameU), sprite.getV(frameV));
                }
            }
            return true;
        });
    }
    public void pushTextureTransform(QuadEmitter context, List<TextureAtlasSprite> toReplace, List<TextureAtlasSprite> replacement) {
        pushTextureTransform(context, toReplace, replacement, TextureAtlas.LOCATION_BLOCKS);
    }
    public void pushTextureTransform(QuadEmitter context, List<TextureAtlasSprite> toReplace, List<TextureAtlasSprite> replacement, ResourceLocation atlasId) {
        context.pushTransform(quad -> {
            if (replacement != null && toReplace != null ){
                TextureAtlasSprite originalSprite = SpriteFinder.get(Minecraft.getInstance().getModelManager().getAtlas(atlasId)).find(quad, 0);
                ResourceLocation keyId = originalSprite.contents().name();
                int textureIndex = IntStream.range(0, toReplace.size())
                        .filter(i -> keyId.equals(toReplace.get(i).contents().name()))
                        .findFirst()
                        .orElse(-1);

                if (textureIndex != -1 && !toReplace.equals(replacement)) {
                    TextureAtlasSprite sprite = replacement.get(textureIndex);
                    for (int index = 0; index < 4; index++) {
                        float frameU = originalSprite.getUOffset(quad.u(index));
                        float frameV = originalSprite.getVOffset(quad.v(index));
                        quad.uv(index, sprite.getU(frameU), sprite.getV(frameV));
                    }
                }
            }
            return true;
        });
    }


    @Override
    public TextureAtlasSprite pfm$getParticle(Level world, BlockPos pos, BlockState state) {
        return pfm$getParticle(state);
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return getTemplateBakedModels().get(0).getParticleIcon();
    }

    @Override
    public void setBlockStateProperty(BlockState state) {
        this.blockState = state;
    }

    @Override
    public void setVariant(VariantBase<?> variant) {
        this.variant = variant;
    }

    @Override
    public BlockState getBlockStateProperty() {
        return blockState;
    }

    @Override
    public VariantBase<?> getVariant() {
        return variant;
    }
}
