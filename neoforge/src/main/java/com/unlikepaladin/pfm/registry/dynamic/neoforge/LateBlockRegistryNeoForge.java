package com.unlikepaladin.pfm.registry.dynamic.neoforge;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.data.materials.DynamicBlockRegistry;
import com.unlikepaladin.pfm.mixin.neoforge.MappedRegistryAccessor;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LateBlockRegistryNeoForge {
    private static boolean hasRegisteredBlockSets = false;
    private static Pair<List<Runnable>, List<Consumer<Registry<Item>>>> LATE_REGISTRATION_QUEUE = null;

    public static void addDynamicBlockRegistration() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        if (LATE_REGISTRATION_QUEUE == null) {
            LATE_REGISTRATION_QUEUE = Pair.of(new ArrayList<>(), new ArrayList<>());
            bus.addListener(EventPriority.HIGHEST, LateBlockRegistryNeoForge::registerLateBlockAndItems);
        }
        Consumer<RegisterEvent> eventConsumer = registerEvent ->  {
            if (registerEvent.getRegistryKey().equals(BuiltInRegistries.BLOCK.key())) {
                Runnable blockEvent = () -> {
                    Registry<?> registry = registerEvent.getRegistry();
                    if (registry instanceof MappedRegistry<?> fr) {
                        boolean frozen = ((MappedRegistryAccessor)fr).isFrozen();
                        fr.unfreeze();
                        LateBlockRegistryImpl.registerBlocks((Registry<Block>) registry);
                        if (frozen) fr.freeze();
                    }
                };
                LATE_REGISTRATION_QUEUE.getFirst().add(blockEvent);
            }
        };
        Consumer<Registry<Item>> itemEvent = LateBlockRegistryImpl::registerItems;
        LATE_REGISTRATION_QUEUE.getSecond().add(itemEvent);

        bus.addListener(EventPriority.HIGHEST, eventConsumer);
    }

    public static void registerLateBlockAndItems(RegisterEvent event) {
        if (!event.getRegistryKey().equals(BuiltInRegistries.ITEM.key()))
            return;
        if (!hasRegisteredBlockSets) {
            DynamicBlockRegistry.initialize();
            hasRegisteredBlockSets = true;
        }
        Registry<Item> forgeRegistry = (Registry<Item>) event.getRegistry();
        LATE_REGISTRATION_QUEUE.getFirst().forEach(Runnable::run);
        LATE_REGISTRATION_QUEUE.getSecond().forEach(registerConsumer -> registerConsumer.accept(forgeRegistry));
        LATE_REGISTRATION_QUEUE.getFirst().clear();
        LATE_REGISTRATION_QUEUE.getSecond().clear();
    }
}
