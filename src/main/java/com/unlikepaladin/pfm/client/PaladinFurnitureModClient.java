package com.unlikepaladin.pfm.client;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.ArmChairDyeable;
import com.unlikepaladin.pfm.blocks.ClassicChairDyeable;
import com.unlikepaladin.pfm.entity.model.ChairEntityModel;
import com.unlikepaladin.pfm.entity.model.ModelEmpty;
import com.unlikepaladin.pfm.entity.render.RenderChair;
import com.unlikepaladin.pfm.entity.render.RenderPlayerChairBlockEntity;
import com.unlikepaladin.pfm.menus.FreezerScreen;
import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import com.unlikepaladin.pfm.registry.EntityRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.unlikepaladin.pfm.registry.EntityPaladinClient.MODEL_CHAIR_LAYER;
import static com.unlikepaladin.pfm.registry.EntityPaladinClient.MODEL_CUBE_LAYER;


public class PaladinFurnitureModClient implements ClientModInitializer {
    public static final Logger CLIENT_LOGGER = LogManager.getLogger();
    @Override
    public void onInitializeClient() {

       // EntityPaladinClient.registerClientEntity();
      //  EntityRenderRegistry.registerRender();
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> state.get(ClassicChairDyeable.COLORID).getFireworkColor(), BlockItemRegistry.CHAIR_CLASSIC_WOOL);
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> state.get(ArmChairDyeable.COLORID).getFireworkColor(), BlockItemRegistry.ARM_CHAIR_STANDARD);
       //ScreenRegistry.register(PaladinFurnitureMod.Player_Chair_Screen_Handler, PlayerChairScreen::new);

        EntityRendererRegistry.INSTANCE.register(EntityRegistry.CHAIR, (context) -> {
            return new RenderChair(context);});

        EntityModelLayerRegistry.registerModelLayer(MODEL_CUBE_LAYER, ModelEmpty::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(MODEL_CHAIR_LAYER, ChairEntityModel::getTexturedModelData);

        BlockEntityRendererRegistry.INSTANCE.register(PaladinFurnitureMod.PLAYER_CHAIR_BLOCK_ENTITY, RenderPlayerChairBlockEntity::new);
        ScreenRegistry.register(PaladinFurnitureMod.FREEZER_SCREEN_HANDLER, FreezerScreen::new);


    }
    private static CompletableFuture globalLoadTask;
    public static CompletableFuture doTask(Runnable toRun) {
        return doTask(toRun, null);
    }

    public static CompletableFuture doTask(Runnable toRun, @Nullable Runnable onFinished) {
        //If the global load task doesn't exist, create it.
        if (globalLoadTask == null || globalLoadTask.isDone()) {
            globalLoadTask = CompletableFuture.runAsync(
                    () -> {
                        runTask(toRun, onFinished);
                    }
            );
        } else {
            //Otherwise, queue up next task.
            globalLoadTask = globalLoadTask.thenRunAsync(
                    () -> {
                        runTask(toRun, onFinished);
                    }
            );
        }

        return globalLoadTask;
    }

    private static void runTask(Runnable toRun, @Nullable Runnable onFinished) {
        toRun.run();

        if (onFinished != null)
            onFinished.run();
    }

    }

