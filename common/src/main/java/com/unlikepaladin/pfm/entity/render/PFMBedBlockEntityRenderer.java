package com.unlikepaladin.pfm.entity.render;

import com.mojang.math.Axis;
import com.unlikepaladin.pfm.blocks.blockentities.PFMBedBlockEntity;
import com.unlikepaladin.pfm.client.EntityRenderIDs;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.state.BedRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.util.Unit;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.client.model.*;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.resources.model.Material;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.DyeColor;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Set;

public class PFMBedBlockEntityRenderer implements BlockEntityRenderer<PFMBedBlockEntity, BedRenderState> {
    private final MaterialSet materials;
    public PFMBedBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this(ctx.materials(), ctx.entityModelSet());
    }

    public PFMBedBlockEntityRenderer(SpecialModelRenderer.BakingContext ctx) {
        this(ctx.materials(), ctx.entityModelSet());
    }

    public PFMBedBlockEntityRenderer(MaterialSet materials, EntityModelSet models) {
        this.bedHead = new Model.Simple(models.bakeLayer(EntityRenderIDs.BED_HEAD_LAYER), RenderType::entitySolid);
        this.bedFoot = new Model.Simple(models.bakeLayer(EntityRenderIDs.BED_FOOT_LAYER), RenderType::entitySolid);
        this.materials = materials;
    }

    private final Model.Simple bedHead;
    private final Model.Simple bedFoot;

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
    public void submit(BedRenderState state, PoseStack matrixStack, SubmitNodeCollector queue, CameraRenderState cameraState) {
        Material spriteIdentifier = state != null ? Sheets.getBedMaterial(state.color) : Sheets.getBedMaterial(DyeColor.WHITE);
        if (state != null) {
            BlockState blockState = state.blockState;
            renderPart(matrixStack, queue, blockState.getValue(BedBlock.PART) == BedPart.HEAD ? this.bedHead : this.bedFoot, blockState.getValue(BedBlock.FACING), spriteIdentifier, state.lightCoords, OverlayTexture.NO_OVERLAY, state.breakProgress, 0);
        } else {
            matrixStack.pushPose();
            matrixStack.mulPose(Axis.YP.rotationDegrees(270.0f));
            matrixStack.translate(0.0,0,-2.0);
            renderPart(matrixStack, queue, this.bedHead, Direction.NORTH, spriteIdentifier, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, null, 0);
            matrixStack.translate(0.0,0,1.0);
            renderPart(matrixStack, queue, this.bedFoot, Direction.NORTH, spriteIdentifier, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, null, 0);
            matrixStack.popPose();
        }
    }

    private void renderPart(PoseStack matrix, OrderedSubmitNodeCollector queue, Model.Simple part, Direction direction, Material sprite, int light, int overlay, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, int i) {
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
        queue.submitModel(part, Unit.INSTANCE, matrix, sprite.renderType(RenderType::entitySolid), light, overlay, -1, materials.get(sprite), i, crumblingOverlay);
        matrix.popPose();
    }

    public void renderAsItem(PoseStack matrices, OrderedSubmitNodeCollector queue, int light, int overlay, Material textureId, int i) {
        matrices.pushPose();
        matrices.mulPose(Axis.YP.rotationDegrees(90.0f));
        matrices.translate(-1.0,0,1.0);
        this.renderPart(matrices, queue, this.bedHead, Direction.SOUTH, textureId, light, overlay, null, i);
        matrices.translate(0.0,0,-1.0);
        this.renderPart(matrices, queue, this.bedFoot, Direction.SOUTH, textureId, light, overlay, null, i);
        matrices.popPose();
    }

    public void getExtents(Set<Vector3f> vertices) {
        PoseStack matrixStack = new PoseStack();
        setTransforms(matrixStack, false);
        this.bedHead.root().getExtentsForGui(matrixStack, vertices);
        matrixStack.setIdentity();
        setTransforms(matrixStack, true);
        this.bedFoot.root().getExtentsForGui(matrixStack, vertices);
    }

    private static void setTransforms(PoseStack matrices, boolean isFoot) {
        matrices.mulPose(Axis.YP.rotationDegrees(90.0f));
        matrices.translate(-1.0,0, !isFoot ? 1.0F : 0.0F);
    }

    @Override
    public void extractRenderState(PFMBedBlockEntity bedBlockEntity, BedRenderState bedBlockEntityRenderState, float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(bedBlockEntity, bedBlockEntityRenderState, tickProgress, cameraPos, crumblingOverlay);
        bedBlockEntityRenderState.color = bedBlockEntity.getColor();
        bedBlockEntityRenderState.facing = bedBlockEntity.getBlockState().getValue(BedBlock.FACING);
        bedBlockEntityRenderState.isHead = bedBlockEntity.getBlockState().getValue(BedBlock.PART) == BedPart.HEAD;
        if (bedBlockEntity.getLevel() != null) {
            DoubleBlockCombiner.NeighborCombineResult<? extends BedBlockEntity> propertySource = DoubleBlockCombiner.combineWithNeigbour(BlockEntityType.BED, BedBlock::getBlockType, BedBlock::getConnectedDirection, ChestBlock.FACING, bedBlockEntity.getBlockState(), bedBlockEntity.getLevel(), bedBlockEntity.getBlockPos(), (world, pos) -> false);
            bedBlockEntityRenderState.lightCoords = propertySource.apply(new BrightnessCombiner<>()).get(bedBlockEntityRenderState.lightCoords);
        }
    }

    @Override
    public BedRenderState createRenderState() {
        return new BedRenderState();
    }
}
