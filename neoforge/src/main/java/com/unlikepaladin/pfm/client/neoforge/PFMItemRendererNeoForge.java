package com.unlikepaladin.pfm.client.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import com.unlikepaladin.pfm.blocks.blockentities.PFMBedBlockEntity;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.bed.UnbakedBedModel;
import com.unlikepaladin.pfm.client.PFMBakedModelManagerAccessor;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.entity.render.OfficeChairEntityRenderer;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.client.ClientHooks;

import java.util.HashMap;
import java.util.Map;

public class PFMItemRendererNeoForge extends BlockEntityWithoutLevelRenderer {
    public static PFMItemRendererNeoForge INSTANCE = new PFMItemRendererNeoForge(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());

    private final PFMBedBlockEntity renderBed;
    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    public PFMItemRendererNeoForge(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet loader) {
        super(blockEntityRenderDispatcher, loader);
        this.blockEntityRenderDispatcher = blockEntityRenderDispatcher;
        if (PaladinFurnitureMod.furnitureEntryMap.get(SimpleBedBlock.class) != null ) {
            renderBed = new PFMBedBlockEntity(BlockPos.ZERO, PaladinFurnitureMod.furnitureEntryMap.get(SimpleBedBlock.class).getVariantToBlockMapList().get(WoodVariantRegistry.OAK).iterator().next().defaultBlockState());
        } else {
            renderBed = null;
        }
    }

    public BakedModel getTransformBedModel(boolean classic) {
        return classic ? UnbakedBedModel.inventoryModels.getB() : UnbakedBedModel.inventoryModels.getA();
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        matrices.popPose();
        boolean leftHanded = Minecraft.getInstance().player != null && Minecraft.getInstance().player.getMainArm() == HumanoidArm.LEFT && mode.firstPerson();

        boolean glint = stack.hasFoil();
        VertexConsumer consumer = ItemRenderer.getFoilBuffer(vertexConsumers, ItemBlockRenderTypes.getRenderType(stack, true), true, glint);
        if (stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof SimpleBedBlock) {
            matrices.pushPose();

            Block block = ((BlockItem) stack.getItem()).getBlock();
            BakedModel bedModel = getTransformBedModel(stack.getItem().getDescriptionId().contains("classic"));
            ClientHooks.handleCameraTransforms(matrices, bedModel, mode, leftHanded);
            matrices.translate(-.5, -.5, -.5); // Replicate ItemRenderer's translation

            BakedModel actualModel = Minecraft.getInstance().getItemRenderer().getModel(stack, Minecraft.getInstance().level, Minecraft.getInstance().player, 0);
            for(BakedModel model : actualModel.getRenderPasses(stack, false)) {
                Minecraft.getInstance().getItemRenderer().renderModelLists(model, stack, light, overlay, matrices, consumer);
            }
            this.renderBed.setColor(((SimpleBedBlock)block).getColor());
            this.blockEntityRenderDispatcher.renderItem(renderBed, matrices, vertexConsumers, light, overlay);
            matrices.popPose();
        } else if (stack.getItem() == PaladinFurnitureModBlocksItems.OFFICE_CHAIR_ITEM) {
            matrices.pushPose();
            BakedModel chairModel = ModelHelper.getModelFromIdentifier(OfficeChairEntityRenderer.MODEL_IDS[0]);

            ClientHooks.handleCameraTransforms(matrices, chairModel, mode, leftHanded);
            matrices.translate(-.5, -.5, -.5); // Replicate ItemRenderer's translation

            OfficeChairEntityRenderer.renderItem(stack, matrices, mode, vertexConsumers, leftHanded, light, overlay);
            matrices.popPose();
        }
        matrices.pushPose();
    }
}
