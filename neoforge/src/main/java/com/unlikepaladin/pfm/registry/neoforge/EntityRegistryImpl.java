package com.unlikepaladin.pfm.registry.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class EntityRegistryImpl {
    public static Map<EntityType<? extends LivingEntity>, AttributeSupplier.Builder> attributeMap = new HashMap<>();
    public static Map<ResourceLocation, EntityType<?>> entityTypeList = new LinkedHashMap<>() {
    };

    public static void registerAttribute(EntityType<? extends LivingEntity> entityType, AttributeSupplier.Builder builder) {
        attributeMap.put(entityType, builder);
    }

    public static void registerEntityType(String id, EntityType<?> entityType) {
        entityTypeList.put(new ResourceLocation(PaladinFurnitureMod.MOD_ID, id), entityType);
    }
}