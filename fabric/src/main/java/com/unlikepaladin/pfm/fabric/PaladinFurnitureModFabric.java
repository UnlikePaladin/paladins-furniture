package com.unlikepaladin.pfm.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.advancements.PFMCriteria;
import com.unlikepaladin.pfm.advancements.fabric.CriteriaRegistryFabric;
import com.unlikepaladin.pfm.blocks.BasicChairBlock;
import com.unlikepaladin.pfm.config.PaladinFurnitureModConfig;
import com.unlikepaladin.pfm.config.option.AbstractConfigOption;
import com.unlikepaladin.pfm.data.materials.DynamicBlockRegistry;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.items.PFMComponents;
import com.unlikepaladin.pfm.networking.SyncConfigPayload;
import com.unlikepaladin.pfm.registry.*;
import com.unlikepaladin.pfm.registry.dynamic.LateBlockRegistry;
import com.unlikepaladin.pfm.registry.fabric.*;
import com.unlikepaladin.pfm.utilities.fabric.PFMFileUtilImpl;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public class PaladinFurnitureModFabric extends PaladinFurnitureMod implements ModInitializer, DedicatedServerModInitializer {

    public static final ResourceLocation FURNITURE_DYED_ID = ResourceLocation.parse("pfm:furniture_dyed");
    public static SoundEvent FURNITURE_DYED_EVENT = SoundEvent.createVariableRangeEvent(FURNITURE_DYED_ID);
    public static final Logger GENERAL_LOGGER = LogManager.getLogger();


    public static PaladinFurnitureModConfig pfmConfig;
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        pfmConfig = new PaladinFurnitureModConfig(FabricLoader.getInstance().getConfigDir());
        try {
            pfmConfig.initialize();
        } catch (IOException e) {
            GENERAL_LOGGER.error("Failed to initialize Paladin's Furniture configuration, default values will be used instead");
            GENERAL_LOGGER.error("", e);
        }
        this.commonInit();

        PFMComponents.registerComponents();
        EntityRegistryFabric.registerEntities();
        PaladinFurnitureModFabric.initializeItemGroup();
        BlockItemRegistryFabric.registerItems();
        BlockItemRegistryFabric.registerBlocks();
        // PFMRuntimeResources.prepareAsyncResourceGen(); No async gen because Forge won't behave, blame it.
        StatisticsRegistryFabric.registerStatistics();
        SoundRegistryFabric.registerSounds();
        NetworkRegistryFabric.registerPackets();
        ScreenHandlerRegistryFabric.registerScreenHandlers();
        RecipeRegistryFabric.registerRecipes();
        ParticleTypeRegistryFabric.registerParticleTypes();
        CriteriaRegistryFabric.registerCriteria();
        ServerPlayConnectionEvents.JOIN.register(PaladinFurnitureModFabric::onServerJoin);
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            PFMFileUtilImpl.currentServer = server;
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            PFMFileUtilImpl.currentServer = null;
        });
    }


    public static void onServerJoin(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server) {
        // Give book
        if (getPFMConfig().shouldGiveGuideBook()) {
            PFMCriteria.GUIDE_BOOK_CRITERION.trigger(handler.getPlayer());
        }

        // Sync Config
        RegistryFriendlyByteBuf buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), server.registryAccess());
        Collection<AbstractConfigOption> configOptions = PaladinFurnitureMod.getPFMConfig().options.values();
        buffer.writeCollection(configOptions, AbstractConfigOption::writeConfigOption);
        sender.sendPacket(new SyncConfigPayload(buffer));
    }
//new ResourceLocation(MOD_ID, "dye_kits")
    public static void initializeItemGroup() {
        PaladinFurnitureMod.DYE_KITS.setB(Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(MOD_ID, "dye_kits"), FabricItemGroup.builder()
                .title(Component.translatable("itemGroup.pfm.dye_kits"))
                .icon(() -> new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_RED))
                .displayItems((enabledFeatures, stacks) -> {
                    stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_RED));
                    stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_ORANGE));
                    stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_YELLOW));
                    stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_GREEN));
                    stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_LIME));
                    stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_CYAN));
                    stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_BLUE));
                    stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_LIGHT_BLUE));
                    stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_PURPLE));
                    stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_MAGENTA));
                    stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_PINK));
                    stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_BROWN));
                    stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_WHITE));
                    stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_GRAY));
                    stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_LIGHT_GRAY));
                    stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_BLACK));
                })
                .build()));

        PaladinFurnitureMod.FURNITURE_GROUP.setB(Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(MOD_ID, "furniture"), FabricItemGroup.builder()
                .title(Component.translatable("itemGroup.pfm.furniture"))
                .icon(() -> PaladinFurnitureMod.furnitureEntryMap.get(BasicChairBlock.class).getVariantToBlockMap().get(WoodVariantRegistry.OAK).asItem().getDefaultInstance())
                .displayItems((displayContext, stacks) -> {
                }
                ).build()));

        PaladinFurnitureMod.BUILDING_BLOCKS.setB(BuiltInRegistries.CREATIVE_MODE_TAB.getValue(CreativeModeTabs.BUILDING_BLOCKS));
    }

    @Override
    public void onInitializeServer() {
        PaladinFurnitureMod.isClient = false;
        registerLateEntries();
        replaceHomePOIStates();
    }

    public static void registerLateEntries() {
        DynamicBlockRegistry.initialize();
        try {
            LateBlockRegistry.registerBlocks();
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        BlockEntityRegistry.registerBlockEntities();
    }
}
