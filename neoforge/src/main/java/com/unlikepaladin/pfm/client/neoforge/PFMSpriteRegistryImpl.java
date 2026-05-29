package com.unlikepaladin.pfm.client.neoforge;

import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.ducks.PFMSpriteContentExtensions;
import com.unlikepaladin.pfm.mixin.PFMMissingSpriteAccessor;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.server.packs.resources.ResourceMetadata;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

public class PFMSpriteRegistryImpl {

    public static void registerSprite(ResourceLocation spriteId) {
    }

    public static void registerDynamicSprite(ResourceLocation spriteId, List<VariantBase<?>> variantBaseList) {
        PFMSpriteRegistry.DYNAMIC_SPRITE_GENERATORS.put(spriteId, (info) -> {
            List<Tuple<ResourceLocation, SpriteContents>> infos = new ArrayList<>();
            NativeImage image = PFMMissingSpriteAccessor.pfm$invokeCreateImage(info.width(), info.height());
            for (VariantBase<?> variantBase : variantBaseList) {
                ResourceLocation templateId = info.name();
                ResourceLocation variantSpriteId = ResourceLocation.fromNamespaceAndPath(templateId.getNamespace(), templateId.getPath().replace("template", variantBase.getPath()));
                SpriteContents variantInfo = new SpriteContents(variantSpriteId, new FrameSize(info.width(), info.height()),
                        image);
                ((PFMSpriteContentExtensions)variantInfo).pfm$setInitialized(false);
                infos.add(new Tuple<>(variantSpriteId, variantInfo));
            }
            return infos;
        });
    }
}
