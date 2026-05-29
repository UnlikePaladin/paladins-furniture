package com.unlikepaladin.pfm.mixin.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.DynamicRenderLayerInterface;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.client.PaladinFurnitureModClient;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.render.BlockRenderLayers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(ItemBlockRenderTypes.class)
public abstract class PFMRenderLayersNeoForgeMixin {

    @Shadow
    @Deprecated
    public static ChunkSectionLayer getChunkRenderType(BlockState arg) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Unique
    private static final Map<BlockState, ChunkSectionLayer> pfm$renderLayers = new HashMap<>();
    @Inject(method = "getChunkRenderType", at = @At("TAIL"), cancellable = true)
    private static void modifyFurnitureRenderLayer(BlockState state, CallbackInfoReturnable<ChunkSectionLayer> cir) {
        if (state.getBlock() instanceof DynamicRenderLayerInterface) {
            ChunkSectionLayer renderLayer = ((DynamicRenderLayerInterface) state.getBlock()).getCustomRenderLayer();
            if (PaladinFurnitureMod.getPFMConfig().isShaderSolidFixOn())
                cir.setReturnValue(PaladinFurnitureModClient.areShadersOn() ? ChunkSectionLayer.SOLID : renderLayer);
            else
                cir.setReturnValue(renderLayer);
        }

        if (state.getBlock().getDescriptionId().contains("pfm")) {
            if (pfm$renderLayers.containsKey(state)) {
                cir.setReturnValue(pfm$renderLayers.get(state));
                return;
            }
            if (Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(state) instanceof AbstractBakedModel abstractBakedModel) {
                VariantBase<?> variant = abstractBakedModel.getVariant(state);
                if (variant != null) {
                    ChunkSectionLayer parentLayer = getChunkRenderType(variant.getBaseBlock().defaultBlockState());
                    ChunkSectionLayer selfLayer = cir.getReturnValue();

                    if (parentLayer != ChunkSectionLayer.SOLID) {
                        cir.setReturnValue(parentLayer);
                        pfm$renderLayers.put(state, parentLayer);
                    } else if (selfLayer != ChunkSectionLayer.SOLID) {
                        cir.setReturnValue(selfLayer);
                        pfm$renderLayers.put(state, selfLayer);
                    } else {
                        // Both are solid, keep solid.
                        pfm$renderLayers.put(state, selfLayer);
                    }
                }
            }
        }
    }
}
