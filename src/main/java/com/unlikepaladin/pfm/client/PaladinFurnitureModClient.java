package com.unlikepaladin.pfm.client;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.ArmChairDyeable;
import com.unlikepaladin.pfm.blocks.ClassicChairDyeable;
import com.unlikepaladin.pfm.entity.model.ModelEmpty;
import com.unlikepaladin.pfm.entity.render.RenderChair;
import com.unlikepaladin.pfm.menus.FreezerScreen;
import com.unlikepaladin.pfm.menus.IronStoveScreen;
import com.unlikepaladin.pfm.menus.StoveScreen;
import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import com.unlikepaladin.pfm.registry.EntityRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.RenderLayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.unlikepaladin.pfm.registry.EntityPaladinClient.MODEL_CUBE_LAYER;


public class PaladinFurnitureModClient implements ClientModInitializer {
    public static final Logger CLIENT_LOGGER = LogManager.getLogger();
    @Override
    public void onInitializeClient() {

       // EntityPaladinClient.registerClientEntity();
      //  EntityRenderRegistry.registerRender();
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> state.get(ClassicChairDyeable.COLORID).getFireworkColor(), BlockItemRegistry.CHAIR_CLASSIC_WOOL);
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> state.get(ArmChairDyeable.COLORID).getFireworkColor(), BlockItemRegistry.ARM_CHAIR_STANDARD);

        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.OAK_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.SPRUCE_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.BIRCH_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.ACACIA_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.JUNGLE_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.DARK_OAK_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.CRIMSON_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.WARPED_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.STRIPPED_OAK_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.STRIPPED_DARK_OAK_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.STRIPPED_BIRCH_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.STRIPPED_SPRUCE_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.STRIPPED_ACACIA_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.STRIPPED_JUNGLE_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.STRIPPED_CRIMSON_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.STRIPPED_WARPED_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.CONCRETE_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.DARK_CONCRETE_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.DARK_WOOD_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.LIGHT_WOOD_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.GRANITE_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.DIORITE_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.NETHERITE_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.CALCITE_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.ANDESITE_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.STONE_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.BLACKSTONE_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.DEEPSLATE_KITCHEN_SINK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, BlockItemRegistry.DEEPSLATE_TILE_KITCHEN_SINK);

        EntityRendererRegistry.INSTANCE.register(EntityRegistry.CHAIR, RenderChair::new);

        EntityModelLayerRegistry.registerModelLayer(MODEL_CUBE_LAYER, ModelEmpty::getTexturedModelData);

        ScreenRegistry.register(PaladinFurnitureMod.FREEZER_SCREEN_HANDLER, FreezerScreen::new);
        ScreenRegistry.register(PaladinFurnitureMod.STOVE_SCREEN_HANDLER, StoveScreen::new);
        ScreenRegistry.register(PaladinFurnitureMod.IRON_STOVE_SCREEN_HANDLER, IronStoveScreen::new);

        BlockRenderLayerMap.INSTANCE.putBlock(BlockItemRegistry.WHITE_FRIDGE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockItemRegistry.XBOX_FRIDGE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockItemRegistry.SIMPLE_STOVE, RenderLayer.getCutout());

    }


    }

