package com.unlikepaladin.pfm.client.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import com.unlikepaladin.pfm.blocks.blockentities.PFMBedBlockEntity;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.bed.UnbakedBedModel;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.entity.render.OfficeChairEntityRenderer;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.Arm;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;

import java.util.*;

public class PFMItemRendererForge extends BuiltinModelItemRenderer {
    public static PFMItemRendererForge INSTANCE = new PFMItemRendererForge(MinecraftClient.getInstance().getBlockEntityRenderDispatcher(), MinecraftClient.getInstance().getEntityModelLoader());

    private final PFMBedBlockEntity renderBed;
    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    public PFMItemRendererForge(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelLoader loader) {
        super(blockEntityRenderDispatcher, loader);
        this.blockEntityRenderDispatcher = blockEntityRenderDispatcher;
        if (PaladinFurnitureMod.furnitureEntryMap.get(SimpleBedBlock.class) != null ) {
            renderBed = new PFMBedBlockEntity(BlockPos.ORIGIN, PaladinFurnitureMod.furnitureEntryMap.get(SimpleBedBlock.class).getVariantToBlockMapList().get(WoodVariantRegistry.OAK).iterator().next().getDefaultState());
        } else {
            renderBed = null;
        }
    }

    public BakedModel getBedModel(boolean classic) {
        return classic ? UnbakedBedModel.inventoryModels.getRight() : UnbakedBedModel.inventoryModels.getLeft();

    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.pop();
        boolean leftHanded = MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.getMainArm() == Arm.LEFT && mode.isFirstPerson();

        boolean glint = stack.hasGlint();
        VertexConsumer consumer = ItemRenderer.getItemGlintConsumer(vertexConsumers, RenderLayers.getItemLayer(stack), true, glint);
        if (stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof SimpleBedBlock) {
            matrices.push();

            Block block = ((BlockItem) stack.getItem()).getBlock();
            BakedModel bedModel = getBedModel(stack.getItem().getTranslationKey().contains("classic"));
            bedModel.applyTransform(mode, matrices, leftHanded);
            matrices.translate(-.5, -.5, -.5); // Replicate ItemRenderer's translation

            // annoyingly, forge is applying some weird wrong transform to the bed model when rendering as an item
            // so i have to manually rotate and translate it to be correct, also happens only here on 1.21.1, how peculiar
            MinecraftClient.getInstance().getItemRenderer().renderBakedItemModel(bedModel, stack, light, overlay, matrices, consumer);

            this.renderBed.setColor(((SimpleBedBlock)block).getColor());
            this.blockEntityRenderDispatcher.renderEntity(renderBed, matrices, vertexConsumers, light, overlay);
            matrices.pop();
        } else if (stack.getItem() == PaladinFurnitureModBlocksItems.OFFICE_CHAIR_ITEM) {
            matrices.push();
            BakedModel chairModel = ModelHelper.getModelFromIdentifier(OfficeChairEntityRenderer.MODEL_IDS[0]);

            chairModel.applyTransform(mode, matrices, leftHanded);
            matrices.translate(-.5, -.5, -.5); // Replicate ItemRenderer's translation

            OfficeChairEntityRenderer.renderItem(stack, matrices, mode, vertexConsumers, leftHanded, light, overlay);
            matrices.pop();
        }
        matrices.push();
    }
}
