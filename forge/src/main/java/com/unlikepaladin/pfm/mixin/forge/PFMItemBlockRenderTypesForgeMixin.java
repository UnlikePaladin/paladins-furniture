package com.unlikepaladin.pfm.mixin.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.DynamicRenderLayerInterface;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.client.PaladinFurnitureModClient;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(ItemBlockRenderTypes.class)
public abstract class PFMItemBlockRenderTypesForgeMixin {


    @Shadow
    @Deprecated(
            forRemoval = true,
            since = "1.19"
    )
    public static Collection<ChunkSectionLayer> getRenderLayers(BlockState state) {
        throw new AssertionError();
    }

    @Unique
    private static final Map<BlockState, Collection<ChunkSectionLayer>> pfm$renderLayers = new HashMap<>();
    @Inject(method = "getRenderLayers", at = @At("TAIL"), cancellable = true, remap = false)
    private static void modifyFurnitureRenderLayer(BlockState state, CallbackInfoReturnable<Collection<ChunkSectionLayer>> cir) {
        if (state.getBlock().getDescriptionId().contains("pfm")) {
            if (pfm$renderLayers.containsKey(state)) {
                cir.setReturnValue(pfm$renderLayers.get(state));
                return;
            }

            if (Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(state) instanceof AbstractBakedModel abstractBakedModel) {
                VariantBase<?> variant = abstractBakedModel.getVariant(state);
                if (variant != null) {
                    Collection<ChunkSectionLayer> baseRenderTypes = getRenderLayers(variant.getBaseBlock().defaultBlockState());
                    Collection<ChunkSectionLayer> currentRenderTypes = cir.getReturnValue();

                    // Combine the render types using union
                    Collection<ChunkSectionLayer> combinedRenderTypes = new ArrayList<>(baseRenderTypes);
                    combinedRenderTypes.addAll(currentRenderTypes);

                    // Prioritize cutout and translucent over solid
                    if (combinedRenderTypes.contains(ChunkSectionLayer.CUTOUT) || combinedRenderTypes.contains(ChunkSectionLayer.TRANSLUCENT) || combinedRenderTypes.contains(ChunkSectionLayer.CUTOUT_MIPPED)) {
                        // Remove solid if higher-priority layers are present
                        combinedRenderTypes.remove(ChunkSectionLayer.SOLID);
                    }

                    // Update cir with the prioritized set
                    cir.setReturnValue(combinedRenderTypes);
                    pfm$renderLayers.put(state, combinedRenderTypes);
                    return;
                }
            }
            if (state.getBlock() instanceof DynamicRenderLayerInterface) {
                ChunkSectionLayer renderLayer = ((DynamicRenderLayerInterface) state.getBlock()).getCustomRenderLayer();
                if (PaladinFurnitureMod.getPFMConfig().isShaderSolidFixOn())
                    cir.setReturnValue(PaladinFurnitureModClient.areShadersOn() ? List.of(ChunkSectionLayer.SOLID) : List.of(renderLayer));
                else
                    cir.setReturnValue(List.of(renderLayer));
            }
        }
    }
}
