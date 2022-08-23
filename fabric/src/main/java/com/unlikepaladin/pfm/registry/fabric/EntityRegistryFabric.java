package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.entity.ChairEntity;
import com.unlikepaladin.pfm.registry.Entities;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EntityRegistryFabric {
    public static void registerEntity(String name, EntityType<? extends LivingEntity> entity) {
        Registry.register(Registry.ENTITY_TYPE, new Identifier(PaladinFurnitureMod.MOD_ID, name), entity);
    }
    public static void registerEntities()
    {
        registerEntity("chair", Entities.CHAIR);
        FabricDefaultAttributeRegistry.register(Entities.CHAIR, ChairEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 0));
    }
}
