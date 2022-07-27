package com.unlikepaladin.pfm.entity.render;

import com.unlikepaladin.pfm.blocks.KitchenStovetop;
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
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

public class PlateBlockEntityRenderer implements BlockEntityRenderer<PlateBlockEntity> {
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
        Direction dir = plateBlockEntity.getCachedState().get(KitchenStovetop.FACING);
        switch(dir) {
            case NORTH:
                matrices.translate(0.5, 0.09, 0.65);
                break;
            case SOUTH:
                matrices.translate(0.5, 0.09, 0.35);
                break;
            case WEST:
                matrices.translate(0.65, 0.09, 0.5);
                break;
            case EAST:
                matrices.translate(0.35, 0.09, 0.5);
        }
        int rot = 90;
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(g));
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(rot));
        int lightAbove = WorldRenderer.getLightmapCoordinates(plateBlockEntity.getWorld(), plateBlockEntity.getPos().up());
        MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumerProvider, 0);
        matrices.pop();
    }
}
