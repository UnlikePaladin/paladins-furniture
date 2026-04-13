package com.unlikepaladin.pfm.client.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import com.unlikepaladin.pfm.blocks.blockentities.PFMBedBlockEntity;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.bed.UnbakedBedModel;
import com.unlikepaladin.pfm.client.PFMBakedModelManagerAccessor;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.entity.render.OfficeChairEntityRenderer;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.core.BlockPos;
import net.minecraftforge.client.ForgeHooksClient;

import java.util.*;

public class PFMItemRendererForge extends BlockEntityWithoutLevelRenderer {
    public static PFMItemRendererForge INSTANCE = new PFMItemRendererForge(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());

    private final PFMBedBlockEntity renderBed;
    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    public PFMItemRendererForge(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet loader) {
        super(blockEntityRenderDispatcher, loader);
        this.blockEntityRenderDispatcher = blockEntityRenderDispatcher;
        if (PaladinFurnitureMod.furnitureEntryMap.get(SimpleBedBlock.class) != null ) {
            renderBed = new PFMBedBlockEntity(BlockPos.ZERO, PaladinFurnitureMod.furnitureEntryMap.get(SimpleBedBlock.class).getVariantToBlockMapList().get(WoodVariantRegistry.OAK).iterator().next().defaultBlockState());
        } else {
            renderBed = null;
        }
    }

    public BakedModel getBedModelForTransform(boolean classic) {
        return classic ? UnbakedBedModel.inventoryModels.getB() : UnbakedBedModel.inventoryModels.getA();

    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        matrices.popPose();
        boolean leftHanded = Minecraft.getInstance().player != null && Minecraft.getInstance().player.getMainArm() == HumanoidArm.LEFT && mode.firstPerson();

        boolean glint = stack.hasFoil();
        VertexConsumer consumer = ItemRenderer.getFoilBuffer(vertexConsumers, ItemBlockRenderTypes.getRenderType(stack), true, glint);
        if (stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof SimpleBedBlock) {
            matrices.pushPose();

            Block block = ((BlockItem) stack.getItem()).getBlock();
            BakedModel bedModel = getBedModelForTransform(stack.getItem().getDescriptionId().contains("classic"));
            bedModel.applyTransform(mode, matrices, leftHanded);
            matrices.translate(-.5, -.5, -.5); // Replicate ItemRenderer's translation

            // annoyingly, forge is applying some weird wrong transform to the bed model when rendering as an item
            // so i have to manually rotate and translate it to be correct, also happens only here on 1.21.1, how peculiar

            BakedModel actualModel = ModelHelper.getModelFromIdentifier(UnbakedBedModel.BED_MODEL_ID);

            ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
            BlockState state = stack.getItem() instanceof BlockItem ? ((BlockItem) stack.getItem()).getBlock().getDefaultState() : null;

            Random random = Random.create();
            long randomSeed = 42L;
            for (Direction direction : Direction.values()) {
                random.setSeed(randomSeed);
                itemRenderer.renderBakedItemQuads(matrices, consumer, ((PFMBakedModelGetQuadsExtension) actualModel).getQuadsCached(stack, state, direction, random), stack, light, overlay);
            }
            random.setSeed(randomSeed);
            itemRenderer.renderBakedItemQuads(matrices, consumer, ((PFMBakedModelGetQuadsExtension)actualModel).getQuadsCached(stack, state, null, random), stack, light, overlay);

            this.renderBed.setColor(((SimpleBedBlock)block).getColor());
            this.blockEntityRenderDispatcher.renderItem(renderBed, matrices, vertexConsumers, light, overlay);
            matrices.popPose();
        } else if (stack.getItem() == PaladinFurnitureModBlocksItems.OFFICE_CHAIR_ITEM) {
            matrices.pushPose();
            BakedModel chairModel = ModelHelper.getModelFromIdentifier(OfficeChairEntityRenderer.MODEL_IDS[0]);

            chairModel.applyTransform(mode, matrices, leftHanded);
            matrices.translate(-.5, -.5, -.5); // Replicate ItemRenderer's translation

            OfficeChairEntityRenderer.renderItem(stack, matrices, mode, vertexConsumers, leftHanded, light, overlay);
            matrices.popPose();
        }
        matrices.pushPose();
    }
}
