package com.unlikepaladin.pfm.mixin.forge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.unlikepaladin.pfm.client.forge.PFMBakedModelGetQuadsExtension;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ItemRenderer.class)
public abstract class PFMItemRendererMixinForge {

    @Shadow
    protected abstract void renderQuadList(PoseStack poseStack, VertexConsumer vertexConsumer, List<BakedQuad> list, ItemStack itemStack, int i, int j);

    @Shadow
    public static VertexConsumer getFoilBuffer(MultiBufferSource multiBufferSource, RenderType renderType, boolean bl, boolean bl2) {
        throw new AssertionError();
    }

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void renderPFMItem(ItemStack stack, ItemDisplayContext renderMode, boolean leftHanded, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        if (!stack.isEmpty() && model instanceof PFMBakedModelGetQuadsExtension) {
            matrices.pushPose();
            PoseStack.Pose pose = matrices.last();
            model = model.applyTransform(renderMode, matrices, leftHanded);
            matrices.translate(-0.5, -0.5, -0.5);

            BlockState state = stack.getItem() instanceof BlockItem ? ((BlockItem) stack.getItem()).getBlock().defaultBlockState() : null;
            RenderType renderLayer = ItemBlockRenderTypes.getRenderType(stack);
            VertexConsumer vertexConsumer = getFoilBuffer(vertexConsumers, renderLayer, true, stack.hasFoil());

            RandomSource random = RandomSource.create();
            long randomSeed = 42L;
            for (Direction direction : Direction.values()) {
                random.setSeed(randomSeed);
                this.renderQuadList(matrices, vertexConsumer, ((PFMBakedModelGetQuadsExtension) model).getQuadsCached(stack, state, direction, random), stack, light, overlay);
            }
            random.setSeed(randomSeed);
            this.renderQuadList(matrices, vertexConsumer, ((PFMBakedModelGetQuadsExtension)model).getQuadsCached(stack, state, null, random), stack, light, overlay);

            // Conditionally pop because of handlePerspective weirdness
            if (matrices.last() != pose) {
                matrices.popPose();
            }
            matrices.popPose();
            ci.cancel();
        }
    }
}