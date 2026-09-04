package com.unlikepaladin.pfm.client.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.DyeableFurnitureBlock;
import com.unlikepaladin.pfm.client.model.FurnitureTintSource;
import com.unlikepaladin.pfm.client.model.PFMBedModelRenderer;
import com.unlikepaladin.pfm.client.model.PFMItemModel;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.client.renderer.block.BuiltInBlockModels;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.resources.Identifier;

public class ItemModelRegistry {
    public static void registerItemModelTypes() {
        ItemModels.ID_MAPPER.put(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "furniture_model"), PFMItemModel.Unbaked.CODEC);
        SpecialModelRenderers.ID_MAPPER.put(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "pfm_bed"), PFMBedModelRenderer.Unbaked.CODEC);
        ItemTintSources.ID_MAPPER.put(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "furniture_color"), FurnitureTintSource.CODEC);
    }


    public static void registerSpecialModelRenderers(BuiltInBlockModels.Builder builder) {
        for (Block block : PaladinFurnitureModBlocksItems.getBeds()) {
            if (block instanceof DyeableFurnitureBlock)
                builder.put(BuiltInBlockModels.special(new PFMBedModelRenderer.Unbaked(((DyeableFurnitureBlock) block).getPFMColor())), block);
        }
    }

}
