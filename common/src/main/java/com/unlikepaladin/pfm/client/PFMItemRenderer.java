package com.unlikepaladin.pfm.client;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import com.unlikepaladin.pfm.blocks.blockentities.PFMBedBlockEntity;
import com.unlikepaladin.pfm.blocks.models.bed.UnbakedBedModel;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Direction;

import java.util.*;

public class PFMItemRenderer {
    /*private final PFMBedBlockEntity renderBed;
    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    public PFMItemRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
        this.blockEntityRenderDispatcher = blockEntityRenderDispatcher;
        if (PaladinFurnitureMod.furnitureEntryMap.get(SimpleBedBlock.class) != null ) {
            renderBed = new PFMBedBlockEntity(BlockPos.ORIGIN, PaladinFurnitureMod.furnitureEntryMap.get(SimpleBedBlock.class).getVariantToBlockMapList().get(WoodVariantRegistry.OAK).iterator().next().getDefaultState());
        } else {
            renderBed = null;
        }
    }

    public static Map<Boolean, BlockModelPart> bedModel = new HashMap<>();
    public BlockModelPart getBedModel(boolean classic) {
        if (bedModel.containsKey(classic) && bedModel.get(classic) != null) {
            return bedModel.get(classic);
        }
        bedModel.put(classic, classic ? UnbakedBedModel.inventoryModels.getRight() : UnbakedBedModel.inventoryModels.getLeft());
        return bedModel.get(classic);
    }

    public void render(ItemStack stack, ItemDisplayContext mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos) {
        boolean leftHanded = MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.getMainArm() == Arm.LEFT && mode.isFirstPerson();

        if (stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof SimpleBedBlock) {
            matrices.push();

            Block block = ((BlockItem) stack.getItem()).getBlock();
//            BlockModelPart bedModel = getBedModel(stack.getItem().getTranslationKey().contains("classic"));


            this.renderBed.setPFMColor(((SimpleBedBlock)block).getColor());
            BlockEntityRenderer<PFMBedBlockEntity> blockEntityRenderer = blockEntityRenderDispatcher.get(renderBed);
            blockEntityRenderer.render(renderBed, 1.0f, matrices, vertexConsumers, light, overlay, cameraPos);
            matrices.pop();
        }
    }*/
}
