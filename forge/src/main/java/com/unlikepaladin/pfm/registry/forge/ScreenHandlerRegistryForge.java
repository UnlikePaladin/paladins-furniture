package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.PaladinFurnitureModClient;
import com.unlikepaladin.pfm.menus.*;
import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import com.unlikepaladin.pfm.registry.ScreenHandlerRegistry;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.stats.StatType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ScreenHandlerRegistryForge {

    @SubscribeEvent
    public static void registerScreenHandlers(RegistryEvent.Register<MenuType<?>> event) {
        ScreenHandlerRegistry.registerScreenHandlers();
        event.getRegistry().registerAll(
            ScreenHandlerRegistryImpl.screenHandlerTypeList.toArray(new MenuType[0])
        );
    }
}
