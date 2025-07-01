package com.unlikepaladin.pfm.client.model;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.entity.render.PFMBedBlockEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.item.model.special.SimpleSpecialModelRenderer;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.DyeColor;

public class PFMBedModelRenderer implements SimpleSpecialModelRenderer {
    private final PFMBedBlockEntityRenderer blockEntityRenderer;
    private final SpriteIdentifier textureId;

    public PFMBedModelRenderer(PFMBedBlockEntityRenderer blockEntityRenderer, SpriteIdentifier textureId) {
        this.blockEntityRenderer = blockEntityRenderer;
        this.textureId = textureId;
    }

    @Override
    public void render(
            ItemDisplayContext modelTransformationMode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, boolean glint
    ) {
        this.blockEntityRenderer.renderAsItem(matrices, vertexConsumers, light, overlay, this.textureId);
    }

    @Environment(EnvType.CLIENT)
    public record Unbaked(DyeColor color) implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<PFMBedModelRenderer.Unbaked> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(DyeColor.CODEC.fieldOf("texture").forGetter(PFMBedModelRenderer.Unbaked::color)).apply(instance, PFMBedModelRenderer.Unbaked::new)
        );

        @Override
        public MapCodec<PFMBedModelRenderer.Unbaked> getCodec() {
            return CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(LoadedEntityModels entityModels) {
            return new PFMBedModelRenderer(new PFMBedBlockEntityRenderer(entityModels), TexturedRenderLayers.createBedTextureId(color));
        }
    }
}
