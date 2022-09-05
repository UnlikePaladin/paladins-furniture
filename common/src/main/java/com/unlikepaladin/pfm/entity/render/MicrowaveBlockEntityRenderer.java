package com.unlikepaladin.pfm.entity.render;


import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

public class MicrowaveBlockEntityRenderer<T extends MicrowaveBlockEntity> extends BlockEntityRenderer<T> {
    public ItemStack itemStack;
    public MicrowaveBlockEntityRenderer(BlockEntityRenderDispatcher ctx) {
        super(ctx);
    }

    @Override
    public void render(T blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        itemStack = blockEntity.getStack(0);
        matrices.push();
        int lightAbove = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), blockEntity.getPos().up());
        Direction facing = blockEntity.getFacing();
        float x,y,z;
        if (facing == Direction.NORTH) {
                x = 0.4f;
                y = 0.2f;
                z = 0.5f;
            }
        else if (facing == Direction.SOUTH) {
                x = 0.6f;
                y = 0.2f;
                z = 0.5f;
            }
        else if (facing == Direction.WEST) {
                x = 0.5f;
                y = 0.2f;
                z = 0.6f;
            }
        else {
                x = 0.5f;
                y = 0.2f;
                z = 0.4f;
            }

        matrices.translate(x, y ,z);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-facing.asRotation()));
        if (blockEntity.isActive && MicrowaveBlockEntity.canAcceptRecipeOutput(blockEntity.getRecipe(), blockEntity.inventory ,blockEntity.getMaxCountPerStack())) {
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((blockEntity.getWorld().getTime() + tickDelta) * 4));}
        matrices.scale(0.5f, 0.5f, 0.5f);
        MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
        matrices.pop();
    }



}
