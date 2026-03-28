package com.unlikepaladin.pfm.blocks.models.fabric;

import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.client.fabric.PFMBakedModelParticleExtension;
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

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public abstract class PFMFabricBakedModel extends AbstractBakedModel implements FabricBakedModel, PFMBakedModelParticleExtension {
    public PFMFabricBakedModel(ModelState settings, List<BakedModel> bakedModels) {
        super(settings, bakedModels);
    }

    public void pushTextureTransform(RenderContext context, TextureAtlasSprite sprite) {
        context.pushTransform(quad -> {
            TextureAtlasSprite originalSprite = SpriteFinder.get(Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS)).find(quad, 0);
            if (originalSprite.getName() != sprite.getName()) {
                for (int index = 0; index < 4; index++) {
                    float frameU = originalSprite.getUOffset(quad.spriteU(index, 0));
                    float frameV = originalSprite.getVOffset(quad.spriteV(index, 0));
                    quad.sprite(index, 0, sprite.getU(frameU), sprite.getV(frameV));
                }
            }
            return true;
        });
    }
    public void pushTextureTransform(RenderContext context, List<TextureAtlasSprite> toReplace, List<TextureAtlasSprite> replacement) {
        pushTextureTransform(context, toReplace, replacement, InventoryMenu.BLOCK_ATLAS);
    }
    public void pushTextureTransform(RenderContext context, List<TextureAtlasSprite> toReplace, List<TextureAtlasSprite> replacement, ResourceLocation atlasId) {
        context.pushTransform(quad -> {
            if (replacement != null && toReplace != null ){
                TextureAtlasSprite originalSprite = SpriteFinder.get(Minecraft.getInstance().getModelManager().getAtlas(atlasId)).find(quad, 0);
                ResourceLocation keyId = originalSprite.getName();
                int textureIndex = IntStream.range(0, toReplace.size())
                        .filter(i -> keyId.equals(toReplace.get(i).getName()))
                        .findFirst()
                        .orElse(-1);

                if (textureIndex != -1 && !toReplace.equals(replacement)) {
                    TextureAtlasSprite sprite = replacement.get(textureIndex);
                    for (int index = 0; index < 4; index++) {
                        float frameU = originalSprite.getUOffset(quad.spriteU(index, 0));
                        float frameV = originalSprite.getVOffset(quad.spriteV(index, 0));
                        quad.sprite(index, 0, sprite.getU(frameU), sprite.getV(frameV));
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
}
