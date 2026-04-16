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

    @Environment(EnvType.CLIENT)
    public record Unbaked(ResourceLocation texture) implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<PFMBedModelRenderer.Unbaked> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(ResourceLocation.CODEC.fieldOf("texture").forGetter(PFMBedModelRenderer.Unbaked::texture)).apply(instance, PFMBedModelRenderer.Unbaked::new)
        );

        public Unbaked(DyeColor color) {
            this(Sheets.colorToResourceMaterial(color));
        }

        @Override
        public MapCodec<PFMBedModelRenderer.Unbaked> type() {
            return CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet entityModels) {
            return new PFMBedModelRenderer(new PFMBedBlockEntityRenderer(entityModels), Sheets.createBedMaterial(this.texture));
        }
    }
}
