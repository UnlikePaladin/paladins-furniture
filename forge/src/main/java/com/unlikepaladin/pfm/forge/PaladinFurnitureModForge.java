package com.unlikepaladin.pfm.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.compat.forge.PaladinFurnitureModConfigImpl;
import com.unlikepaladin.pfm.data.forge.PaladinFurnitureModDataGenForge;
import com.unlikepaladin.pfm.data.forge.TagsImpl;
import com.unlikepaladin.pfm.registry.forge.*;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;


@Mod(PaladinFurnitureMod.MOD_ID)
public class PaladinFurnitureModForge extends PaladinFurnitureMod {
    public static ConfigHolder<PaladinFurnitureModConfigImpl> pfmConfig;
    public PaladinFurnitureModForge() {
        TagsImpl.TUCKABLE_BLOCKS = BlockTags.create(new Identifier("pfm", "tuckable_blocks"));
        MinecraftForge.EVENT_BUS.register(BlockItemRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(RecipeRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(ScreenHandlerRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(StatisticsRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(SoundRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(BlockEntityRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(PaladinFurnitureModDataGenForge.class);
        NetworkRegistryForge.registerPackets();
        MinecraftForge.EVENT_BUS.register(EntityRegistryForge.class);
        if (ModList.get().isLoaded("cloth_config")) {
            pfmConfig = AutoConfig.register(PaladinFurnitureModConfigImpl.class, Toml4jConfigSerializer::new);
            ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class,
                    () -> new ConfigGuiHandler.ConfigGuiFactory(
                            (client, parent) -> AutoConfig.getConfigScreen(PaladinFurnitureModConfigImpl.class, parent).get()));
        }
        this.commonInit();
        ItemGroupRegistry.registerItemGroups();
    }

}
