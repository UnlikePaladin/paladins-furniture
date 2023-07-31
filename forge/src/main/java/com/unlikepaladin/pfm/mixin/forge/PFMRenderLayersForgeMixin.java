package com.unlikepaladin.pfm.mixin.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.DynamicRenderLayerInterface;
import com.unlikepaladin.pfm.client.PaladinFurnitureModClient;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraftforge.client.ChunkRenderTypeSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderLayers.class)
public class PFMRenderLayersForgeMixin {
    @Inject(method = "getRenderLayers", at = @At("HEAD"), cancellable = true)
    private static void modifyFurnitureRenderLayer(BlockState state, CallbackInfoReturnable<ChunkRenderTypeSet> cir) {
        if (state.getBlock() instanceof DynamicRenderLayerInterface) {
            RenderLayer renderLayer = ((DynamicRenderLayerInterface) state.getBlock()).getCustomRenderLayer();
            if (PaladinFurnitureMod.getPFMConfig().isShaderSolidFixOn())
                cir.setReturnValue(PaladinFurnitureModClient.areShadersOn() ? ChunkRenderTypeSet.of(RenderLayer.getSolid()) : ChunkRenderTypeSet.of(renderLayer));
            else
                cir.setReturnValue(ChunkRenderTypeSet.of(renderLayer));
        }
    }
}
