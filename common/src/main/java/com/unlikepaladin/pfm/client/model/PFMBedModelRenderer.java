package com.unlikepaladin.pfm.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.entity.render.PFMBedBlockEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.item.DyeColor;
import org.joml.Vector3fc;

import java.util.function.Consumer;

public class PFMBedModelRenderer implements NoDataSpecialModelRenderer {
    private final PFMBedBlockEntityRenderer blockEntityRenderer;
    private final SpriteId textureId;

    public PFMBedModelRenderer(PFMBedBlockEntityRenderer blockEntityRenderer, SpriteId textureId) {
        this.blockEntityRenderer = blockEntityRenderer;
        this.textureId = textureId;
    }


    @Override
    public void submit(PoseStack matrices, SubmitNodeCollector queue, int light, int overlay, boolean glint, int i) {
        this.blockEntityRenderer.renderAsItem(matrices, queue, light, overlay, this.textureId, i);

    }

    @Override
    public void getExtents(Consumer<Vector3fc> vertices) {
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
        public SpecialModelRenderer<?> bake(BakingContext entityModels) {
            return new PFMBedModelRenderer(new PFMBedBlockEntityRenderer(entityModels), Sheets.createBedSprite(color));
        }
    }
}
