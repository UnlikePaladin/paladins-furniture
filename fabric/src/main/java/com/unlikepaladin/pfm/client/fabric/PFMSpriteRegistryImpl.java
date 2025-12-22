package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.ducks.PFMSpriteContentExtensions;
import com.unlikepaladin.pfm.mixin.PFMMissingSpriteAccessor;
import net.minecraft.client.resource.metadata.AnimationResourceMetadata;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.texture.SpriteDimensions;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class PFMSpriteRegistryImpl {
    public static void registerSprite(Identifier spriteId) {
        PFMSpriteRegistry.PFM_SPRITES.put(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, spriteId);
    }

    public static void registerDynamicSprite(Identifier spriteId, List<VariantBase<?>> variantBaseList) {
        PFMSpriteRegistry.PFM_SPRITES.put(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, spriteId);
        PFMSpriteRegistry.DYNAMIC_SPRITE_GENERATORS.put(spriteId, (info) -> {
            List<Pair<Identifier, SpriteContents>> infos = new ArrayList<>();
            NativeImage image = PFMMissingSpriteAccessor.pfm$invokeCreateImage(info.getWidth(), info.getHeight());
            for (VariantBase<?> variantBase : variantBaseList) {
                Identifier templateId = info.getId();
                Identifier variantSpriteId = new Identifier(templateId.getNamespace(), templateId.getPath().replace("template", variantBase.getPath()));
                SpriteContents variantInfo = new SpriteContents(variantSpriteId, new SpriteDimensions(info.getWidth(), info.getHeight()),
                        image, AnimationResourceMetadata.EMPTY);
                ((PFMSpriteContentExtensions)variantInfo).pfm$setInitialized(false);
                infos.add(new Pair<>(variantSpriteId, variantInfo));
            }
            return infos;
        });
    }
}
