package com.unlikepaladin.pfm.mixin.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.DynamicRenderLayerInterface;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.client.PaladinFurnitureModClient;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.BlockRenderLayers;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(BlockRenderLayers.class)
public abstract class PFMRenderLayersNeoForgeMixin {
    @Shadow
    @Deprecated
    public static BlockRenderLayer getBlockLayer(BlockState state) {
        throw new AssertionError();
    }

    @Unique
    private static final Map<BlockState, BlockRenderLayer> pfm$renderLayers = new HashMap<>();
    @Inject(method = "getBlockLayer", at = @At("TAIL"), cancellable = true)
    private static void modifyFurnitureRenderLayer(BlockState state, CallbackInfoReturnable<BlockRenderLayer> cir) {
        if (state.getBlock() instanceof DynamicRenderLayerInterface) {
            BlockRenderLayer renderLayer = ((DynamicRenderLayerInterface) state.getBlock()).getCustomRenderLayer();
            if (PaladinFurnitureMod.getPFMConfig().isShaderSolidFixOn())
                cir.setReturnValue(PaladinFurnitureModClient.areShadersOn() ? BlockRenderLayer.SOLID : renderLayer);
            else
                cir.setReturnValue(renderLayer);
        }

        if (state.getBlock().getTranslationKey().contains("pfm")) {
            if (pfm$renderLayers.containsKey(state)) {
                cir.setReturnValue(pfm$renderLayers.get(state));
                return;
            }
            if (MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(state) instanceof AbstractBakedModel abstractBakedModel) {
                VariantBase<?> variant = abstractBakedModel.getVariant(state);
                if (variant != null) {
                    BlockRenderLayer parentLayer = getBlockLayer(variant.getBaseBlock().getDefaultState());
                    BlockRenderLayer selfLayer = cir.getReturnValue();

                    if (parentLayer != BlockRenderLayer.SOLID) {
                        cir.setReturnValue(parentLayer);
                        pfm$renderLayers.put(state, parentLayer);
                    } else if (selfLayer != BlockRenderLayer.SOLID) {
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
