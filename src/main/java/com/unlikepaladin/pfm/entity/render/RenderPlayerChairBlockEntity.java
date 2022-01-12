package com.unlikepaladin.pfm.entity.render;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.util.UUIDTypeAdapter;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blockentities.PlayerChairBlockEntity;
import com.unlikepaladin.pfm.blocks.PlayerChair;
import com.unlikepaladin.pfm.entity.model.ChairEntityModel;
import com.unlikepaladin.pfm.registry.EntityPaladinClient;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.BlockState;

import net.minecraft.block.SkullBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;

import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.entity.model.DragonHeadEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.UUID;


public class RenderPlayerChairBlockEntity implements BlockEntityRenderer<PlayerChairBlockEntity> {

private final ChairEntityModel model;


    private static final Identifier TEXTURE = DefaultSkinHelper.getTexture();
public RenderPlayerChairBlockEntity(BlockEntityRendererFactory.Context ctx) {
    this.model = new ChairEntityModel(ctx.getLayerModelPart(EntityPaladinClient.MODEL_CHAIR_LAYER));
}


    public void render(PlayerChairBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockState blockState = entity.getCachedState();
        float f = ((Direction)blockState.get(PlayerChair.FACING)).asRotation() - 90;
        ChairEntityModel chairEntityModel = this.model;
            RenderLayer renderLayer = getRenderLayer(entity, entity.getOwner());
            renderChair(f, matrices, vertexConsumers, light, chairEntityModel, renderLayer);

    }

    public static void renderChair(float f, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ChairEntityModel model, RenderLayer renderLayer) {
        matrices.push();
        matrices.translate(0.5D, 0.5D, 0.5D);
        matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(f));
        matrices.translate(-0.5D, -0.5D, -0.5D);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrices.pop();
    }
    public static RenderLayer getRenderLayer(PlayerChairBlockEntity chairBlockEntity, @Nullable GameProfile profile) {
        if (profile != null) {
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            Map<com.mojang.authlib.minecraft.MinecraftProfileTexture.Type, MinecraftProfileTexture> map = minecraftClient.getSkinProvider().getTextures(profile);
            return map.containsKey(MinecraftProfileTexture.Type.SKIN) ? RenderLayer.getEntityTranslucent(minecraftClient.getSkinProvider().loadSkin((MinecraftProfileTexture) map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN)) : RenderLayer.getEntityCutoutNoCull(DefaultSkinHelper.getTexture(PlayerEntity.getUuidFromProfile(profile)));
        } else {
            return RenderLayer.getEntityCutoutNoCullZOffset(TEXTURE);
        }
    }
}
