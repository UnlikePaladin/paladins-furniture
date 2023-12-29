package com.unlikepaladin.pfm.blocks.models.fabric;

import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.client.fabric.PFMBakedModelParticleExtension;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public abstract class PFMFabricBakedModel extends AbstractBakedModel implements FabricBakedModel, PFMBakedModelParticleExtension {
    public PFMFabricBakedModel(ModelBakeSettings settings, List<BakedModel> bakedModels) {
        super(settings, bakedModels);
    }

    public void pushTextureTransform(RenderContext context, Sprite sprite) {
        context.pushTransform(quad -> {
            Sprite originalSprite = SpriteFinder.get(MinecraftClient.getInstance().getBakedModelManager().getAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)).find(quad, 0);
            if (originalSprite.getContents().getId() != sprite.getContents().getId()) {
                for (int index = 0; index < 4; index++) {
                    float frameU = originalSprite.getFrameFromU(quad.spriteU(index, 0));
                    float frameV = originalSprite.getFrameFromV(quad.spriteV(index, 0));
                    quad.sprite(index, 0, sprite.getFrameU(frameU), sprite.getFrameV(frameV));
                }
            }
            return true;
        });
    }
    public void pushTextureTransform(RenderContext context, List<Sprite> toReplace, List<Sprite> replacement) {
        pushTextureTransform(context, toReplace, replacement, PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
    }
    public void pushTextureTransform(RenderContext context, List<Sprite> toReplace, List<Sprite> replacement, Identifier atlasId) {
        context.pushTransform(quad -> {
            if (replacement != null && toReplace != null ){
                Sprite originalSprite = SpriteFinder.get(MinecraftClient.getInstance().getBakedModelManager().getAtlas(atlasId)).find(quad, 0);
                Identifier keyId = originalSprite.getContents().getId();
                int textureIndex = IntStream.range(0, toReplace.size())
                        .filter(i -> keyId.equals(toReplace.get(i).getContents().getId()))
                        .findFirst()
                        .orElse(-1);

                if (textureIndex != -1 && !toReplace.equals(replacement)) {
                    Sprite sprite = replacement.get(textureIndex);
                    for (int index = 0; index < 4; index++) {
                        float frameU = originalSprite.getFrameFromU(quad.spriteU(index, 0));
                        float frameV = originalSprite.getFrameFromV(quad.spriteV(index, 0));
                        quad.sprite(index, 0, sprite.getFrameU(frameU), sprite.getFrameV(frameV));
                    }
                }
            }
            return true;
        });
    }


    @Override
    public Sprite pfm$getParticle(World world, BlockPos pos, BlockState state) {
        return pfm$getParticle(state);
    }

    @Override
    public Sprite getParticleSprite() {
        return getTemplateBakedModels().get(0).getParticleSprite();
    }
}
