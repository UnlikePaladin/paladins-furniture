package com.unlikepaladin.pfm.client.forge;

import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import com.unlikepaladin.pfm.blocks.blockentities.PFMBedBlockEntity;
import com.unlikepaladin.pfm.blocks.models.bed.UnbakedBedModel;
import com.unlikepaladin.pfm.client.PFMBakedModelManagerAccessor;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.entity.render.OfficeChairEntityRenderer;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
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
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.ForgeHooksClient;

import java.util.HashMap;
import java.util.Map;

public class PFMItemRendererForge extends BuiltinModelItemRenderer {
    public static PFMItemRendererForge INSTANCE = new PFMItemRendererForge(MinecraftClient.getInstance().getBlockEntityRenderDispatcher(), MinecraftClient.getInstance().getEntityModelLoader());

    private final PFMBedBlockEntity renderBed;
    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    public PFMItemRendererForge(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelLoader loader) {
        super(blockEntityRenderDispatcher, loader);
        this.blockEntityRenderDispatcher = blockEntityRenderDispatcher;
        if (PaladinFurnitureModBlocksItems.furnitureEntryMap.get(SimpleBedBlock.class) != null ) {
            renderBed = new PFMBedBlockEntity(BlockPos.ORIGIN, PaladinFurnitureModBlocksItems.furnitureEntryMap.get(SimpleBedBlock.class).getVariantToBlockMapList().get(WoodVariantRegistry.OAK).iterator().next().getDefaultState());
        } else {
            renderBed = null;
        }
    }

    static Map<Boolean, BakedModel> bedModel = new HashMap<>();
    public BakedModel getBedModelForTransform(boolean classic) {
        if (bedModel.containsKey(classic) && bedModel.get(classic) != null) {
            return bedModel.get(classic);
        }
        bedModel.put(classic, ((PFMBakedModelManagerAccessor)MinecraftClient.getInstance().getBakedModelManager()).pfm$getModelFromNormalID(UnbakedBedModel.BED_MODEL_PARTS_BASE[classic ? 23 : 11]));
        return ((PFMBakedModelManagerAccessor)MinecraftClient.getInstance().getBakedModelManager()).pfm$getModelFromNormalID(UnbakedBedModel.BED_MODEL_ID);
    }

    @Override
    public void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.pop();
        boolean leftHanded = MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.getMainArm() == Arm.LEFT && mode.isFirstPerson();

        boolean glint = stack.hasGlint();
        VertexConsumer consumer = ItemRenderer.getItemGlintConsumer(vertexConsumers, RenderLayers.getItemLayer(stack, true), true, glint);
        if (stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof SimpleBedBlock) {
            matrices.push();

            Block block = ((BlockItem) stack.getItem()).getBlock();
            BakedModel modelForTransform = getBedModelForTransform(stack.getItem().getTranslationKey().contains("classic"));
            ForgeHooksClient.handleCameraTransforms(matrices, modelForTransform, mode, leftHanded);
            matrices.translate(-.5, -.5, -.5); // Replicate ItemRenderer's translation

            BakedModel actualModel = ((PFMBakedModelManagerAccessor)MinecraftClient.getInstance().getBakedModelManager()).pfm$getModelFromNormalID(UnbakedBedModel.BED_MODEL_ID);
            ForgeHooksClient.drawItemLayered(MinecraftClient.getInstance().getItemRenderer(), actualModel, stack, matrices, vertexConsumers, light, overlay, false);

            this.renderBed.setColor(((SimpleBedBlock)block).getColor());
            this.blockEntityRenderDispatcher.renderEntity(renderBed, matrices, vertexConsumers, light, overlay);
            matrices.pop();
        } else if (stack.getItem() == PaladinFurnitureModBlocksItems.OFFICE_CHAIR_ITEM) {
            matrices.push();
            BakedModel chairModel = ((PFMBakedModelManagerAccessor)MinecraftClient.getInstance().getBakedModelManager())
                    .pfm$getModelFromNormalID(OfficeChairEntityRenderer.MODEL_IDS[0]);

            ForgeHooksClient.handleCameraTransforms(matrices, chairModel, mode, leftHanded);
            matrices.translate(-.5, -.5, -.5); // Replicate ItemRenderer's translation

            OfficeChairEntityRenderer.renderItem(stack, matrices, mode, vertexConsumers, leftHanded, light, overlay);
            matrices.pop();
        }
        matrices.push();
    }
}
