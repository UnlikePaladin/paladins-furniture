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
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> BiomeColors.getWaterColor(view, pos), BlockItemRegistry.OAK_KITCHEN_SINK);
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

