package com.unlikepaladin.pfm.mixin.forge;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.DynamicRenderLayerInterface;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.client.PaladinFurnitureModClient;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraftforge.client.ChunkRenderTypeSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(ItemBlockRenderTypes.class)
public abstract class PFMItemBlockRenderTypesForgeMixin {


    @Shadow
    @Deprecated(
            forRemoval = true,
            since = "1.19"
    )
    public static ChunkRenderTypeSet getRenderLayers(BlockState state) {
        throw new AssertionError();
    }

    @Unique
    private static final Map<BlockState, ChunkRenderTypeSet> pfm$renderLayers = new HashMap<>();
    @Inject(method = "getRenderLayers", at = @At("TAIL"), cancellable = true, remap = false)
    private static void modifyFurnitureRenderLayer(BlockState state, CallbackInfoReturnable<ChunkRenderTypeSet> cir) {
        if (state.getBlock().getDescriptionId().contains("pfm")) {
            if (pfm$renderLayers.containsKey(state)) {
                cir.setReturnValue(pfm$renderLayers.get(state));
                return;
            }

            if (Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(state) instanceof AbstractBakedModel abstractBakedModel) {
                VariantBase<?> variant = abstractBakedModel.getVariant(state);
                if (variant != null) {
                    ChunkRenderTypeSet baseRenderTypes = getRenderLayers(variant.getBaseBlock().defaultBlockState());
                    ChunkRenderTypeSet currentRenderTypes = cir.getReturnValue();

                    // Combine the render types using union
                    ChunkRenderTypeSet combinedRenderTypes = ChunkRenderTypeSet.union(baseRenderTypes, currentRenderTypes);

                    // Prioritize cutout and translucent over solid
                    if (combinedRenderTypes.contains(RenderType.cutout()) || combinedRenderTypes.contains(RenderType.translucent()) || combinedRenderTypes.contains(RenderType.cutoutMipped())) {
                        // Remove solid if higher-priority layers are present
                        combinedRenderTypes = ChunkRenderTypeSet.intersection(combinedRenderTypes,
                                ChunkRenderTypeSet.of(RenderType.cutout(), RenderType.translucent(), RenderType.cutoutMipped())
                        );
                    }

                    // Update cir with the prioritized set
                    cir.setReturnValue(combinedRenderTypes);
                    pfm$renderLayers.put(state, combinedRenderTypes);
                    return;
                }
            }
            if (state.getBlock() instanceof DynamicRenderLayerInterface) {
                RenderType renderLayer = ((DynamicRenderLayerInterface) state.getBlock()).getCustomRenderLayer();
                if (PaladinFurnitureMod.getPFMConfig().isShaderSolidFixOn())
                    cir.setReturnValue(PaladinFurnitureModClient.areShadersOn() ? ChunkRenderTypeSet.of(RenderType.solid()) : ChunkRenderTypeSet.of(renderLayer));
                else
                    cir.setReturnValue(ChunkRenderTypeSet.of(renderLayer));
            }
        }
    }
}
