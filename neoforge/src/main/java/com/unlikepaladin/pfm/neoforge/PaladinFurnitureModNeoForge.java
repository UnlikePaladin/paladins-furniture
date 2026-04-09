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
import com.unlikepaladin.pfm.utilities.Version;
import net.minecraft.SharedConstants;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.KnownPack;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.flag.FeatureFlags;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
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
    public PaladinFurnitureModNeoForge(IEventBus modEventBus) {
        pfmConfig = new PaladinFurnitureModConfig(FMLPaths.CONFIGDIR.get());
        try {
            pfmConfig.initialize();
        } catch (IOException e) {
            GENERAL_LOGGER.error("Failed to initialize Paladin's Furniture configuration, default values will be used instead");
            GENERAL_LOGGER.error("", e);
        }
        this.commonInit();
        modEventBus.register(EntityRegistryNeoForge.class);
        modEventBus.register(BlockItemRegistryNeoForge.class);
        modEventBus.register(StatisticsRegistryNeoForge.class);
        modEventBus.register(ScreenHandlerRegistryNeoForge.class);
        modEventBus.register(RecipeRegistryNeoForge.class);
        modEventBus.register(BlockEntityRegistryNeoForge.class);
        modEventBus.register(SoundRegistryNeoForge.class);
        modEventBus.addListener(NetworkRegistryNeoForge::register);
        modEventBus.addListener(EventPriority.LOW, ColorRegistryNeoForge::registerBlockColors);
        modEventBus.addListener(EventPriority.LOWEST, ColorRegistryNeoForge::registerItemColors);
        LateBlockRegistryNeoForge.addDynamicBlockRegistration(modEventBus);
        PaladinFurnitureMod.isClientSide = FMLEnvironment.dist == Dist.CLIENT;
        NeoForge.EVENT_BUS.addListener(NetworkRegistryNeoForge::onServerJoin);
        modEventBus.addListener(ItemGroupRegistryNeoForge::registerItemGroups);
        modEventBus.addListener(ItemGroupRegistryNeoForge::addToVanillaItemGroups);
        modEventBus.addListener(PaladinFurnitureModNeoForge::generateResources);
    }

    @SubscribeEvent
    public static void generateResources(AddPackFindersEvent event) {
        int data = SharedConstants.getCurrentVersion().getPackVersion(PackType.SERVER_DATA);
        int resource = SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES);
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            PackMetadataSection packResourceMetadata = new PackMetadataSection(Component.literal("Runtime Generated Assets for PFM"), SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES), Optional.empty());
            Pack.ResourcesSupplier packFactory = new Pack.ResourcesSupplier() {
                @Override
                public PackResources openPrimary(PackLocationInfo info) {
                    return new PathPackRPWrapper(Suppliers.memoize(() -> {
                        if (!PFMDataGenerator.areAssetsRunning())
                            PFMRuntimeResources.prepareAndRunAssetGen(false);
                        return PFMRuntimeResources.getAssetsPack(info);}), packResourceMetadata, info);
                }

                @Override
                public PackResources openFull(PackLocationInfo info, Pack.Metadata metadata) {
                    return this.openPrimary(info);
                }
            };
            event.addRepositorySource(profileAdder -> {
                profileAdder.accept(Pack.readMetaAndCreate(new PackLocationInfo("pfm-asset-resources", Component.literal("PFM Assets"), PackSource.DEFAULT, Optional.of(new KnownPack(PaladinFurnitureMod.MOD_ID, "pfm_assets", Version.getCurrentVersion()))),  packFactory, PackType.CLIENT_RESOURCES, new PackSelectionConfig(true, Pack.Position.BOTTOM, false)));
            });
        } else if (event.getPackType() == PackType.SERVER_DATA) {
            PackMetadataSection packResourceMetadata = new PackMetadataSection(Component.literal("Runtime Generated Data for PFM"), SharedConstants.getCurrentVersion().getPackVersion(PackType.SERVER_DATA), Optional.empty());
            Pack.ResourcesSupplier packFactory = new Pack.ResourcesSupplier() {
                @Override
                public PackResources openPrimary(PackLocationInfo info) {
                    return new PathPackRPWrapper(Suppliers.memoize(() -> {
                        if (!PFMDataGenerator.isDataRunning())
                            PFMRuntimeResources.prepareAndRunDataGen(false);
                        return PFMRuntimeResources.getDataPack(info);}), packResourceMetadata, info);
                }

                @Override
                public PackResources openFull(PackLocationInfo info, Pack.Metadata metadata) {
                    return this.openPrimary(info);
                }
            };
            event.addRepositorySource(profileAdder -> {
                profileAdder.accept(Pack.readMetaAndCreate(new PackLocationInfo("pfm-data-resources", Component.literal("PFM Data"), PackSource.DEFAULT, Optional.of(new KnownPack(PaladinFurnitureMod.MOD_ID, "pfm_data", Version.getCurrentVersion()))),  packFactory, PackType.SERVER_DATA, new PackSelectionConfig(true, Pack.Position.BOTTOM, false)));
            });
        }
    }
}
