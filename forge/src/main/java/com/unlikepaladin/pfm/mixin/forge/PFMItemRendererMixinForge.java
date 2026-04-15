package com.unlikepaladin.pfm.mixin.forge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.unlikepaladin.pfm.client.model.PFMBakedModelGetQuadsExtension;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.util.math.MatrixUtil;
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
    private static VertexConsumer getDynamicDisplayGlintConsumer(VertexConsumerProvider provider, RenderLayer layer, MatrixStack.Entry entry) {
        return null;
    }

    @Shadow
    private static void renderBakedItemQuads(MatrixStack matrices, VertexConsumer vertexConsumer, List<BakedQuad> quads, int[] tints, int light, int overlay) {
    }

    @Inject(method = "renderItem(Lnet/minecraft/item/ModelTransformationMode;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II[ILnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/item/ItemRenderState$Glint;)V", at = @At(value = "HEAD"), cancellable = true)
    private static void renderPFMItem(ModelTransformationMode transformationMode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, int[] tints, BakedModel model, RenderLayer layer, ItemRenderState.Glint glint, CallbackInfo ci) {
        if (model instanceof PFMBakedModelGetQuadsExtension) {
            VertexConsumer vertexconsumer;
            if (glint == ItemRenderState.Glint.SPECIAL) {
                MatrixStack.Entry posestack$pose = matrices.peek().copy();
                if (transformationMode == ModelTransformationMode.GUI) {
                    MatrixUtil.scale(posestack$pose.getPositionMatrix(), 0.5F);
                } else if (transformationMode.isFirstPerson()) {
                    MatrixUtil.scale(posestack$pose.getPositionMatrix(), 0.75F);
                }

                vertexconsumer = getDynamicDisplayGlintConsumer(vertexConsumers, layer, posestack$pose);
            } else {
                vertexconsumer = getItemGlintConsumer(vertexConsumers, layer, true, glint != ItemRenderState.Glint.NONE);
            }

            Random randomsource = Random.create();
            long seed = 42L;

            for (Direction direction : Direction.values()) {
                randomsource.setSeed(seed);
                renderBakedItemQuads(matrices, vertexconsumer, ((PFMBakedModelGetQuadsExtension) model).getQuadsCached(direction, randomsource), tints, light, overlay);
            }

            randomsource.setSeed(seed);
            renderBakedItemQuads(matrices, vertexconsumer, ((PFMBakedModelGetQuadsExtension) model).getQuadsCached(null, randomsource), tints, light, overlay);
            ci.cancel();
        }
    }
}