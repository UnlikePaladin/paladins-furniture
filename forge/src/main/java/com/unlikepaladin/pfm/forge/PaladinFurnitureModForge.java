package com.unlikepaladin.pfm.forge;

import com.unlikepaladin.pfm.registry.forge.BlockItemRegistryForge;
import net.examplemod.ExampleMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(ExampleMod.MOD_ID)
public class PaladinFurnitureModForge {
    public PaladinFurnitureModForge() {
        // Submit our event bus to let architectury register our content on the right time
        //EventBuses.registerModEventBus(ExampleMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        //ExampleMod.init();
        MinecraftForge.EVENT_BUS.register(BlockItemRegistryForge.class);
    }
}
