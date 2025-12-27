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
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.LightmapCoordinatesRetriever;
import net.minecraft.client.render.block.entity.state.BedBlockEntityRenderState;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.command.RenderCommandQueue;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.SpriteHolder;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Unit;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Set;

public class PFMBedBlockEntityRenderer implements BlockEntityRenderer<PFMBedBlockEntity, BedBlockEntityRenderState> {
    private final SpriteHolder materials;
    public PFMBedBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this(ctx.spriteHolder(), ctx.loadedEntityModels());
    }

    public PFMBedBlockEntityRenderer(SpecialModelRenderer.BakeContext ctx) {
        this(ctx.spriteHolder(), ctx.entityModelSet());
    }

    public PFMBedBlockEntityRenderer(SpriteHolder materials, LoadedEntityModels models) {
        this.bedHead = new Model.SinglePartModel(models.getModelPart(EntityRenderIDs.BED_HEAD_LAYER), RenderLayer::getEntitySolid);
        this.bedFoot = new Model.SinglePartModel(models.getModelPart(EntityRenderIDs.BED_FOOT_LAYER), RenderLayer::getEntitySolid);
        this.materials = materials;
    }

    private final Model.SinglePartModel bedHead;
    private final Model.SinglePartModel bedFoot;

    public static TexturedModelData getFootTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData group = modelPartData.addChild("main", ModelPartBuilder.create(), ModelTransform.origin(8.0F, 24.0F, -8.0F));

        ModelPartData foot_r1 = group.addChild("foot_r1", ModelPartBuilder.create().uv(2, 24).cuboid(-8.0F, -11.0F, -8.0F, 16.0F, 13.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-8.0F, -1.0F, 11.0F, -1.5708F, 3.1416F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    public static TexturedModelData getHeadTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData group = modelPartData.addChild("main", ModelPartBuilder.create(), ModelTransform.origin(8.0F, 24.0F, -8.0F));

        ModelPartData head_r1 = group.addChild("head_r1", ModelPartBuilder.create().uv(2, 2).cuboid(-28.0F, -5.0F, -3.0F, 16.0F, 13.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-28.0F, -6.0F, 8.0F, -1.5708F, 3.1416F, 0.0F));

        ModelPartData pillow = group.addChild("pillow", ModelPartBuilder.create(), ModelTransform.origin(-28.0F, -5.0F, 8.0F));

        ModelPartData head_r2 = pillow.addChild("head_r2", ModelPartBuilder.create().uv(7, 5).mirrored().cuboid(-20.0F, -5.0F, -5.0F, 7.0F, 8.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(7, 5).cuboid(-27.0F, -5.0F, -5.0F, 7.0F, 8.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -1.5708F, 3.1416F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void render(BedBlockEntityRenderState state, MatrixStack matrixStack, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        SpriteIdentifier spriteIdentifier = state != null ? TexturedRenderLayers.getBedTextureId(state.dyeColor) : TexturedRenderLayers.getBedTextureId(DyeColor.WHITE);
        if (state != null) {
            BlockState blockState = state.blockState;
            renderPart(matrixStack, queue, blockState.get(BedBlock.PART) == BedPart.HEAD ? this.bedHead : this.bedFoot, blockState.get(BedBlock.FACING), spriteIdentifier, state.lightmapCoordinates, OverlayTexture.DEFAULT_UV, state.crumblingOverlay, 0);
        } else {
            matrixStack.push();
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(270.0f));
            matrixStack.translate(0.0,0,-2.0);
            renderPart(matrixStack, queue, this.bedHead, Direction.NORTH, spriteIdentifier, LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, null, 0);
            matrixStack.translate(0.0,0,1.0);
            renderPart(matrixStack, queue, this.bedFoot, Direction.NORTH, spriteIdentifier, LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, null, 0);
            matrixStack.pop();
        }
    }

    private void renderPart(MatrixStack matrix, RenderCommandQueue queue, Model.SinglePartModel part, Direction direction, SpriteIdentifier sprite, int light, int overlay, ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay, int i) {
        matrix.push();
        matrix.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0f));

        matrix.translate(0.5, -1.5, 0);
        matrix.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction.getPositiveHorizontalDegrees()));
        switch (direction) {
            case EAST: matrix.translate(-0.5, 0, 0); break;
            case WEST: matrix.translate(0.5, 0, 0); break;
            case NORTH: matrix.translate(0, 0, 0.5); break;
            case SOUTH: matrix.translate(0, 0, -0.5); break;
        }
        queue.submitModel(part, Unit.INSTANCE, matrix, sprite.getRenderLayer(RenderLayer::getEntitySolid), light, overlay, -1, materials.getSprite(sprite), i, crumblingOverlay);
        matrix.pop();
    }

    public void renderAsItem(MatrixStack matrices, RenderCommandQueue queue, int light, int overlay, SpriteIdentifier textureId, int i) {
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0f));
        matrices.translate(-1.0,0,1.0);
        this.renderPart(matrices, queue, this.bedHead, Direction.SOUTH, textureId, light, overlay, null, i);
        matrices.translate(0.0,0,-1.0);
        this.renderPart(matrices, queue, this.bedFoot, Direction.SOUTH, textureId, light, overlay, null, i);
        matrices.pop();
    }

    public void collectVertices(Set<Vector3f> vertices) {
        MatrixStack matrixStack = new MatrixStack();
        setTransforms(matrixStack, false);
        this.bedHead.getRootPart().collectVertices(matrixStack, vertices);
        matrixStack.loadIdentity();
        setTransforms(matrixStack, true);
        this.bedFoot.getRootPart().collectVertices(matrixStack, vertices);
    }

    private static void setTransforms(MatrixStack matrices, boolean isFoot) {
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0f));
        matrices.translate(-1.0,0, !isFoot ? 1.0F : 0.0F);
    }

    @Override
    public void updateRenderState(PFMBedBlockEntity bedBlockEntity, BedBlockEntityRenderState bedBlockEntityRenderState, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(bedBlockEntity, bedBlockEntityRenderState, tickProgress, cameraPos, crumblingOverlay);
        bedBlockEntityRenderState.dyeColor = bedBlockEntity.getColor();
        bedBlockEntityRenderState.facing = bedBlockEntity.getCachedState().get(BedBlock.FACING);
        bedBlockEntityRenderState.headPart = bedBlockEntity.getCachedState().get(BedBlock.PART) == BedPart.HEAD;
        if (bedBlockEntity.getWorld() != null) {
            DoubleBlockProperties.PropertySource<? extends BedBlockEntity> propertySource = DoubleBlockProperties.toPropertySource(BlockEntityType.BED, BedBlock::getBedPart, BedBlock::getOppositePartDirection, ChestBlock.FACING, bedBlockEntity.getCachedState(), bedBlockEntity.getWorld(), bedBlockEntity.getPos(), (world, pos) -> false);
            bedBlockEntityRenderState.lightmapCoordinates = propertySource.apply(new LightmapCoordinatesRetriever<>()).get(bedBlockEntityRenderState.lightmapCoordinates);
        }
    }

    @Override
    public BedBlockEntityRenderState createRenderState() {
        return new BedBlockEntityRenderState();
    }
}
