package com.unlikepaladin.pfm.registry.forge;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityRegistryImpl {
    public static Map<EntityType<? extends LivingEntity>, DefaultAttributeContainer.Builder> attributeMap = new HashMap<>();
    public static List<EntityType<?>> entityTypeList = new ArrayList<>();

    public static void registerAttribute(EntityType<? extends LivingEntity> entityType, DefaultAttributeContainer.Builder builder) {
        attributeMap.put(entityType, builder);
    }

    public static void registerEntityType(String id, EntityType<?> entityType) {
        entityType.setRegistryName(id);
        entityTypeList.add(entityType);
    }
}