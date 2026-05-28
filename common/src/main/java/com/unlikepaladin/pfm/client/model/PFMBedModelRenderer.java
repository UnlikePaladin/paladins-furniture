package com.unlikepaladin.pfm.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.entity.render.PFMBedBlockEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

import java.util.Set;

public class PFMBedModelRenderer implements NoDataSpecialModelRenderer {
    private final PFMBedBlockEntityRenderer blockEntityRenderer;
    private final Material textureId;

    public PFMBedModelRenderer(PFMBedBlockEntityRenderer blockEntityRenderer, Material textureId) {
        this.blockEntityRenderer = blockEntityRenderer;
        this.textureId = textureId;
    }

    @Override
    public void render(
            ItemDisplayContext modelTransformationMode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, boolean glint
    ) {
        this.blockEntityRenderer.renderAsItem(matrices, vertexConsumers, light, overlay, this.textureId);
    }

    @Override
    public void getExtents(Set<Vector3f> vertices) {
        this.blockEntityRenderer.getExtents(vertices);
    }

    @Environment(EnvType.CLIENT)
    public record Unbaked(DyeColor color) implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<PFMBedModelRenderer.Unbaked> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(DyeColor.CODEC.fieldOf("texture").forGetter(PFMBedModelRenderer.Unbaked::color)).apply(instance, PFMBedModelRenderer.Unbaked::new)
        );

        @Override
        public MapCodec<PFMBedModelRenderer.Unbaked> type() {
            return CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet entityModels) {
            return new PFMBedModelRenderer(new PFMBedBlockEntityRenderer(entityModels), Sheets.createBedMaterial(color));
        }
    }
}
