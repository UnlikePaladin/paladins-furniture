package com.unlikepaladin.pfm.client.forge;

import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.ducks.PFMSpriteContentExtensions;
import com.unlikepaladin.pfm.mixin.PFMMissingSpriteAccessor;
import net.minecraft.client.resource.metadata.AnimationResourceMetadata;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.texture.SpriteDimensions;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
