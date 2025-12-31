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
import com.unlikepaladin.pfm.utilities.Version;
import net.minecraft.registry.VersionedIdentifier;
import net.minecraft.resource.*;
import com.unlikepaladin.pfm.runtime.PFMDataGenerator;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.SharedConstants;
import net.minecraft.resource.metadata.PackResourceMetadata;
import net.minecraft.text.Text;
import net.minecraft.util.dynamic.Range;
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
        if (event.getPackType() == ResourceType.CLIENT_RESOURCES) {
            PackResourceMetadata packResourceMetadata = new PackResourceMetadata(Text.literal("Runtime Generated Assets for PFM"), new Range<>(SharedConstants.getGameVersion().packVersion(ResourceType.CLIENT_RESOURCES)));
            ResourcePackProfile.PackFactory packFactory = new ResourcePackProfile.PackFactory() {
                @Override
                public ResourcePack open(ResourcePackInfo info) {
                    return new PathPackRPWrapper(Suppliers.memoize(() -> {
                        if (!PFMDataGenerator.areAssetsRunning())
                            PFMRuntimeResources.prepareAndRunAssetGen(false);
                        return PFMRuntimeResources.getAssetsPack(info);}), packResourceMetadata, info);
                }

                @Override
                public ResourcePack openWithOverlays(ResourcePackInfo info, ResourcePackProfile.Metadata metadata) {
                    return this.open(info);
                }
            };
            event.addRepositorySource(profileAdder -> {
                profileAdder.accept(ResourcePackProfile.create(new ResourcePackInfo("pfm-asset-resources", Text.literal("PFM Assets"), ResourcePackSource.NONE, Optional.of(new VersionedIdentifier(PaladinFurnitureMod.MOD_ID, "pfm_assets", Version.getCurrentVersion()))),  packFactory, ResourceType.CLIENT_RESOURCES, new ResourcePackPosition(true, ResourcePackProfile.InsertionPosition.BOTTOM, false)));
            });
        } else if (event.getPackType() == ResourceType.SERVER_DATA) {
            PackResourceMetadata packResourceMetadata = new PackResourceMetadata(Text.literal("Runtime Generated Data for PFM"), new Range<>(SharedConstants.getGameVersion().packVersion(ResourceType.SERVER_DATA)));
            ResourcePackProfile.PackFactory packFactory = new ResourcePackProfile.PackFactory() {
                @Override
                public ResourcePack open(ResourcePackInfo info) {
                    return new PathPackRPWrapper(Suppliers.memoize(() -> {
                        if (!PFMDataGenerator.isDataRunning())
                            PFMRuntimeResources.prepareAndRunDataGen(false);
                        return PFMRuntimeResources.getDataPack(info);}), packResourceMetadata, info);
                }

                @Override
                public ResourcePack openWithOverlays(ResourcePackInfo info, ResourcePackProfile.Metadata metadata) {
                    return this.open(info);
                }
            };
            event.addRepositorySource(profileAdder -> {
                profileAdder.accept(ResourcePackProfile.create(new ResourcePackInfo("pfm-data-resources", Text.literal("PFM Data"), ResourcePackSource.NONE, Optional.of(new VersionedIdentifier(PaladinFurnitureMod.MOD_ID, "pfm_data", Version.getCurrentVersion()))),  packFactory, ResourceType.SERVER_DATA, new ResourcePackPosition(true, ResourcePackProfile.InsertionPosition.BOTTOM, false)));
            });
        }
    }
}
