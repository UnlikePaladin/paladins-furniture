package com.unlikepaladin.pfm.entity.render;

import com.mojang.math.Axis;
import com.unlikepaladin.pfm.blocks.blockentities.PFMBedBlockEntity;
import com.unlikepaladin.pfm.blocks.models.bed.UnbakedBedModel;
import com.unlikepaladin.pfm.client.EntityRenderIDs;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.resources.model.Material;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.DyeColor;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class PFMBedBlockEntityRenderer implements BlockEntityRenderer<PFMBedBlockEntity> {
    public PFMBedBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.bedHead = ctx.bakeLayer(EntityRenderIDs.BED_HEAD_LAYER);
        this.bedFoot = ctx.bakeLayer(EntityRenderIDs.BED_FOOT_LAYER);
    }
    private final ModelPart bedHead;
    private final ModelPart bedFoot;

    public static LayerDefinition getFootTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition group = modelPartData.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(8.0F, 24.0F, -8.0F));

        PartDefinition foot_r1 = group.addOrReplaceChild("foot_r1", CubeListBuilder.create().texOffs(2, 24).addBox(-8.0F, -11.0F, -8.0F, 16.0F, 13.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -1.0F, 11.0F, -1.5708F, 3.1416F, 0.0F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    public static LayerDefinition getHeadTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition group = modelPartData.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(8.0F, 24.0F, -8.0F));

        PartDefinition head_r1 = group.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(2, 2).addBox(-28.0F, -5.0F, -3.0F, 16.0F, 13.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-28.0F, -6.0F, 8.0F, -1.5708F, 3.1416F, 0.0F));

        PartDefinition pillow = group.addOrReplaceChild("pillow", CubeListBuilder.create(), PartPose.offset(-28.0F, -5.0F, 8.0F));

        PartDefinition head_r2 = pillow.addOrReplaceChild("head_r2", CubeListBuilder.create().texOffs(7, 5).mirror().addBox(-20.0F, -5.0F, -5.0F, 7.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(7, 5).addBox(-27.0F, -5.0F, -5.0F, 7.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 3.1416F, 0.0F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public void render(PFMBedBlockEntity bedBlockEntity, float f, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, int j) {
        Material spriteIdentifier = bedBlockEntity != null ? Sheets.BED_TEXTURES[bedBlockEntity.getColor().getId()] : Sheets.BED_TEXTURES[DyeColor.WHITE.getId()];
        Level world2 = bedBlockEntity != null ? bedBlockEntity.getLevel() :  null;
        if (world2 != null) {
            BlockState blockState = bedBlockEntity.getBlockState();
            DoubleBlockCombiner.NeighborCombineResult<BedBlockEntity> propertySource = DoubleBlockCombiner.combineWithNeigbour(BlockEntityType.BED, BedBlock::getBlockType, BedBlock::getConnectedDirection, ChestBlock.FACING, blockState, world2, bedBlockEntity.getBlockPos(), (world, pos) -> false);
            int k = ((Int2IntFunction)propertySource.apply(new BrightnessCombiner())).get(i);
            this.renderPart(matrixStack, vertexConsumerProvider, blockState.getValue(BedBlock.PART) == BedPart.HEAD ? this.bedHead : this.bedFoot, blockState.getValue(BedBlock.FACING), spriteIdentifier, k, j, false);
        } else {
            matrixStack.pushPose();
            matrixStack.mulPose(Axis.YP.rotationDegrees(270.0f));
            matrixStack.translate(0.0,0,-2.0);
            this.renderPart(matrixStack, vertexConsumerProvider, this.bedHead, Direction.NORTH, spriteIdentifier, i, j, false);
            matrixStack.translate(0.0,0,1.0);
            this.renderPart(matrixStack, vertexConsumerProvider, this.bedFoot, Direction.NORTH, spriteIdentifier, i, j, true);
            matrixStack.popPose();
        }
    }

    private void renderPart(PoseStack matrix, MultiBufferSource vertexConsumers, ModelPart part, Direction direction, Material sprite, int light, int overlay, boolean isFoot) {
        matrix.pushPose();
        matrix.mulPose(Axis.XP.rotationDegrees(180.0f));

        matrix.translate(0.5, -1.5, 0);
        matrix.mulPose(Axis.YP.rotationDegrees(direction.toYRot()));
        switch (direction) {
            case EAST: matrix.translate(-0.5, 0, 0); break;
            case WEST: matrix.translate(0.5, 0, 0); break;
            case NORTH: matrix.translate(0, 0, 0.5); break;
            case SOUTH: matrix.translate(0, 0, -0.5); break;
        }

        VertexConsumer vertexConsumer = sprite.buffer(vertexConsumers, RenderType::entitySolid);
        part.render(matrix, vertexConsumer, light, overlay);
        matrix.popPose();
    }

}
