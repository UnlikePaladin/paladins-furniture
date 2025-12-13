package com.unlikepaladin.pfm.client.forge;

import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.mixin.PFMSpriteInfoAccesor;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PFMSpriteRegistryImpl {
    static Queue<Identifier> REGISTER = new LinkedList<>();
    public static void registerSprite(Identifier spriteId) {
        REGISTER.add(spriteId);
    }

    @SubscribeEvent
    public static void register(TextureStitchEvent.Pre event) {
        if (event.getMap().getId() == PlayerScreenHandler.BLOCK_ATLAS_TEXTURE) {
            PFMSpriteRegistry.registerAdditionalSprites();
            while (!REGISTER.isEmpty()) {
                event.addSprite(REGISTER.poll());
            }
        }
    }

    public static void registerDynamicSprite(Identifier spriteId, List<VariantBase<?>> variantBaseList) {
        REGISTER.add(spriteId);
        PFMSpriteRegistry.DYNAMIC_SPRITE_GENERATORS.put(spriteId, (info) -> {
            List<Sprite.Info> infos = new LinkedList<>();
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
