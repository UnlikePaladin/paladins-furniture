package com.unlikepaladin.pfm.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.compat.PaladinFurnitureModConfig;
import com.unlikepaladin.pfm.compat.fabric.PaladinFurnitureModConfigImpl;
import com.unlikepaladin.pfm.compat.fabric.sandwichable.PFMSandwichableRegistry;
import com.unlikepaladin.pfm.registry.*;
import com.unlikepaladin.pfm.registry.fabric.*;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.unlikepaladin.pfm.PaladinFurnitureMod.MOD_ID;

public class PaladinFurnitureModFabric  implements ModInitializer {

    public static final Identifier FURNITURE_DYED_ID = new Identifier("pfm:furniture_dyed");
    public static SoundEvent FURNITURE_DYED_EVENT = new SoundEvent(FURNITURE_DYED_ID);

    public static final Logger GENERAL_LOGGER = LogManager.getLogger();

    public static ConfigHolder<PaladinFurnitureModConfigImpl> pfmConfig;
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

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

        PaladinFurnitureMod.FURNITURE_GROUP = FabricItemGroupBuilder.build(
                new Identifier(MOD_ID, "furniture"),
                () -> new ItemStack(PaladinFurnitureModBlocksItems.OAK_CHAIR));

        EntityRegistryFabric.registerEntities();
        PaladinFurnitureMod.commonInit();
        BlockItemRegistryFabric.registerBlocks();
        if (FabricLoader.getInstance().isModLoaded("sandwichable")) {
            PFMSandwichableRegistry.register();
        }
        if (FabricLoader.getInstance().isModLoaded("cloth-config2")) {
            pfmConfig = AutoConfig.register(PaladinFurnitureModConfigImpl.class, Toml4jConfigSerializer::new);
        }
        StatisticsRegistryFabric.registerStatistics();
        SoundRegistryFabric.registerSounds();
        BlockEntityRegistryFabric.registerBlockEntities();
        NetworkRegistryFabric.registerPackets();
        ScreenHandlerRegistryFabric.registerScreenHandlers();
        RecipeRegistryFabric.registerRecipes();
    }

}
