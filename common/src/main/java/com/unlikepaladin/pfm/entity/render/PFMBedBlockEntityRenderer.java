package com.unlikepaladin.pfm.entity.render;

import com.unlikepaladin.pfm.blocks.blockentities.PFMBedBlockEntity;
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
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.LightmapCoordinatesRetriever;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class PFMBedBlockEntityRenderer extends BlockEntityRenderer<PFMBedBlockEntity> {
    public PFMBedBlockEntityRenderer(BlockEntityRenderDispatcher ctx) {
        super(ctx);
        this.bedFoot = new ModelPart(64, 64, 2, 24);
        this.bedFoot.setPivot(8.0F, 24.0F, -8.0F);
        this.bedFoot.addCuboid(-16.0F, -9.0F, 8.0F, 16.0F, 13.0F, 4.0F, 0.0F, false);

        final ModelPart head_r1;
        final ModelPart pillow;
        final ModelPart head_r2;
        bedHead = new ModelPart(64, 64, 0 ,0);
        bedHead.setPivot(0.0F, 24.5F, 18.0F);
        setRotationAngle(bedHead, -1.5708F, 3.1416F, 0.0F);
        head_r1 = new ModelPart(64, 64, 0 ,0);
        head_r1.setPivot(-20.0F, 1.0F, -1.5F);
        bedHead.addChild(head_r1);
        setRotationAngle(head_r1, -1.5708F, 3.1416F, 0.0F);
        head_r1.setTextureOffset(2, 2).addCuboid(-28.0F, -5.0F, -3.0F, 16.0F, 13.0F, 4.0F, 0.0F, false);
        pillow = new ModelPart(64, 64, 0 ,0);
        pillow.setPivot(-20.0F, 2.0F, -1.5F);
        bedHead.addChild(pillow);
        head_r2 = new ModelPart(64, 64, 0 ,0);
        head_r2.setPivot(0.0F, 0.0F, 0.0F);
        pillow.addChild(head_r2);
        setRotationAngle(head_r2, -1.5708F, 3.1416F, 0.0F);
        head_r2.setTextureOffset(7, 5).addCuboid(-20.0F, -5.0F, -5.0F, 7.0F, 8.0F, 1.0F, 0.0F, true);
        head_r2.setTextureOffset(7, 5).addCuboid(-27.0F, -5.0F, -5.0F, 7.0F, 8.0F, 1.0F, 0.0F, false);
    }
    private final ModelPart bedHead;
    private final ModelPart bedFoot;

    @Override
    public void render(PFMBedBlockEntity bedBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        SpriteIdentifier spriteIdentifier = TexturedRenderLayers.BED_TEXTURES[bedBlockEntity.getColor().getId()];
        World world2 = bedBlockEntity.getWorld();
        if (world2 != null) {
            BlockState blockState = bedBlockEntity.getCachedState();
            DoubleBlockProperties.PropertySource<BedBlockEntity> propertySource = DoubleBlockProperties.toPropertySource(BlockEntityType.BED, BedBlock::getBedPart, BedBlock::getOppositePartDirection, ChestBlock.FACING, blockState, world2, bedBlockEntity.getPos(), (world, pos) -> false);
            int k = ((Int2IntFunction)propertySource.apply(new LightmapCoordinatesRetriever())).get(i);
            boolean isHead = blockState.get(BedBlock.PART) == BedPart.HEAD;
            if (isHead)
                matrixStack.translate(0, 1.0, 0.0);
            this.renderPart(matrixStack, vertexConsumerProvider, isHead ? this.bedHead : this.bedFoot, blockState.get(BedBlock.FACING), spriteIdentifier, k, j, false);
        } else {
            matrixStack.push();
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(270.0f));
            matrixStack.translate(0.0,1.0,-2.0);
            this.renderPart(matrixStack, vertexConsumerProvider, this.bedHead, Direction.NORTH, spriteIdentifier, i, j, false);
            matrixStack.translate(0, -1.0, 1.0);
            this.renderPart(matrixStack, vertexConsumerProvider, this.bedFoot, Direction.NORTH, spriteIdentifier, i, j, true);
            matrixStack.pop();
        }
    }

    private void renderPart(MatrixStack matrix, VertexConsumerProvider vertexConsumers, ModelPart part, Direction direction, SpriteIdentifier sprite, int light, int overlay, boolean isFoot) {
        matrix.push();
        matrix.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0f));

        matrix.translate(0.5, 0.5, 0.5);
        matrix.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0f + direction.asRotation()));
        matrix.translate(-0.5, -0.5, -0.5);

        matrix.translate(0.5, -0.9385, -0.5625);

        VertexConsumer vertexConsumer = sprite.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);
        part.render(matrix, vertexConsumer, light, overlay);
        matrix.pop();
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }
}
