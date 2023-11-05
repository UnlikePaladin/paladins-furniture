package com.unlikepaladin.pfm.forge;

import com.google.common.base.Suppliers;
import com.mojang.bridge.game.PackType;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.PathPackRPWrapper;
import com.unlikepaladin.pfm.config.PaladinFurnitureModConfig;
import com.unlikepaladin.pfm.registry.dynamic.forge.LateBlockRegistryForge;
import com.unlikepaladin.pfm.registry.forge.*;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.SharedConstants;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.metadata.PackResourceMetadata;
import net.minecraft.text.Text;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
        LateBlockRegistryForge.addDynamicBlockRegistration();
        PaladinFurnitureMod.isClient = FMLEnvironment.dist == Dist.CLIENT;
    }

    @SubscribeEvent
    public static void generateResources(AddPackFindersEvent event) {
        PackResourceMetadata packResourceMetadata = new PackResourceMetadata(Text.literal("pfm-runtime-resources"), SharedConstants.getGameVersion().getPackVersion(PackType.RESOURCE));
        event.addRepositorySource((profileAdder, factory) -> profileAdder.accept(factory.create("pfm-resources", Text.literal("PFM Resources"), true,
                () -> new PathPackRPWrapper(Suppliers.memoize(() -> {
                    PFMRuntimeResources.prepareAndRunResourceGen(false); return PFMRuntimeResources.ASSETS_PACK;}), packResourceMetadata)
                , packResourceMetadata, ResourcePackProfile.InsertionPosition.BOTTOM, ResourcePackSource.PACK_SOURCE_NONE, true)));
    }
}
