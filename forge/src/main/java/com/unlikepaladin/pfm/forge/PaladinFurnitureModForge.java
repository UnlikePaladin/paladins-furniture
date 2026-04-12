package com.unlikepaladin.pfm.forge;

import com.google.common.base.Suppliers;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.PathPackRPWrapper;
import com.unlikepaladin.pfm.client.forge.ColorRegistryForge;
import com.unlikepaladin.pfm.config.PaladinFurnitureModConfig;
import com.unlikepaladin.pfm.data.forge.PFMTagsImpl;
import com.unlikepaladin.pfm.items.forge.PFMComponentsImpl;
import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import com.unlikepaladin.pfm.registry.dynamic.forge.LateBlockRegistryForge;
import com.unlikepaladin.pfm.registry.forge.*;
import com.unlikepaladin.pfm.runtime.PFMDataGenerator;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import com.unlikepaladin.pfm.utilities.Version;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.KnownPack;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.flag.FeatureFlags;
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
import java.util.List;
import java.util.Optional;


@Mod(PaladinFurnitureMod.MOD_ID)
public class PaladinFurnitureModForge extends PaladinFurnitureMod {
    public static PaladinFurnitureModConfig pfmConfig;
    public PaladinFurnitureModForge(FMLJavaModLoadingContext loadContext) {
        pfmConfig = new PaladinFurnitureModConfig(FMLPaths.CONFIGDIR.get());
        PaladinFurnitureMod.isClientSide = FMLEnvironment.dist == Dist.CLIENT;
        try {
            pfmConfig.initialize();
        } catch (IOException e) {
            GENERAL_LOGGER.error("Failed to initialize Paladin's Furniture configuration, default values will be used instead");
            GENERAL_LOGGER.error("", e);
        }
        this.commonInit();
        MinecraftForge.EVENT_BUS.register(EntityRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(BlockItemRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(StatisticsRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(ScreenHandlerRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(RecipeRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(BlockEntityRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(SoundRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(NetworkRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(PFMComponentsImpl.class);
        if (isClientSide) {
            loadContext.getModEventBus().addListener(EventPriority.LOW, ColorRegistryForge::registerBlockColors);
            loadContext.getModEventBus().addListener(EventPriority.LOWEST, ColorRegistryForge::registerItemColors);
        }
        NetworkRegistryForge.registerPackets();
        LateBlockRegistryForge.addDynamicBlockRegistration(loadContext);
        loadContext.getModEventBus().addListener(ItemGroupRegistryForge::registerItemGroups);
        loadContext.getModEventBus().addListener(ItemGroupRegistryForge::addToVanillaItemGroups);
        loadContext.getModEventBus().addListener(PaladinFurnitureModForge::generateResources);
    }

    @SubscribeEvent
    public static void generateResources(AddPackFindersEvent event) {
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
