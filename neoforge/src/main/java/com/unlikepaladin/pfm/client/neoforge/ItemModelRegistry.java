package com.unlikepaladin.pfm.client.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.DyeableFurnitureBlock;
import com.unlikepaladin.pfm.client.model.FurnitureTintSource;
import com.unlikepaladin.pfm.client.model.PFMBedModelRenderer;
import com.unlikepaladin.pfm.client.model.PFMItemModel;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;

@EventBusSubscriber(modid = "pfm", value = Dist.CLIENT)
public class ItemModelRegistry {
    @SubscribeEvent
    public static void registerItemModelTypes(RegisterItemModelsEvent event) {
        event.register(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "furniture_model"), PFMItemModel.Unbaked.CODEC);
    }

    @SubscribeEvent
    public static void registerSpecialModelRenderer(RegisterSpecialModelRendererEvent event) {
        event.register(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "pfm_bed"), PFMBedModelRenderer.Unbaked.CODEC);
    }

    @SubscribeEvent
    public static void registerTintSourceTypes(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "furniture_color"), FurnitureTintSource.CODEC);
    }

    @SubscribeEvent
    public static void registerSpecialModelRenderers(RegisterSpecialBlockModelRendererEvent event) {
        for (Block block : PaladinFurnitureModBlocksItems.getBeds()) {
            if (block instanceof DyeableFurnitureBlock)
                event.register(block, new PFMBedModelRenderer.Unbaked(((DyeableFurnitureBlock) block).getPFMColor()));
        }
    }


}
