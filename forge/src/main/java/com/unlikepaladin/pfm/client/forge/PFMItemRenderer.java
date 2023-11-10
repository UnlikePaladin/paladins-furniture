package com.unlikepaladin.pfm.client.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.basicLamp.UnbakedBasicLampModel;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = PaladinFurnitureMod.MOD_ID, value = Dist.CLIENT)
public class PFMItemRenderer extends BuiltinModelItemRenderer {
    public static final PFMItemRenderer INSTANCE = new PFMItemRenderer();
    public PFMItemRenderer() {
        super(MinecraftClient.getInstance().getBlockEntityRenderDispatcher(), MinecraftClient.getInstance().getEntityModelLoader());
    }

    @Override
    public void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (stack.isOf(PaladinFurnitureModBlocksItems.BASIC_LAMP_ITEM)) {
            WoodVariant variant = WoodVariantRegistry.OAK;
            if (stack.hasNbt()) {
                variant = WoodVariantRegistry.getVariant(Identifier.tryParse(stack.getSubNbt("BlockEntityTag").getString("variant")));
            }
            Map<WoodVariant, Map<String, BakedModel>> bakedModels = new LinkedHashMap<>();
            List<String> modelParts = new ArrayList<>(UnbakedBasicLampModel.MODEL_PARTS_BASE);
            modelParts.addAll(UnbakedBasicLampModel.STATIC_PARTS);
            for (WoodVariant woodVariant : WoodVariantRegistry.getVariants()) {
                bakedModels.put(woodVariant, new LinkedHashMap<>());
                for (String part : modelParts) {
                    bakedModels.get(woodVariant).put(part, MinecraftClient.getInstance().getBakedModelManager().getModel(new Identifier(PaladinFurnitureMod.MOD_ID, part.replaceAll("template", woodVariant.asString()))));
                }
            }
            boolean glint = stack.hasGlint();
            matrices.pop();
            ItemRenderer renderer = MinecraftClient.getInstance().getItemRenderer();
            matrices.push();
            BakedModel lampShadeModel = (bakedModels.get(variant).get(modelParts.get(4))).applyTransform(mode, matrices, false);
            BakedModel poleModel = (bakedModels.get(variant).get(modelParts.get(2)));
            BakedModel bulbModel = (bakedModels.get(variant).get(modelParts.get(5)));

            matrices.translate(-.5, -.5, -.5); // Replicate ItemRenderer's translation

            VertexConsumer consumer = ItemRenderer.getItemGlintConsumer(vertexConsumers, RenderLayers.getItemLayer(stack, true), true, glint);
            renderer.renderBakedItemModel(lampShadeModel, stack, light, overlay, matrices, consumer);
            renderer.renderBakedItemModel(poleModel, stack, light, overlay, matrices, consumer);
            renderer.renderBakedItemModel(bulbModel, stack, light, overlay, matrices, consumer);
        }
    }
}
