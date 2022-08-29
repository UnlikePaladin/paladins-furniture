package com.unlikepaladin.pfm.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.compat.PaladinFurnitureModConfig;
import com.unlikepaladin.pfm.compat.forge.PaladinFurnitureModConfigImpl;
import com.unlikepaladin.pfm.data.forge.TagsImpl;
import com.unlikepaladin.pfm.registry.forge.*;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmlclient.ConfigGuiHandler;
import net.minecraftforge.fmllegacy.network.FMLNetworkConstants;

import static com.unlikepaladin.pfm.PaladinFurnitureMod.FURNITURE_GROUP;
import static com.unlikepaladin.pfm.PaladinFurnitureMod.MOD_ID;


//TODO: Add Recipes and Data gen for tags on Forge
@Mod(PaladinFurnitureMod.MOD_ID)
public class PaladinFurnitureModForge {
    public static ConfigHolder<PaladinFurnitureModConfigImpl> pfmConfig;
    public PaladinFurnitureModForge() {
        ItemGroupRegistry.registerItemGroups();
        TagsImpl.TUCKABLE_BLOCKS = BlockTags.createOptional(new Identifier("pfm", "tuckable_blocks"));
        MinecraftForge.EVENT_BUS.register(EntityRegistryForge.class);
        PaladinFurnitureMod.commonInit();
        MinecraftForge.EVENT_BUS.register(BlockItemRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(StatisticsRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(ScreenHandlerRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(RecipeRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(BlockEntityRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(SoundRegistryForge.class);
        NetworkRegistryForge.registerPackets();
        if (ModList.get().isLoaded("cloth_config")) {
            pfmConfig = AutoConfig.register(PaladinFurnitureModConfigImpl.class, Toml4jConfigSerializer::new);
            ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class,
                    () -> new ConfigGuiHandler.ConfigGuiFactory(
                            (client, parent) -> AutoConfig.getConfigScreen(PaladinFurnitureModConfigImpl.class, parent).get()));
        }
    }

}
