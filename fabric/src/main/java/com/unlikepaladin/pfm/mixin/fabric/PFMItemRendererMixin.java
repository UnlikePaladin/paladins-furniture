package com.unlikepaladin.pfm.mixin.fabric;

import com.unlikepaladin.pfm.client.PFMBuiltinItemRendererExtension;
import com.unlikepaladin.pfm.client.fabric.PFMItemRendererFabric;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemRenderer.class, priority = 999)
public class PFMItemRendererMixin {

    @Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemTransforms$TransformType;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V", cancellable = true)
    private void renderWithItemRenderer(ItemStack stack, ItemDisplayContext renderMode, boolean leftHanded, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        matrices.pushPose();
        if (stack.getItem() instanceof PFMBuiltinItemRendererExtension || stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof PFMBuiltinItemRendererExtension) {
            PFMItemRendererFabric.INSTANCE.renderByItem(stack, renderMode, matrices, vertexConsumers, light, overlay);
        }
        matrices.popPose();
    }
}
