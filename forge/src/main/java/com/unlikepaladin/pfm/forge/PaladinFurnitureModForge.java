package com.unlikepaladin.pfm.forge;

import com.google.common.base.Suppliers;
import com.mojang.bridge.game.PackType;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.PathPackRPWrapper;
import com.unlikepaladin.pfm.client.forge.ColorRegistryForge;
import com.unlikepaladin.pfm.config.PaladinFurnitureModConfig;
import com.unlikepaladin.pfm.registry.dynamic.forge.LateBlockRegistryForge;
import com.unlikepaladin.pfm.registry.forge.*;
import com.unlikepaladin.pfm.runtime.PFMDataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.SharedConstants;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;


@Mod(PaladinFurnitureMod.MOD_ID)
public class PaladinFurnitureModForge extends PaladinFurnitureMod {
    public static PaladinFurnitureModConfig pfmConfig;
    public PaladinFurnitureModForge() {
        pfmConfig = new PaladinFurnitureModConfig(FMLPaths.CONFIGDIR.get());
        PaladinFurnitureMod.isClientSide = FMLEnvironment.dist == Dist.CLIENT;
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
        if (isClientSide) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.LOW, ColorRegistryForge::registerBlockColors);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.LOWEST, ColorRegistryForge::registerItemColors);
        }
        NetworkRegistryForge.registerPackets();
        LateBlockRegistryForge.addDynamicBlockRegistration(Block.class);
        LateBlockRegistryForge.addDynamicBlockRegistration(Item.class);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(PaladinFurnitureModForge::generateResources);

    }

    @SubscribeEvent
    public static void generateResources(AddPackFindersEvent event) {
        if (event.getPackType() == net.minecraft.server.packs.PackType.CLIENT_RESOURCES) {
            PackMetadataSection packResourceMetadata = new PackMetadataSection(new TextComponent("Runtime Generated Assets for PFM"), SharedConstants.getCurrentVersion().getPackVersion(PackType.RESOURCE));
            event.addRepositorySource((profileAdder, factory) -> profileAdder.accept(factory.create("pfm-asset-resources", new TextComponent("PFM Assets"), true,
                    () -> new PathPackRPWrapper(Suppliers.memoize(() -> {
                        if (!PFMDataGenerator.areAssetsRunning())
                            PFMRuntimeResources.prepareAndRunAssetGen(false);
                        return PFMRuntimeResources.ASSETS_PACK;}), packResourceMetadata)
                    , packResourceMetadata, Pack.Position.BOTTOM, PackSource.DEFAULT, false)));
        } else if (event.getPackType() == net.minecraft.server.packs.PackType.SERVER_DATA) {
            PackMetadataSection packResourceMetadata = new PackMetadataSection(new TextComponent("Runtime Generated Data for PFM"), SharedConstants.getCurrentVersion().getPackVersion(PackType.DATA));
            event.addRepositorySource((profileAdder, factory) -> profileAdder.accept(factory.create("pfm-data-resources", new TextComponent("PFM Data"), true,
                    () -> new PathPackRPWrapper(Suppliers.memoize(() -> {
                        if (!PFMDataGenerator.isDataRunning())
                            PFMRuntimeResources.prepareAndRunDataGen(false);
                        return PFMRuntimeResources.DATA_PACK;}), packResourceMetadata)
                    , packResourceMetadata, Pack.Position.BOTTOM, PackSource.DEFAULT, false)));
        }
    }
}
