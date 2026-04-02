package com.unlikepaladin.pfm.neoforge;

import com.google.common.base.Suppliers;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.PathPackRPWrapper;
import com.unlikepaladin.pfm.client.neoforge.ColorRegistryNeoForge;
import com.unlikepaladin.pfm.config.PaladinFurnitureModConfig;
import com.unlikepaladin.pfm.registry.dynamic.neoforge.LateBlockRegistryNeoForge;
import com.unlikepaladin.pfm.registry.neoforge.*;
import com.unlikepaladin.pfm.runtime.PFMDataGenerator;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.SharedConstants;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.flag.FeatureFlags;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;


import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Mod(PaladinFurnitureMod.MOD_ID)
public class PaladinFurnitureModNeoForge extends PaladinFurnitureMod {
    public static PaladinFurnitureModConfig pfmConfig;
    public PaladinFurnitureModNeoForge() {
        pfmConfig = new PaladinFurnitureModConfig(FMLPaths.CONFIGDIR.get());
        try {
            pfmConfig.initialize();
        } catch (IOException e) {
            GENERAL_LOGGER.error("Failed to initialize Paladin's Furniture configuration, default values will be used instead");
            GENERAL_LOGGER.error("", e);
        }
        this.commonInit();
        NeoForge.EVENT_BUS.register(EntityRegistryNeoForge.class);
        NeoForge.EVENT_BUS.register(BlockItemRegistryNeoForge.class);
        NeoForge.EVENT_BUS.register(StatisticsRegistryNeoForge.class);
        NeoForge.EVENT_BUS.register(ScreenHandlerRegistryNeoForge.class);
        NeoForge.EVENT_BUS.register(RecipeRegistryNeoForge.class);
        NeoForge.EVENT_BUS.register(BlockEntityRegistryNeoForge.class);
        NeoForge.EVENT_BUS.register(SoundRegistryNeoForge.class);
        NeoForge.EVENT_BUS.register(NetworkRegistryNeoForge.class);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.LOW, ColorRegistryNeoForge::registerBlockColors);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.LOWEST, ColorRegistryNeoForge::registerItemColors);
        NetworkRegistryNeoForge.registerPackets();
        LateBlockRegistryNeoForge.addDynamicBlockRegistration();
        PaladinFurnitureMod.isClientSide = FMLEnvironment.dist == Dist.CLIENT;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ItemGroupRegistryNeoForge::registerItemGroups);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ItemGroupRegistryNeoForge::addToVanillaItemGroups);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(PaladinFurnitureModNeoForge::generateResources);

    }

    @SubscribeEvent
    public static void generateResources(AddPackFindersEvent event) {
        int data = SharedConstants.getCurrentVersion().getPackVersion(PackType.SERVER_DATA);
        int resource = SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES);
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            PackMetadataSection packResourceMetadata = new PackMetadataSection(Component.literal("Runtime Generated Assets for PFM"), SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES), Optional.empty());
            Pack.ResourcesSupplier packFactory = new Pack.ResourcesSupplier() {
                @Override
                public PackResources openPrimary(String name) {
                    return new PathPackRPWrapper(Suppliers.memoize(() -> {
                        if (!PFMDataGenerator.areAssetsRunning())
                            PFMRuntimeResources.prepareAndRunAssetGen(false);
                        return PFMRuntimeResources.ASSETS_PACK;}), packResourceMetadata);
                }

                @Override
                public PackResources openFull(String name, Pack.Info metadata) {
                    return this.openPrimary(name);
                }
            };
            Pack.Info metadata = new Pack.Info(Component.literal("Runtime Generated Assets for PFM"), data, resource, PackCompatibility.COMPATIBLE, FeatureFlags.DEFAULT_FLAGS, List.of(), false);
            event.addRepositorySource(profileAdder -> {
                profileAdder.accept(Pack.create("pfm-asset-resources", Component.literal("PFM Assets"), true,  packFactory, metadata, Pack.Position.BOTTOM, false, PackSource.DEFAULT));
            });
        } else if (event.getPackType() == PackType.SERVER_DATA) {
            PackMetadataSection packResourceMetadata = new PackMetadataSection(Component.literal("Runtime Generated Data for PFM"), SharedConstants.getCurrentVersion().getPackVersion(PackType.SERVER_DATA), Optional.empty());
            Pack.ResourcesSupplier packFactory = new Pack.ResourcesSupplier() {
                @Override
                public PackResources openPrimary(String name) {
                    return new PathPackRPWrapper(Suppliers.memoize(() -> {
                        if (!PFMDataGenerator.isDataRunning())
                            PFMRuntimeResources.prepareAndRunDataGen(false);
                        return PFMRuntimeResources.DATA_PACK;}), packResourceMetadata);
                }

                @Override
                public PackResources openFull(String name, Pack.Info metadata) {
                    return this.openPrimary(name);
                }
            };
            Pack.Info metadata = new Pack.Info(Component.literal("Runtime Generated Data for PFM"), data, resource, PackCompatibility.COMPATIBLE, FeatureFlags.DEFAULT_FLAGS, List.of(), false);
            event.addRepositorySource(profileAdder -> {
                profileAdder.accept(Pack.create("pfm-data-resources", Component.literal("PFM Data"), true,  packFactory, metadata, Pack.Position.BOTTOM, false, PackSource.DEFAULT));
            });
        }
    }
}
