package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.mixin.PFMSpriteInfoAccesor;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.texture.Sprite;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class PFMSpriteRegistryImpl {
    public static void registerSprite(Identifier spriteId) {
        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(
                (spriteRegistry, registry) -> {
                    registry.register(spriteId);
                }
        );
    }

    public static void registerDynamicSprite(Identifier spriteId, List<VariantBase<?>> variantBaseList) {
        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(
                (spriteRegistry, registry) -> {
                    registry.register(spriteId);
                }
        );
        PFMSpriteRegistry.DYNAMIC_SPRITE_GENERATORS.put(spriteId, (info) -> {
            List<Sprite.Info> infos = new ArrayList<>();
            for (VariantBase<?> variantBase : variantBaseList) {
                Identifier templateId = info.getId();
                Identifier variantSpriteId = new Identifier(templateId.getNamespace(), templateId.getPath().replace("template", variantBase.getPath()));
                Sprite.Info variantInfo = new Sprite.Info(variantSpriteId, info.getWidth(), info.getHeight(), ((PFMSpriteInfoAccesor)(Object)info).pfm$getAnimation());
                infos.add(variantInfo);
            }
            return infos;
        });
    }
}
