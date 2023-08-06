package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.entity.ChairEntity;
import com.unlikepaladin.pfm.registry.Entities;
import com.unlikepaladin.pfm.registry.EntityRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;

public class EntityRegistryFabric {
    public static void registerEntity(String name, EntityType<? extends Entity> entity) {
        Registry.register(Registries.ENTITY_TYPE, new Identifier(PaladinFurnitureMod.MOD_ID, name), entity);
    }
    public static void registerEntities()
    {
        EntityRegistry.registerEntityTypes();
        EntityRegistry.registerAttributes();
    }
}
