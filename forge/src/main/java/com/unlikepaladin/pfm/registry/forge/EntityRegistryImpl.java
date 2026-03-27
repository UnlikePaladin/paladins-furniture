package com.unlikepaladin.pfm.registry.forge;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityRegistryImpl {
    public static Map<EntityType<? extends LivingEntity>, AttributeSupplier.Builder> attributeMap = new HashMap<>();
    public static List<EntityType<?>> entityTypeList = new ArrayList<>();

    public static void registerAttribute(EntityType<? extends LivingEntity> entityType, AttributeSupplier.Builder builder) {
        attributeMap.put(entityType, builder);
    }

    public static void registerEntityType(String id, EntityType<?> entityType) {
        entityType.setRegistryName(id);
        entityTypeList.add(entityType);
    }
}