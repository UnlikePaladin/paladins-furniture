package com.unlikepaladin.pfm.entity.render;

import com.unlikepaladin.pfm.blocks.models.chair.UnbakedChairModel;
import com.unlikepaladin.pfm.client.EntityRenderIDs;
import com.unlikepaladin.pfm.client.PFMBakedModelManagerAccessor;
import com.unlikepaladin.pfm.entity.ChairEntity;
import com.unlikepaladin.pfm.entity.OfficeChairEntity;
import com.unlikepaladin.pfm.entity.model.ModelEmpty;
import com.unlikepaladin.pfm.entity.model.OfficeChairModelEmpty;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OfficeChairEntityRenderer extends MobEntityRenderer<OfficeChairEntity, OfficeChairModelEmpty> {
    public static final Identifier MODEL_ID = new Identifier("pfm:block/office_chair/office_chair");

    public OfficeChairEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new OfficeChairModelEmpty(), 0f);
    }

    @Override
    public void render(OfficeChairEntity mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        matrixStack.push();
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F - f));

        matrixStack.translate(-0.45, 0, -0.5);

        BakedModel model = ((PFMBakedModelManagerAccessor)MinecraftClient.getInstance().getBakedModelManager()).pfm$getModelFromNormalID(MODEL_ID);

        List<BakedQuad> chairQuads = new ArrayList<>(Arrays.stream(Direction.values()).map(direction -> model.getQuads(null, direction, mobEntity.world.random))
                .flatMap(List::stream).toList());
        chairQuads.addAll(model.getQuads(null, null, mobEntity.world.random));


        for (BakedQuad quad : chairQuads) {
            float brightness = mobEntity.world.getBrightness(quad.getFace(), quad.hasShade());

            vertexConsumerProvider.getBuffer(RenderLayer.getSolid())
                    .quad(matrixStack.peek(), quad, brightness, brightness, brightness,  light, 0);
        }

        matrixStack.pop();
    }

    @Override
    public Identifier getTexture(OfficeChairEntity entity) {
        return null;
    }
}
