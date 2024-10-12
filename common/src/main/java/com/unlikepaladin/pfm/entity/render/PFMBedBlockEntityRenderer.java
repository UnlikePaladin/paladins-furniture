package com.unlikepaladin.pfm.entity.render;

import com.unlikepaladin.pfm.blocks.blockentities.PFMBedBlockEntity;
import com.unlikepaladin.pfm.blocks.models.bed.UnbakedBedModel;
import com.unlikepaladin.pfm.client.EntityRenderIDs;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.entity.BedBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.BedPart;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BedBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.LightmapCoordinatesRetriever;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;

public class PFMBedBlockEntityRenderer implements BlockEntityRenderer<PFMBedBlockEntity> {
    public PFMBedBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.bedHead = ctx.getLayerModelPart(EntityRenderIDs.BED_HEAD_LAYER);
        this.bedFoot = ctx.getLayerModelPart(EntityRenderIDs.BED_FOOT_LAYER);
    }
    private final ModelPart bedHead;
    private final ModelPart bedFoot;

    public static TexturedModelData getFootTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData group = modelPartData.addChild("main", ModelPartBuilder.create(), ModelTransform.pivot(8.0F, 24.0F, -8.0F));

        ModelPartData foot_r1 = group.addChild("foot_r1", ModelPartBuilder.create().uv(2, 24).cuboid(-8.0F, -11.0F, -8.0F, 16.0F, 13.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-8.0F, -1.0F, 11.0F, -1.5708F, 3.1416F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    public static TexturedModelData getHeadTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData group = modelPartData.addChild("main", ModelPartBuilder.create(), ModelTransform.pivot(8.0F, 24.0F, -8.0F));

        ModelPartData head_r1 = group.addChild("head_r1", ModelPartBuilder.create().uv(2, 2).cuboid(-28.0F, -5.0F, -3.0F, 16.0F, 13.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-28.0F, -6.0F, 8.0F, -1.5708F, 3.1416F, 0.0F));

        ModelPartData pillow = group.addChild("pillow", ModelPartBuilder.create(), ModelTransform.pivot(-28.0F, -5.0F, 8.0F));

        ModelPartData head_r2 = pillow.addChild("head_r2", ModelPartBuilder.create().uv(7, 5).mirrored().cuboid(-20.0F, -5.0F, -5.0F, 7.0F, 8.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 5).cuboid(-27.0F, -5.0F, -5.0F, 7.0F, 8.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -1.5708F, 3.1416F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void render(PFMBedBlockEntity bedBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        SpriteIdentifier spriteIdentifier = TexturedRenderLayers.BED_TEXTURES[bedBlockEntity.getColor().getId()];
        World world2 = bedBlockEntity.getWorld();
        if (world2 != null) {
            BlockState blockState = bedBlockEntity.getCachedState();
            DoubleBlockProperties.PropertySource<BedBlockEntity> propertySource = DoubleBlockProperties.toPropertySource(BlockEntityType.BED, BedBlock::getBedPart, BedBlock::getOppositePartDirection, ChestBlock.FACING, blockState, world2, bedBlockEntity.getPos(), (world, pos) -> false);
            int k = ((Int2IntFunction)propertySource.apply(new LightmapCoordinatesRetriever())).get(i);
            this.renderPart(matrixStack, vertexConsumerProvider, blockState.get(BedBlock.PART) == BedPart.HEAD ? this.bedHead : this.bedFoot, blockState.get(BedBlock.FACING), spriteIdentifier, k, j, false);
        } else {
            matrixStack.push();
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(270.0f));
            matrixStack.translate(0.0,0,-2.0);
            this.renderPart(matrixStack, vertexConsumerProvider, this.bedHead, Direction.NORTH, spriteIdentifier, i, j, false);
            matrixStack.translate(0.0,0,1.0);
            this.renderPart(matrixStack, vertexConsumerProvider, this.bedFoot, Direction.NORTH, spriteIdentifier, i, j, true);
            matrixStack.pop();
        }
    }

    private void renderPart(MatrixStack matrix, VertexConsumerProvider vertexConsumers, ModelPart part, Direction direction, SpriteIdentifier sprite, int light, int overlay, boolean isFoot) {
        matrix.push();
        matrix.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0f));

        matrix.translate(0.5, -1.5, 0);
        matrix.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction.asRotation()));
        switch (direction) {
            case EAST: matrix.translate(-0.5, 0, 0); break;
            case WEST: matrix.translate(0.5, 0, 0); break;
            case NORTH: matrix.translate(0, 0, 0.5); break;
            case SOUTH: matrix.translate(0, 0, -0.5); break;
        }

        VertexConsumer vertexConsumer = sprite.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);
        part.render(matrix, vertexConsumer, light, overlay);
        matrix.pop();
    }

}
