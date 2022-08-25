package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.PaladinFurnitureModClient;
import com.unlikepaladin.pfm.menus.*;
import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.stat.StatType;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmlclient.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ScreenHandlerRegistryForge {

    @SubscribeEvent
    public static void registerScreenHandlers(RegistryEvent.Register<ScreenHandlerType<?>> event) {
        ScreenHandlerIDs.FREEZER_SCREEN_HANDLER = IForgeContainerType.create(FreezerScreenHandler::new);
        ScreenHandlerIDs.WORKBENCH_SCREEN_HANDLER = IForgeContainerType.create(WorkbenchScreenHandler::new);
        ScreenHandlerIDs.STOVE_SCREEN_HANDLER = IForgeContainerType.create(StoveScreenHandler::new);
        ScreenHandlerIDs.IRON_STOVE_SCREEN_HANDLER = IForgeContainerType.create(IronStoveScreenHandler::new);
        ScreenHandlerIDs.MICROWAVE_SCREEN_HANDLER = IForgeContainerType.create(MicrowaveScreenHandler::new);

        event.getRegistry().registerAll(

                ScreenHandlerIDs.WORKBENCH_SCREEN_HANDLER.setRegistryName("furniture"),
                ScreenHandlerIDs.STOVE_SCREEN_HANDLER.setRegistryName("stove_block_entity"),
                ScreenHandlerIDs.IRON_STOVE_SCREEN_HANDLER.setRegistryName("iron_stove_block_entity"),
                ScreenHandlerIDs.MICROWAVE_SCREEN_HANDLER .setRegistryName("microwave_block_entity"),
                ScreenHandlerIDs.FREEZER_SCREEN_HANDLER.setRegistryName("freezer_block_entity")
            );

    }
}
