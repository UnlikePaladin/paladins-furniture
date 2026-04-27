package com.unlikepaladin.pfm.client.forge;

import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import com.unlikepaladin.pfm.blocks.blockentities.PFMBedBlockEntity;
import com.unlikepaladin.pfm.blocks.models.bed.UnbakedBedModel;
import com.unlikepaladin.pfm.client.PFMBakedModelManagerAccessor;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.entity.render.OfficeChairEntityRenderer;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.core.BlockPos;
import net.minecraftforge.client.ForgeHooksClient;

import java.util.HashMap;
import java.util.Map;

public class PFMItemRendererForge extends BlockEntityWithoutLevelRenderer {

    private final PFMBedBlockEntity renderBed = new PFMBedBlockEntity();
    public PFMItemRendererForge() {
        super();
    }

    static Map<Boolean, BakedModel> bedModel = new HashMap<>();
    public BakedModel getBedModelForTransform(boolean classic) {
        if (bedModel.containsKey(classic) && bedModel.get(classic) != null) {
            return bedModel.get(classic);
        }
        bedModel.put(classic, ((PFMBakedModelManagerAccessor) Minecraft.getInstance().getModelManager()).pfm$getModelFromNormalID(UnbakedBedModel.BED_MODEL_PARTS_BASE[classic ? 23 : 11]));
        return ((PFMBakedModelManagerAccessor) Minecraft.getInstance().getModelManager()).pfm$getModelFromNormalID(UnbakedBedModel.BED_MODEL_ID);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        matrices.popPose();
        boolean leftHanded = Minecraft.getInstance().player != null && Minecraft.getInstance().player.getMainArm() == HumanoidArm.LEFT && mode.firstPerson();

        boolean glint = stack.hasFoil();
        VertexConsumer consumer = ItemRenderer.getFoilBuffer(vertexConsumers, ItemBlockRenderTypes.getRenderType(stack, true), true, glint);
        if (stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof SimpleBedBlock) {
            matrices.pushPose();

            Block block = ((BlockItem) stack.getItem()).getBlock();
            BakedModel modelForTransform = getBedModelForTransform(stack.getItem().getDescriptionId().contains("classic"));
            ForgeHooksClient.handleCameraTransforms(matrices, modelForTransform, mode, leftHanded);
            matrices.translate(-.5, -.5, -.5); // Replicate ItemRenderer's translation

            BakedModel actualModel = ((PFMBakedModelManagerAccessor) Minecraft.getInstance().getModelManager()).pfm$getModelFromNormalID(UnbakedBedModel.BED_MODEL_ID);
            ForgeHooksClient.drawItemLayered(Minecraft.getInstance().getItemRenderer(), actualModel, stack, matrices, vertexConsumers, light, overlay, false);

            this.renderBed.setColor(((SimpleBedBlock)block).getColor());
            BlockEntityRenderDispatcher.INSTANCE.renderItem(renderBed, matrices, vertexConsumers, light, overlay);
            matrices.popPose();
        } else if (stack.getItem() == PaladinFurnitureModBlocksItems.OFFICE_CHAIR_ITEM) {
            matrices.pushPose();
            BakedModel chairModel = ((PFMBakedModelManagerAccessor) Minecraft.getInstance().getModelManager())
                    .pfm$getModelFromNormalID(OfficeChairEntityRenderer.MODEL_IDS[0]);

            ForgeHooksClient.handleCameraTransforms(matrices, chairModel, mode, leftHanded);
            matrices.translate(-.5, -.5, -.5); // Replicate ItemRenderer's translation

            OfficeChairEntityRenderer.renderItem(stack, matrices, mode, vertexConsumers, leftHanded, light, overlay);
            matrices.popPose();
        }
        matrices.pushPose();
    }
}
