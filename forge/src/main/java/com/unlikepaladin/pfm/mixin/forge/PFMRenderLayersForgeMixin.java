package com.unlikepaladin.pfm.mixin.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.DynamicRenderLayerInterface;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.client.PaladinFurnitureModClient;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.RenderLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(RenderLayers.class)
public abstract class PFMRenderLayersForgeMixin {

    @Shadow
    @Deprecated(
            forRemoval = true,
            since = "1.19"
    )
    public static Collection<BlockRenderLayer> getRenderLayers(BlockState state) {
        throw new AssertionError();
    }

    @Unique
    private static final Map<BlockState, Collection<BlockRenderLayer>> pfm$renderLayers = new HashMap<>();
    @Inject(method = "getRenderLayers", at = @At("TAIL"), cancellable = true)
    private static void modifyFurnitureRenderLayer(BlockState state, CallbackInfoReturnable<Collection<BlockRenderLayer>> cir) {
        if (state.getBlock().getTranslationKey().contains("pfm")) {
            if (pfm$renderLayers.containsKey(state)) {
                cir.setReturnValue(pfm$renderLayers.get(state));
                return;
            }

            if (MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(state) instanceof AbstractBakedModel abstractBakedModel) {
                VariantBase<?> variant = abstractBakedModel.getVariant(state);
                if (variant != null) {
                    Collection<BlockRenderLayer> baseRenderTypes = getRenderLayers(variant.getBaseBlock().getDefaultState());
                    Collection<BlockRenderLayer> currentRenderTypes = cir.getReturnValue();

                    // Combine the render types using union
                    Collection<BlockRenderLayer> combinedRenderTypes = new ArrayList<>(baseRenderTypes);
                    combinedRenderTypes.addAll(currentRenderTypes);

                    // Prioritize cutout and translucent over solid
                    if (combinedRenderTypes.contains(BlockRenderLayer.CUTOUT) || combinedRenderTypes.contains(BlockRenderLayer.TRANSLUCENT) || combinedRenderTypes.contains(BlockRenderLayer.CUTOUT_MIPPED)) {
                        // Remove solid if higher-priority layers are present
                        combinedRenderTypes.remove(BlockRenderLayer.SOLID);
                    }

                    // Update cir with the prioritized set
                    cir.setReturnValue(combinedRenderTypes);
                    pfm$renderLayers.put(state, combinedRenderTypes);
                    return;
                }
            }
            if (state.getBlock() instanceof DynamicRenderLayerInterface) {
                BlockRenderLayer renderLayer = ((DynamicRenderLayerInterface) state.getBlock()).getCustomRenderLayer();
                if (PaladinFurnitureMod.getPFMConfig().isShaderSolidFixOn())
                    cir.setReturnValue(PaladinFurnitureModClient.areShadersOn() ? List.of(BlockRenderLayer.SOLID) : List.of(renderLayer));
                else
                    cir.setReturnValue(List.of(renderLayer));
            }
        }
    }
}
