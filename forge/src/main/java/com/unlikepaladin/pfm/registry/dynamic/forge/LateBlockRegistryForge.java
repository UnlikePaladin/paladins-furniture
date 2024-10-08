package com.unlikepaladin.pfm.registry.dynamic.forge;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.data.materials.DynamicBlockRegistry;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.registry.dynamic.LateBlockRegistry;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LateBlockRegistryForge {
    private static boolean hasRegisteredBlockSets = false;
    private static Pair<List<Runnable>, List<Consumer<IForgeRegistry<Item>>>> LATE_REGISTRATION_QUEUE = null;

    public static void addDynamicBlockRegistration(FMLJavaModLoadingContext loadingContext) {
        IEventBus bus = loadingContext.getModEventBus();
        if (LATE_REGISTRATION_QUEUE == null) {
            LATE_REGISTRATION_QUEUE = Pair.of(new ArrayList<>(), new ArrayList<>());
            bus.addListener(EventPriority.HIGHEST, LateBlockRegistryForge::registerLateBlockAndItems);
        }
        Consumer<RegisterEvent> eventConsumer = registerEvent ->  {
            if (registerEvent.getRegistryKey().equals(ForgeRegistries.Keys.BLOCKS)) {
                Runnable blockEvent = () -> {
                    IForgeRegistry<?> registry = registerEvent.getForgeRegistry();
                    if (registry instanceof ForgeRegistry<?> fr) {
                        boolean frozen = fr.isLocked();
                        fr.unfreeze();
                        LateBlockRegistryImpl.registerBlocks((IForgeRegistry<Block>) registry);
                        if (frozen) fr.freeze();
                    }
                };
                LATE_REGISTRATION_QUEUE.getFirst().add(blockEvent);
            }
        };
        Consumer<IForgeRegistry<Item>> itemEvent = LateBlockRegistryImpl::registerItems;
        LATE_REGISTRATION_QUEUE.getSecond().add(itemEvent);

        bus.addListener(EventPriority.HIGHEST, eventConsumer);
    }

    public static void registerLateBlockAndItems(RegisterEvent event) {
        if (!event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS))
            return;
        if (!hasRegisteredBlockSets) {
            DynamicBlockRegistry.initialize();
            hasRegisteredBlockSets = true;
        }
        IForgeRegistry<Item> forgeRegistry = event.getForgeRegistry();
        LATE_REGISTRATION_QUEUE.getFirst().forEach(Runnable::run);
        LATE_REGISTRATION_QUEUE.getSecond().forEach(registerConsumer -> registerConsumer.accept(forgeRegistry));
        LATE_REGISTRATION_QUEUE.getFirst().clear();
        LATE_REGISTRATION_QUEUE.getSecond().clear();
    }
}
