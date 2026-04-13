package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.entity.ChairEntity;
import com.unlikepaladin.pfm.registry.Entities;
import com.unlikepaladin.pfm.registry.EntityRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;

public class EntityRegistryFabric {
    public static void registerEntity(String name, EntityType<? extends Entity> entity) {
        Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, name), entity);
    }
    public static void registerEntities()
    {
        EntityRegistry.registerEntityTypes();
        EntityRegistry.registerAttributes();
    }
}
