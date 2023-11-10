package com.unlikepaladin.pfm.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.config.PaladinFurnitureModConfig;
import com.unlikepaladin.pfm.registry.dynamic.forge.LateBlockRegistryForge;
import com.unlikepaladin.pfm.registry.forge.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.SharedConstants;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.metadata.PackResourceMetadata;
import net.minecraft.text.LiteralText;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;


@Mod(PaladinFurnitureMod.MOD_ID)
public class PaladinFurnitureModForge extends PaladinFurnitureMod {
    public static PaladinFurnitureModConfig pfmConfig;
    public PaladinFurnitureModForge() {
        pfmConfig = new PaladinFurnitureModConfig(FMLPaths.CONFIGDIR.get().resolve("pfm.properties"));
        try {
            pfmConfig.initialize();
        } catch (IOException e) {
            GENERAL_LOGGER.error("Failed to initialize Paladin's Furniture configuration, default values will be used instead");
            GENERAL_LOGGER.error("", e);
        }
        this.commonInit();
        ItemGroupRegistryForge.registerItemGroups();
        MinecraftForge.EVENT_BUS.register(EntityRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(BlockItemRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(StatisticsRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(ScreenHandlerRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(RecipeRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(BlockEntityRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(SoundRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(NetworkRegistryForge.class);
        NetworkRegistryForge.registerPackets();
        LateBlockRegistryForge.addDynamicBlockRegistration(Block.class);
        LateBlockRegistryForge.addDynamicBlockRegistration(Item.class);
        PaladinFurnitureMod.isClient = FMLEnvironment.dist == Dist.CLIENT;
    }
}
