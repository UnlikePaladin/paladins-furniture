package com.unlikepaladin.pfm.entity.render;

import com.unlikepaladin.pfm.blocks.blockentities.PFMBedBlockEntity;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
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
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.resources.model.Material;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.DyeColor;
import net.minecraft.core.Direction;
import com.mojang.math.Vector3f;
import net.minecraft.world.level.Level;

public class PFMBedBlockEntityRenderer extends BlockEntityRenderer<PFMBedBlockEntity> {
    public PFMBedBlockEntityRenderer(BlockEntityRenderDispatcher ctx) {
        super(ctx);
        this.bedFoot = new ModelPart(64, 64, 2, 24);
        this.bedFoot.setPos(8.0F, 24.0F, -8.0F);
        this.bedFoot.addBox(-16.0F, -9.0F, 8.0F, 16.0F, 13.0F, 4.0F, 0.0F, false);

        final ModelPart head_r1;
        final ModelPart pillow;
        final ModelPart head_r2;
        bedHead = new ModelPart(64, 64, 0 ,0);
        bedHead.setPos(0.0F, 24.5F, 18.0F);
        setRotationAngle(bedHead, -1.5708F, 3.1416F, 0.0F);
        head_r1 = new ModelPart(64, 64, 0 ,0);
        head_r1.setPos(-20.0F, 1.0F, -1.5F);
        bedHead.addChild(head_r1);
        setRotationAngle(head_r1, -1.5708F, 3.1416F, 0.0F);
        head_r1.texOffs(2, 2).addBox(-28.0F, -5.0F, -3.0F, 16.0F, 13.0F, 4.0F, 0.0F, false);
        pillow = new ModelPart(64, 64, 0 ,0);
        pillow.setPos(-20.0F, 2.0F, -1.5F);
        bedHead.addChild(pillow);
        head_r2 = new ModelPart(64, 64, 0 ,0);
        head_r2.setPos(0.0F, 0.0F, 0.0F);
        pillow.addChild(head_r2);
        setRotationAngle(head_r2, -1.5708F, 3.1416F, 0.0F);
        head_r2.texOffs(7, 5).addBox(-20.0F, -5.0F, -5.0F, 7.0F, 8.0F, 1.0F, 0.0F, true);
        head_r2.texOffs(7, 5).addBox(-27.0F, -5.0F, -5.0F, 7.0F, 8.0F, 1.0F, 0.0F, false);
    }
    private final ModelPart bedHead;
    private final ModelPart bedFoot;

    @Override
    public void render(PFMBedBlockEntity bedBlockEntity, float f, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, int j) {
        Material spriteIdentifier = bedBlockEntity != null ? Sheets.BED_TEXTURES[bedBlockEntity.getColor().getId()] : Sheets.BED_TEXTURES[DyeColor.WHITE.getId()];
        Level world2 = bedBlockEntity != null ? bedBlockEntity.getLevel() :  null;
        if (world2 != null) {
            BlockState blockState = bedBlockEntity.getBlockState();
            DoubleBlockCombiner.NeighborCombineResult<BedBlockEntity> propertySource = DoubleBlockCombiner.combineWithNeigbour(BlockEntityType.BED, BedBlock::getBlockType, BedBlock::getConnectedDirection, ChestBlock.FACING, blockState, world2, bedBlockEntity.getBlockPos(), (world, pos) -> false);
            int k = ((Int2IntFunction)propertySource.apply(new BrightnessCombiner())).get(i);
            boolean isHead = blockState.getValue(BedBlock.PART) == BedPart.HEAD;
            if (isHead)
                matrixStack.translate(0, 1.0, 0.0);
            this.renderPart(matrixStack, vertexConsumerProvider, isHead ? this.bedHead : this.bedFoot, blockState.getValue(BedBlock.FACING), spriteIdentifier, k, j, false);
        } else {
            matrixStack.pushPose();
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(270.0f));
            matrixStack.translate(0.0,1.0,-2.0);
            this.renderPart(matrixStack, vertexConsumerProvider, this.bedHead, Direction.NORTH, spriteIdentifier, i, j, false);
            matrixStack.translate(0, -1.0, 1.0);
            this.renderPart(matrixStack, vertexConsumerProvider, this.bedFoot, Direction.NORTH, spriteIdentifier, i, j, true);
            matrixStack.popPose();
        }
    }

    private void renderPart(PoseStack matrix, MultiBufferSource vertexConsumers, ModelPart part, Direction direction, Material sprite, int light, int overlay, boolean isFoot) {
        matrix.pushPose();
        matrix.mulPose(Vector3f.XP.rotationDegrees(90.0f));

        matrix.translate(0.5, 0.5, 0.5);
        matrix.mulPose(Vector3f.ZP.rotationDegrees(180.0f + direction.toYRot()));
        matrix.translate(-0.5, -0.5, -0.5);

        matrix.translate(0.5, -0.9385, -0.5625);

        VertexConsumer vertexConsumer = sprite.buffer(vertexConsumers, RenderType::entitySolid);
        part.render(matrix, vertexConsumer, light, overlay);
        matrix.popPose();
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.xRot = x;
        bone.yRot = y;
        bone.zRot = z;
    }
}
