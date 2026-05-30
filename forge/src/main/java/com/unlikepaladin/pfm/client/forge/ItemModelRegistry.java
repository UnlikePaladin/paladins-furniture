package com.unlikepaladin.pfm.client.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.DyeableFurnitureBlock;
import com.unlikepaladin.pfm.client.model.FurnitureTintSource;
import com.unlikepaladin.pfm.client.model.PFMBedModelRenderer;
import com.unlikepaladin.pfm.client.model.PFMItemModel;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.resources.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CreateSpecialBlockRendererEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ItemModelRegistry {
    public static void registerItemModelTypes() {
        ItemModels.ID_MAPPER.put(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "furniture_model"), PFMItemModel.Unbaked.CODEC);
        SpecialModelRenderers.ID_MAPPER.put(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "pfm_bed"), PFMBedModelRenderer.Unbaked.CODEC);
        ItemTintSources.ID_MAPPER.put(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "furniture_color"), FurnitureTintSource.CODEC);
    }


    public static void registerSpecialModelRenderers(CreateSpecialBlockRendererEvent event) {
        for (Block block : PaladinFurnitureModBlocksItems.getBeds()) {
            if (block instanceof DyeableFurnitureBlock)
                event.register(block, new PFMBedModelRenderer.Unbaked(((DyeableFurnitureBlock) block).getPFMColor()));
        }
    }


}
