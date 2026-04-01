package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import com.unlikepaladin.pfm.blocks.blockentities.PFMBedBlockEntity;
import com.unlikepaladin.pfm.blocks.models.bed.UnbakedBedModel;
import com.unlikepaladin.pfm.client.PFMBakedModelManagerAccessor;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.entity.render.OfficeChairEntityRenderer;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.core.BlockPos;

import java.util.*;

public class PFMItemRendererFabric extends BlockEntityWithoutLevelRenderer {
    public static PFMItemRendererFabric INSTANCE = new PFMItemRendererFabric(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());

    private final PFMBedBlockEntity renderBed;
    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    public PFMItemRendererFabric(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet loader) {
        super(blockEntityRenderDispatcher, loader);
        this.blockEntityRenderDispatcher = blockEntityRenderDispatcher;
        if (PaladinFurnitureMod.furnitureEntryMap.get(SimpleBedBlock.class) != null ) {
            renderBed = new PFMBedBlockEntity(BlockPos.ZERO, PaladinFurnitureMod.furnitureEntryMap.get(SimpleBedBlock.class).getVariantToBlockMapList().get(WoodVariantRegistry.OAK).iterator().next().defaultBlockState());
        } else {
            renderBed = null;
        }
    }

    static Map<Boolean, BakedModel> bedModel = new HashMap<>();
    public BakedModel getBedModelForTransform(boolean classic) {
        if (bedModel.containsKey(classic) && bedModel.get(classic) != null) {
            return bedModel.get(classic);
        }
        bedModel.put(classic, ((PFMBakedModelManagerAccessor) Minecraft.getInstance().getModelManager()).pfm$getModelFromNormalID(UnbakedBedModel.BED_MODEL_PARTS_BASE[classic ? 23 : 11]));
        return bedModel.get(classic);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        boolean leftHanded = Minecraft.getInstance().player != null && Minecraft.getInstance().player.getMainArm() == HumanoidArm.LEFT && mode.firstPerson();

        if (stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof SimpleBedBlock) {
            matrices.pushPose();

            Block block = ((BlockItem) stack.getItem()).getBlock();
            BakedModel bedModel = getBedModelForTransform(stack.getItem().getDescriptionId().contains("classic"));
            bedModel.getTransforms().getTransform(mode).apply(leftHanded, matrices);
            matrices.translate(-.5, -.5, -.5); // Replicate ItemRenderer's translation

            this.renderBed.setColor(((SimpleBedBlock)block).getColor());
            this.blockEntityRenderDispatcher.renderItem(renderBed, matrices, vertexConsumers, light, overlay);
            matrices.popPose();
        } else if (stack.getItem() == PaladinFurnitureModBlocksItems.OFFICE_CHAIR_ITEM) {
            matrices.pushPose();
            BakedModel chairModel = ((PFMBakedModelManagerAccessor) Minecraft.getInstance().getModelManager())
                    .pfm$getModelFromNormalID(OfficeChairEntityRenderer.MODEL_IDS[0]);

            chairModel.getTransforms().getTransform(mode).apply(
                    mode == ItemDisplayContext.FIRST_PERSON_LEFT_HAND ||
                            mode == ItemDisplayContext.THIRD_PERSON_LEFT_HAND, matrices);

            matrices.translate(-.5, -.5, -.5); // Replicate ItemRenderer's translation

            OfficeChairEntityRenderer.renderItem(stack, matrices, mode, vertexConsumers, leftHanded, light, overlay);
            matrices.popPose();
        }
    }
}
