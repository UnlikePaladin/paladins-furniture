package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.entity.ChairEntity;
import com.unlikepaladin.pfm.registry.Entities;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;

public class EntityRegistryImpl {
    public static void registerAttribute(EntityType<? extends LivingEntity> entityType, DefaultAttributeContainer.Builder builder) {
        FabricDefaultAttributeRegistry.register(entityType, builder);
    }

    public static void registerEntityType(String id, EntityType<?> entityType) {
        Registry.register(Registries.ENTITY_TYPE, new Identifier(PaladinFurnitureMod.MOD_ID, id), entityType);
    }
}