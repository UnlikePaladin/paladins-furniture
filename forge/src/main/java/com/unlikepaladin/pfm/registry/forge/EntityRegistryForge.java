package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.registry.EntityRegistry;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegistryForge {

    @SubscribeEvent
    public static void registerEntities(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.ENTITY_TYPES, entityTypeRegisterHelper -> {
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
