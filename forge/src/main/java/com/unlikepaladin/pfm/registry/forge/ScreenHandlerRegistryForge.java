package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.PaladinFurnitureModClient;
import com.unlikepaladin.pfm.menus.*;
import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.stat.StatType;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ScreenHandlerRegistryForge {

    @SubscribeEvent
    public static void registerScreenHandlers(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.MENU_TYPES, screenHandlerTypeRegisterHelper -> {
            screenHandlerTypeRegisterHelper.register(new Identifier(PaladinFurnitureMod.MOD_ID, "freezer_block_entity"), ScreenHandlerIDs.FREEZER_SCREEN_HANDLER = IForgeMenuType.create(FreezerScreenHandler::new));
            screenHandlerTypeRegisterHelper.register(new Identifier(PaladinFurnitureMod.MOD_ID,"furniture"), ScreenHandlerIDs.WORKBENCH_SCREEN_HANDLER = IForgeMenuType.create(WorkbenchScreenHandler::new));
            screenHandlerTypeRegisterHelper.register(new Identifier(PaladinFurnitureMod.MOD_ID,"stove_block_entity"), ScreenHandlerIDs.STOVE_SCREEN_HANDLER = IForgeMenuType.create(StoveScreenHandler::new));
            screenHandlerTypeRegisterHelper.register( new Identifier(PaladinFurnitureMod.MOD_ID,"iron_stove_block_entity"), ScreenHandlerIDs.IRON_STOVE_SCREEN_HANDLER = IForgeMenuType.create(IronStoveScreenHandler::new));
            screenHandlerTypeRegisterHelper.register(new Identifier(PaladinFurnitureMod.MOD_ID,"microwave_block_entity"), ScreenHandlerIDs.MICROWAVE_SCREEN_HANDLER = IForgeMenuType.create(MicrowaveScreenHandler::new));
        });
    }
}
