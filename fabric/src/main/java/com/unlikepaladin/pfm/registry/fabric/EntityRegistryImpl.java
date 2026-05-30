package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.entity.ChairEntity;
import com.unlikepaladin.pfm.registry.Entities;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.resources.Identifier;
import net.minecraft.core.Registry;

public class EntityRegistryImpl {
    public static void registerAttribute(EntityType<? extends LivingEntity> entityType, AttributeSupplier.Builder builder) {
        FabricDefaultAttributeRegistry.register(entityType, builder);
    }

    public static void registerEntityType(String id, EntityType<?> entityType) {
        Registry.register(BuiltInRegistries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, id), entityType);
    }
}