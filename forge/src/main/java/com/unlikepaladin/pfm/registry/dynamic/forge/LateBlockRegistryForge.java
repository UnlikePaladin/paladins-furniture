package com.unlikepaladin.pfm.registry.dynamic.forge;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.data.materials.DynamicBlockRegistry;
import com.unlikepaladin.pfm.registry.dynamic.LateBlockRegistry;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LateBlockRegistryForge {
    private static boolean hasRegisteredBlockSets = false;
    private static Pair<List<Runnable>, List<Consumer<RegistryEvent.Register<Item>>>> LATE_REGISTRATION_QUEUE = null;

    public static < R extends IForgeRegistryEntry<R>> void addDynamicBlockRegistration(Class<R> regType) {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        if (LATE_REGISTRATION_QUEUE == null) {
            LATE_REGISTRATION_QUEUE = Pair.of(new ArrayList<>(), new ArrayList<>());
            bus.addGenericListener(Item.class, EventPriority.HIGHEST, LateBlockRegistryForge::registerLateBlockAndItems);
        }
        Consumer<RegistryEvent.Register<R>> eventConsumer;
            if (regType == Block.class || regType == Item.class) {
                if (regType == Block.class) {
                    eventConsumer = e -> {
                        Runnable blockEvent = () -> {
                            IForgeRegistry<?> registry = e.getRegistry();
                            if (registry instanceof ForgeRegistry<?> fr) {
                                boolean frozen = fr.isLocked();
                                fr.unfreeze();
                                try {
                                    LateBlockRegistry.registerBlocks();
                                } catch (InvocationTargetException | InstantiationException |
                                         IllegalAccessException ex) {
                                    throw new RuntimeException(ex);
                                }
                                LateBlockRegistryImpl.registerBlocks((RegistryEvent.Register<Block>) e);
                                if (frozen) fr.freeze();
                            }
                        };
                        LATE_REGISTRATION_QUEUE.getFirst().add(blockEvent);
                    };
                    bus.addGenericListener(regType, EventPriority.HIGHEST, eventConsumer);
                } else {
                    Consumer<RegistryEvent.Register<Item>> itemEvent = LateBlockRegistryImpl::registerItems;
                    LATE_REGISTRATION_QUEUE.getSecond().add(itemEvent);
                }
        };
    }

    public static void registerLateBlockAndItems(RegistryEvent.Register<Item> event) {
        if (!hasRegisteredBlockSets) {
            DynamicBlockRegistry.initialize();
            hasRegisteredBlockSets = true;
        }
        LATE_REGISTRATION_QUEUE.getFirst().forEach(Runnable::run);
        LATE_REGISTRATION_QUEUE.getSecond().forEach(registerConsumer -> registerConsumer.accept(event));
        LATE_REGISTRATION_QUEUE.getFirst().clear();
        LATE_REGISTRATION_QUEUE.getSecond().clear();
    }
}
