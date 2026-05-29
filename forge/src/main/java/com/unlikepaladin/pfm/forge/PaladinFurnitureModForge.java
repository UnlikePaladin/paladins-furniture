package com.unlikepaladin.pfm.forge;

import com.google.common.base.Suppliers;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.PathPackRPWrapper;
import com.unlikepaladin.pfm.client.forge.ColorRegistryForge;
import com.unlikepaladin.pfm.client.forge.ItemModelRegistry;
import com.unlikepaladin.pfm.client.forge.PaladinFurnitureModClientForge;
import com.unlikepaladin.pfm.config.PaladinFurnitureModConfig;
import com.unlikepaladin.pfm.items.forge.PFMComponentsImpl;
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
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.util.InclusiveRange;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CreateSpecialBlockRendererEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.eventbus.api.listener.Priority;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.RegisterEvent;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Optional;


@Mod(PaladinFurnitureMod.MOD_ID)
public class PaladinFurnitureModForge extends PaladinFurnitureMod {
    public static PaladinFurnitureModConfig pfmConfig;
    public PaladinFurnitureModForge(FMLJavaModLoadingContext loadContext) {
        pfmConfig = new PaladinFurnitureModConfig(FMLPaths.CONFIGDIR.get());
        PaladinFurnitureMod.isClient = FMLEnvironment.dist == Dist.CLIENT;
        try {
            pfmConfig.initialize();
        } catch (IOException e) {
            GENERAL_LOGGER.error("Failed to initialize Paladin's Furniture configuration, default values will be used instead");
            GENERAL_LOGGER.error("", e);
        }
        BusGroup modBusGroup = loadContext.getModBusGroup();
        this.commonInit();
        BusGroup.DEFAULT.register(MethodHandles.lookup(), EntityRegistryForge.class);
        BusGroup.DEFAULT.register(MethodHandles.lookup(), BlockItemRegistryForge.class);
        RegisterEvent.getBus(modBusGroup).addListener(StatisticsRegistryForge::registerStatistics);
        RegisterEvent.getBus(modBusGroup).addListener(ScreenHandlerRegistryForge::registerScreenHandlers);
        BusGroup.DEFAULT.register(MethodHandles.lookup(), RecipeRegistryForge.class);
        RegisterEvent.getBus(modBusGroup).addListener(BlockEntityRegistryForge::registerEntities);
        RegisterEvent.getBus(modBusGroup).addListener(SoundRegistryForge::registerSounds);
        BusGroup.DEFAULT.register(MethodHandles.lookup(), NetworkRegistryForge.class);
        RegisterEvent.getBus(modBusGroup).addListener(PFMComponentsImpl::registerComponents);
        if (isClient) {
            ItemModelRegistry.registerItemModelTypes();
            var blockColorsBus = RegisterColorHandlersEvent.Block.getBus(modBusGroup);
            blockColorsBus.addListener(Priority.LOW, ColorRegistryForge::registerBlockColors);
            CreateSpecialBlockRendererEvent.BUS.addListener(ItemModelRegistry::registerSpecialModelRenderers);
            PaladinFurnitureModClientForge.registerCustomModels();
        }
        NetworkRegistryForge.registerPackets();
        LateBlockRegistryForge.addDynamicBlockRegistration(loadContext);
        RegisterEvent.getBus(modBusGroup).addListener(ItemGroupRegistryForge::registerItemGroups);
        BuildCreativeModeTabContentsEvent.getBus(modBusGroup).addListener(ItemGroupRegistryForge::addToVanillaItemGroups);
        AddPackFindersEvent.getBus(modBusGroup).addListener(PaladinFurnitureModForge::generateResources);
    }

    public static void generateResources(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            PackMetadataSection packResourceMetadata = new PackMetadataSection(Component.literal("Runtime Generated Assets for PFM"), new InclusiveRange<>(SharedConstants.getCurrentVersion().packVersion(PackType.CLIENT_RESOURCES)));
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
            PackMetadataSection packResourceMetadata = new PackMetadataSection(Component.literal("Runtime Generated Data for PFM"), new InclusiveRange<>(SharedConstants.getCurrentVersion().packVersion(PackType.SERVER_DATA)));
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
