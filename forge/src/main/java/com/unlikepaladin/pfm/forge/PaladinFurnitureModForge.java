package com.unlikepaladin.pfm.forge;

import com.google.common.base.Suppliers;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.PathPackRPWrapper;
import com.unlikepaladin.pfm.config.PaladinFurnitureModConfig;
import com.unlikepaladin.pfm.data.forge.PFMTagsImpl;
import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import com.unlikepaladin.pfm.registry.dynamic.forge.LateBlockRegistryForge;
import com.unlikepaladin.pfm.registry.forge.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.resource.*;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.Identifier;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.SharedConstants;
import net.minecraft.resource.metadata.PackResourceMetadata;
import net.minecraft.text.Text;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
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
    public PaladinFurnitureModForge() {
        pfmConfig = new PaladinFurnitureModConfig(FMLPaths.CONFIGDIR.get().resolve("pfm.properties"));
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
        NetworkRegistryForge.registerPackets();
        LateBlockRegistryForge.addDynamicBlockRegistration();
        PaladinFurnitureMod.isClient = FMLEnvironment.dist == Dist.CLIENT;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ItemGroupRegistryForge::registerItemGroups);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ItemGroupRegistryForge::addToVanillaItemGroups);
    }

    @SubscribeEvent
    public static void generateResources(AddPackFindersEvent event) {
        PackResourceMetadata packResourceMetadata = new PackResourceMetadata(Text.literal("pfm-runtime-resources"), SharedConstants.getGameVersion().getResourceVersion(event.getPackType()), Optional.empty());
        ResourcePackProfile.PackFactory packFactory = new ResourcePackProfile.PackFactory() {
            @Override
            public ResourcePack open(String name) {
                return new PathPackRPWrapper(Suppliers.memoize(() -> {
                    PFMRuntimeResources.prepareAndRunResourceGen(false); return PFMRuntimeResources.ASSETS_PACK;}), packResourceMetadata);
            }

            @Override
            public ResourcePack openWithOverlays(String name, ResourcePackProfile.Metadata metadata) {
                return this.open(name);
            }
        };
        ResourcePackProfile.Metadata metadata = new ResourcePackProfile.Metadata(Text.literal("pfm-runtime-resources"), ResourcePackCompatibility.COMPATIBLE, FeatureFlags.DEFAULT_ENABLED_FEATURES, List.of());
        event.addRepositorySource(profileAdder -> {
            profileAdder.accept(ResourcePackProfile.of("pfm-resources", Text.literal("PFM Resources"), true,  packFactory, metadata, ResourcePackProfile.InsertionPosition.BOTTOM, true, ResourcePackSource.NONE));
        });
    }
}
