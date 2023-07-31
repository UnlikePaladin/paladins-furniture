package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.util.Identifier;

import java.util.*;

public class EntityRegistryImpl {
    public static Map<EntityType<? extends LivingEntity>, DefaultAttributeContainer.Builder> attributeMap = new HashMap<>();
    public static Map<Identifier, EntityType<?>> entityTypeList = new LinkedHashMap<>() {
    };

    public static void registerAttribute(EntityType<? extends LivingEntity> entityType, DefaultAttributeContainer.Builder builder) {
        attributeMap.put(entityType, builder);
    }

    public static void registerEntityType(String id, EntityType<?> entityType) {
        entityTypeList.put(new Identifier(PaladinFurnitureMod.MOD_ID, id), entityType);
    }
}