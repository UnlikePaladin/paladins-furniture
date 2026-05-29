package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.registry.EntityRegistry;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.BOTH)
public class EntityRegistryForge {

    @SubscribeEvent
    public static void registerEntities(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.ENTITY_TYPES, entityTypeRegisterHelper -> {
            EntityRegistry.registerEntityTypes();
            EntityRegistryImpl.entityTypeList.forEach(entityTypeRegisterHelper::register);
        });
    }

    static boolean fired = false;
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        if (fired) return;

        EntityRegistry.registerAttributes();
        fired = true;
        EntityRegistryImpl.attributeMap.forEach((entityType, builder) -> {
            event.put(entityType, builder.build());
        });
    }
}
