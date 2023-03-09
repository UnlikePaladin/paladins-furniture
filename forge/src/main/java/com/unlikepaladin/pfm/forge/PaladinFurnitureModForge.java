package com.unlikepaladin.pfm.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.screens.PFMConfigScreen;
import com.unlikepaladin.pfm.config.PaladinFurnitureModConfig;
import com.unlikepaladin.pfm.data.forge.PaladinFurnitureModDataGenForge;
import com.unlikepaladin.pfm.data.forge.TagsImpl;
import com.unlikepaladin.pfm.networking.forge.SyncConfigPacket;
import com.unlikepaladin.pfm.registry.forge.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fmlclient.ConfigGuiHandler;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

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
        ItemGroupRegistry.registerItemGroups();
        TagsImpl.TUCKABLE_BLOCKS = BlockTags.createOptional(new Identifier("pfm", "tuckable_blocks"));
        MinecraftForge.EVENT_BUS.register(EntityRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(BlockItemRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(StatisticsRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(ScreenHandlerRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(RecipeRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(BlockEntityRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(SoundRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(PaladinFurnitureModDataGenForge.class);
        MinecraftForge.EVENT_BUS.register(NetworkRegistryForge.class);
        NetworkRegistryForge.registerPackets();
        this.commonInit();
    }
}
