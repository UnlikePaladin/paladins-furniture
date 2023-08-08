package com.unlikepaladin.pfm.mixin.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.DynamicRenderLayerInterface;
import com.unlikepaladin.pfm.client.PaladinFurnitureModClient;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderLayers.class)
public class PFMRenderLayersForgeMixin {
    @Inject(method = "canRenderInLayer(Lnet/minecraft/block/BlockState;Lnet/minecraft/client/render/RenderLayer;)Z", at = @At("HEAD"), cancellable = true)
    private static void modifyFurnitureRenderLayer(BlockState state, RenderLayer type, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof DynamicRenderLayerInterface) {
            RenderLayer renderLayer = ((DynamicRenderLayerInterface) state.getBlock()).getCustomRenderLayer();
            if (PaladinFurnitureMod.getPFMConfig().isShaderSolidFixOn())
                cir.setReturnValue(PaladinFurnitureModClient.areShadersOn() ? type == RenderLayer.getSolid() : type == renderLayer);
            else
                cir.setReturnValue(type == renderLayer);
        }
    }
}
