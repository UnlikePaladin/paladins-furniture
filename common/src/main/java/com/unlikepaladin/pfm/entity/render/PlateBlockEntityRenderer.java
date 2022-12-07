package com.unlikepaladin.pfm.entity.render;

import com.unlikepaladin.pfm.blocks.Plate;
import com.unlikepaladin.pfm.blocks.blockentities.PlateBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

public class PlateBlockEntityRenderer<T extends PlateBlockEntity> implements BlockEntityRenderer<T> {
    public ItemStack itemStack;
    private static final float SCALE = 0.375f;
    public PlateBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }
    @Override
    public void render(PlateBlockEntity plateBlockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
        Direction direction = plateBlockEntity.getCachedState().get(Plate.FACING);
        itemStack = plateBlockEntity.getItemInPlate();
        matrices.push();
        Direction direction2 = Direction.fromHorizontal((direction.getHorizontal()) % 4);
        float g = -direction2.asRotation();
        Direction dir = plateBlockEntity.getCachedState().get(Plate.FACING);
        switch (dir) {
            case NORTH -> matrices.translate(0.5, 0.08, 0.65);
            case SOUTH -> matrices.translate(0.5, 0.08, 0.35);
            case WEST -> matrices.translate(0.65, 0.08, 0.5);
            case EAST -> matrices.translate(0.35, 0.08, 0.5);
        }
        int rot = 90;
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(g));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rot));
        if (Registries.ITEM.getId(itemStack.getItem()).toString().equals("sandwichable:sandwich")) {
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(270.0f));
            matrices.translate(0.0, 0.11, 0.05);
        }
        int lightAbove = WorldRenderer.getLightmapCoordinates(plateBlockEntity.getWorld(), plateBlockEntity.getPos().up());
        MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumerProvider, 0);
        matrices.pop();
    }
}
