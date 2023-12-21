package com.unlikepaladin.pfm.blocks.models.fabric;

import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.client.fabric.PFMBakedModelParticleExtension;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.screen.PlayerScreenHandler;

import java.util.List;
import java.util.Map;

public abstract class PFMFabricBakedModel extends AbstractBakedModel implements FabricBakedModel, PFMBakedModelParticleExtension {
    public PFMFabricBakedModel(ModelBakeSettings settings, List<BakedModel> bakedModels) {
        super(settings, bakedModels);
    }

    public void pushTextureTransform(RenderContext context, Sprite sprite) {
        context.pushTransform(quad -> {
            Sprite originalSprite = SpriteFinder.get(MinecraftClient.getInstance().getBakedModelManager().getAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)).find(quad, 0);
            if (originalSprite.getId() != sprite.getId()) {
                for (int index = 0; index < 4; index++) {
                    float frameU = originalSprite.method_35804(quad.spriteU(index, 0));
                    float frameV = originalSprite.method_35805(quad.spriteV(index, 0));
                    quad.sprite(index, 0, sprite.getFrameU(frameU), sprite.getFrameV(frameV));
                }
            }
            return true;
        });
    }

    public void pushTextureTransform(RenderContext context, List<Sprite> toReplace, List<Sprite> replacement) {
        context.pushTransform(quad -> {
            Sprite originalSprite = SpriteFinder.get(MinecraftClient.getInstance().getBakedModelManager().getAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)).find(quad, 0);
            if (toReplace.stream().anyMatch(sprite -> originalSprite.getId().equals(sprite.getId())) && !toReplace.equals(replacement)) {
                Sprite sprite = replacement.get(toReplace.indexOf(originalSprite));
                for (int index = 0; index < 4; index++) {
                    float frameU = originalSprite.method_35804(quad.spriteU(index, 0));
                    float frameV = originalSprite.method_35805(quad.spriteV(index, 0));
                    quad.sprite(index, 0, sprite.getFrameU(frameU), sprite.getFrameV(frameV));
                }
            }
            return true;
        });
    }


    @Override
    public Sprite getParticleSprite() {
        return getTemplateBakedModels().get(0).getParticleSprite();
    }
}
