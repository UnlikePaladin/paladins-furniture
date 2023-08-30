package com.unlikepaladin.pfm.fabric;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Lifecycle;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.advancements.PFMCriteria;
import com.unlikepaladin.pfm.advancements.fabric.CriteriaRegistryFabric;
import com.unlikepaladin.pfm.blocks.BasicChairBlock;
import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import com.unlikepaladin.pfm.compat.cookingforblockheads.fabric.PFMCookingForBlockHeadsCompat;
import com.unlikepaladin.pfm.config.PaladinFurnitureModConfig;
import com.unlikepaladin.pfm.config.option.AbstractConfigOption;
import com.unlikepaladin.pfm.data.materials.DynamicBlockRegistry;
import com.unlikepaladin.pfm.mixin.PFMMixinPointOfInterestTypeFactory;
import com.unlikepaladin.pfm.registry.*;
import com.unlikepaladin.pfm.registry.dynamic.LateBlockRegistry;
import com.unlikepaladin.pfm.registry.fabric.*;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.poi.PointOfInterestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class PaladinFurnitureModFabric extends PaladinFurnitureMod implements ModInitializer, DedicatedServerModInitializer {

    public static final Identifier FURNITURE_DYED_ID = new Identifier("pfm:furniture_dyed");
    public static SoundEvent FURNITURE_DYED_EVENT = new SoundEvent(FURNITURE_DYED_ID);
    public static final Logger GENERAL_LOGGER = LogManager.getLogger();


    public static PaladinFurnitureModConfig pfmConfig;
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        pfmConfig = new PaladinFurnitureModConfig(FabricLoader.getInstance().getConfigDir().resolve("pfm.properties"));
        try {
            pfmConfig.initialize();
        } catch (IOException e) {
            GENERAL_LOGGER.error("Failed to initialize Paladin's Furniture configuration, default values will be used instead");
            GENERAL_LOGGER.error("", e);
        }
        this.commonInit();

        PaladinFurnitureMod.DYE_KITS = FabricItemGroupBuilder.create(
                        new Identifier(MOD_ID, "dye_kits"))
                .icon(() -> new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_RED))
                .appendItems(stacks -> {
                    stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_RED));
                    stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_ORANGE));
                    stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_YELLOW));
                    stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_GREEN));
                    stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_LIME));
                    stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_CYAN));
                    stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_BLUE));
                    stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_LIGHT_BLUE));
                    stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_PURPLE));
                    stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_MAGENTA));
                    stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_PINK));
                    stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_BROWN));
                    stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_WHITE));
                    stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_GRAY));
                    stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_LIGHT_GRAY));
                    stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_BLACK));
                })
                .build();

        EntityRegistryFabric.registerEntities();
        PaladinFurnitureModFabric.initializeItemGroup();
        BlockItemRegistryFabric.registerItems();
        BlockItemRegistryFabric.registerBlocks();
        //PFMRuntimeResources.prepareAsyncResourceGen(); No async gen because Forge won't behave, blame it.
        PFMRuntimeResources.ready = true;
        StatisticsRegistryFabric.registerStatistics();
        SoundRegistryFabric.registerSounds();
        NetworkRegistryFabric.registerPackets();
        ScreenHandlerRegistryFabric.registerScreenHandlers();
        RecipeRegistryFabric.registerRecipes();
        ParticleTypeRegistryFabric.registerParticleTypes();
        CriteriaRegistryFabric.registerCriteria();
        ServerPlayConnectionEvents.JOIN.register(PaladinFurnitureModFabric::onServerJoin);
    }


    public static void onServerJoin(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        //Give book
        if (getPFMConfig().shouldGiveGuideBook()) {
            PFMCriteria.GUIDE_BOOK_CRITERION.trigger(handler.getPlayer());
        }

        //Sync Config
        PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
        Collection<AbstractConfigOption> configOptions = PaladinFurnitureMod.getPFMConfig().options.values();
        buffer.writeCollection(configOptions, AbstractConfigOption::writeConfigOption);
        sender.sendPacket(NetworkIDs.CONFIG_SYNC_ID, buffer);
    }

    public static void initializeItemGroup() {
        PaladinFurnitureMod.FURNITURE_GROUP = FabricItemGroupBuilder.build(
                new Identifier(MOD_ID, "furniture"),
                () -> PaladinFurnitureMod.furnitureEntryMap.get(BasicChairBlock.class).getFromVanillaWoodType(BoatEntity.Type.OAK, true).asItem().getDefaultStack());
    }

    public static void replaceHomePOI() {
        Set<BlockState> addedBedStates = PaladinFurnitureModBlocksItems.beds.stream().flatMap(block -> block.getStateManager().getStates().stream().filter(state -> state.get(SimpleBedBlock.PART) == BedPart.HEAD)).collect(ImmutableSet.toImmutableSet());
        Set<BlockState> newBedStates = new HashSet<>();
        newBedStates.addAll(PaladinFurnitureModBlocksItems.originalHomePOIBedStates);
        newBedStates.addAll(addedBedStates);
        newBedStates = newBedStates.stream().collect(ImmutableSet.toImmutableSet());
        PointOfInterestType.HOME = Registry.POINT_OF_INTEREST_TYPE.replace(OptionalInt.of(Registry.POINT_OF_INTEREST_TYPE.getRawId(PointOfInterestType.HOME)), Registry.POINT_OF_INTEREST_TYPE.getKey(PointOfInterestType.HOME).or(() -> Optional.of(RegistryKey.of(Registry.POINT_OF_INTEREST_TYPE.getKey(), new Identifier("minecraft:home")))).get(), PFMMixinPointOfInterestTypeFactory.newPoi("home", newBedStates, 1, 1), Lifecycle.stable()).value();
    }
    @Override
    public void onInitializeServer() {
        PaladinFurnitureMod.isClient = false;
        registerLateEntries();
        replaceHomePOI();
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
        if (PaladinFurnitureMod.getModList().contains("cookingforblockheads"))
            PFMCookingForBlockHeadsCompat.initBlockConnectors();
    }
}
