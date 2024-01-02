package com.unlikepaladin.pfm.registry.neoforge;

import com.unlikepaladin.pfm.registry.EntityRegistry;
import net.minecraft.registry.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.RegisterEvent;


@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegistryNeoForge {

    @SubscribeEvent
    public static void registerEntities(RegisterEvent event) {
        event.register(Registries.ENTITY_TYPE.getKey(), entityTypeRegisterHelper -> {
            EntityRegistry.registerEntityTypes();
            EntityRegistryImpl.entityTypeList.forEach(entityTypeRegisterHelper::register);
        });
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        EntityRegistry.registerAttributes();
        EntityRegistryImpl.attributeMap.forEach((entityType, builder) -> {
            event.put(entityType, builder.build());
        });
    }
}
