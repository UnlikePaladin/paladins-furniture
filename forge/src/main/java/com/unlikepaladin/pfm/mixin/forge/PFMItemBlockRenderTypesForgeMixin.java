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
    public static boolean canRenderInLayer(BlockState state, RenderType type) {
        throw new AssertionError();
    }

    @Unique
    private static final Map<Pair<BlockState, RenderType>, Boolean> pfm$renderLayers = new HashMap<>();

    @Inject(method = "canRenderInLayer(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/client/renderer/RenderType;)Z", at = @At("TAIL"), cancellable = true, remap = false)
    private static void modifyFurnitureRenderLayer(BlockState state, RenderType type, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock().getDescriptionId().contains("pfm")) {
            Pair<BlockState, RenderType> renderLayerPair = new Pair<>(state, type);
            if (pfm$renderLayers.containsKey(renderLayerPair)) {
                cir.setReturnValue(pfm$renderLayers.get(renderLayerPair));
                return;
            }

            if (Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(state) instanceof AbstractBakedModel abstractBakedModel) {
                VariantBase<?> variant = abstractBakedModel.getVariant(state);
                if (variant != null) {
                    boolean doesBaseRender = canRenderInLayer(variant.getBaseBlock().defaultBlockState(), type);

                    if (type == RenderType.solid()) {
                        boolean cutoutRenders = canRenderInLayer(variant.getBaseBlock().defaultBlockState(), RenderType.cutout());
                        boolean translucentRenders = canRenderInLayer(variant.getBaseBlock().defaultBlockState(), RenderType.translucent());
                        boolean cutoutRendersMipped = canRenderInLayer(variant.getBaseBlock().defaultBlockState(), RenderType.cutoutMipped());

                        if (cutoutRenders || translucentRenders || cutoutRendersMipped) {
                            // Block solid if higher-priority layers can render
                            cir.setReturnValue(false);
                            pfm$renderLayers.put(renderLayerPair, false);
                        } else {
                            // Allow solid only if the base and current block both agree
                            cir.setReturnValue(doesBaseRender && cir.getReturnValue());
                            pfm$renderLayers.put(renderLayerPair, doesBaseRender && cir.getReturnValue());
                        }
                    } else {
                        // For cutout or translucent, prioritize the current block's renderability
                        cir.setReturnValue(doesBaseRender || cir.getReturnValue());
                        pfm$renderLayers.put(renderLayerPair, doesBaseRender || cir.getReturnValue());
                    }
                    return;
                }
            }
            if (state.getBlock() instanceof DynamicRenderLayerInterface) {
                RenderType renderLayer = ((DynamicRenderLayerInterface) state.getBlock()).getCustomRenderLayer();
                if (PaladinFurnitureMod.getPFMConfig().isShaderSolidFixOn())
                    cir.setReturnValue(PaladinFurnitureModClient.areShadersOn() ? type == RenderType.solid() : type == renderLayer);
                else
                    cir.setReturnValue(type == renderLayer);
            }
        }
    }
}
