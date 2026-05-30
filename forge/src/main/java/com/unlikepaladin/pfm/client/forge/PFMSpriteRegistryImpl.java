package com.unlikepaladin.pfm.client.forge;

import com.mojang.blaze3d.platform.NativeImage;
import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.ducks.PFMSpriteContentExtensions;
import com.unlikepaladin.pfm.mixin.PFMMissingSpriteAccessor;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.server.packs.resources.ResourceMetadata;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.util.Tuple;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public class PFMSpriteRegistryImpl {

    public static void registerSprite(Identifier spriteId) {
    }

    public static void registerDynamicSprite(Identifier spriteId, List<VariantBase<?>> variantBaseList) {
        PFMSpriteRegistry.DYNAMIC_SPRITE_GENERATORS.put(spriteId, (info) -> {
            List<Tuple<Identifier, SpriteContents>> infos = new ArrayList<>();
            NativeImage image = PFMMissingSpriteAccessor.pfm$invokeCreateImage(info.width(), info.height());
            for (VariantBase<?> variantBase : variantBaseList) {
                Identifier templateId = info.name();
                Identifier variantSpriteId = Identifier.fromNamespaceAndPath(templateId.getNamespace(), templateId.getPath().replace("template", variantBase.getPath()));
                SpriteContents variantInfo = new SpriteContents(variantSpriteId, new FrameSize(info.width(), info.height()),
                        image);
                ((PFMSpriteContentExtensions)variantInfo).pfm$setInitialized(false);
                infos.add(new Tuple<>(variantSpriteId, variantInfo));
            }
            return infos;
        });
    }
}
