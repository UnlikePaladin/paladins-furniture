package com.unlikepaladin.pfm.mixin.forge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.MatrixUtil;
import com.unlikepaladin.pfm.client.model.PFMBakedModelGetQuadsExtension;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ItemRenderer.class)
public abstract class PFMItemRendererMixinForge {

    @Shadow
    public static VertexConsumer getFoilBuffer(MultiBufferSource multiBufferSource, RenderType renderType, boolean bl, boolean bl2) {
        throw new AssertionError();
    }


    @Shadow
    protected static VertexConsumer getCompassFoilBuffer(MultiBufferSource arg, RenderType arg2, PoseStack.Pose arg3) {
        return null;
    }

    @Shadow
    public static void renderQuadList(PoseStack arg, VertexConsumer arg2, List<BakedQuad> list, int[] is, int j, int k) {
    }

    @Inject(method = "renderItem", at = @At(value = "HEAD"), cancellable = true)
    private static void renderPFMItem(ItemDisplayContext transformationMode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, int[] tints, BakedModel model, RenderType layer, ItemStackRenderState.FoilType glint, CallbackInfo ci) {
        if (model instanceof PFMBakedModelGetQuadsExtension) {
            VertexConsumer vertexconsumer;
            if (glint == ItemStackRenderState.FoilType.SPECIAL) {
                PoseStack.Pose posestack$pose = matrices.last().copy();
                if (transformationMode == ItemDisplayContext.GUI) {
                    MatrixUtil.mulComponentWise(posestack$pose.pose(), 0.5F);
                } else if (transformationMode.firstPerson()) {
                    MatrixUtil.mulComponentWise(posestack$pose.pose(), 0.75F);
                }

                vertexconsumer = getCompassFoilBuffer(vertexConsumers, layer, posestack$pose);
            } else {
                vertexconsumer = getFoilBuffer(vertexConsumers, layer, true, glint != ItemStackRenderState.FoilType.NONE);
            }

            RandomSource randomsource = RandomSource.create();
            long seed = 42L;

            for (Direction direction : Direction.values()) {
                randomsource.setSeed(seed);
                renderQuadList(matrices, vertexconsumer, ((PFMBakedModelGetQuadsExtension) model).getQuadsCached(direction, randomsource), tints, light, overlay);
            }

            randomsource.setSeed(seed);
            renderQuadList(matrices, vertexconsumer, ((PFMBakedModelGetQuadsExtension) model).getQuadsCached(null, randomsource), tints, light, overlay);
            ci.cancel();
        }
    }
}