package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.mixin.PFMSpriteInfoAccesor;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class PFMSpriteRegistryImpl {
    public static void registerSprite(ResourceLocation spriteId) {
        ClientSpriteRegistryCallback.event(InventoryMenu.BLOCK_ATLAS).register(
                (spriteRegistry, registry) -> {
                    registry.register(spriteId);
                }
        );
    }

    public static void registerDynamicSprite(ResourceLocation spriteId, List<VariantBase<?>> variantBaseList) {
        ClientSpriteRegistryCallback.event(InventoryMenu.BLOCK_ATLAS).register(
                (spriteRegistry, registry) -> {
                    registry.register(spriteId);
                }
        );
        PFMSpriteRegistry.DYNAMIC_SPRITE_GENERATORS.put(spriteId, (info) -> {
            List<TextureAtlasSprite.Info> infos = new ArrayList<>();
            for (VariantBase<?> variantBase : variantBaseList) {
                ResourceLocation templateId = info.name();
                ResourceLocation variantSpriteId = new ResourceLocation(templateId.getNamespace(), templateId.getPath().replace("template", variantBase.getPath()));
                TextureAtlasSprite.Info variantInfo = new TextureAtlasSprite.Info(variantSpriteId, info.width(), info.height(), ((PFMSpriteInfoAccesor)(Object)info).pfm$getAnimation());
                infos.add(variantInfo);
            }
            return infos;
        });
    }
}
