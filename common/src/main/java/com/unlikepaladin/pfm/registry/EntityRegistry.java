package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.compat.PFMModCompatibility;
import com.unlikepaladin.pfm.entity.ChairEntity;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;

public class EntityRegistry {

    public static void registerEntityTypes() {
        registerEntityType("chair", Entities.CHAIR);
        PaladinFurnitureMod.pfmModCompatibilities.forEach(PFMModCompatibility::registerEntityTypes);
    }

    public static void registerAttributes() {
        registerAttribute(Entities.CHAIR, ChairEntity.createMobAttributes());
    }

    @ExpectPlatform
    public static void registerAttribute(EntityType<? extends LivingEntity> entityType, DefaultAttributeContainer.Builder builder) {
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static void registerEntityType(String id, EntityType<?> entityType) {
        throw new RuntimeException();
    }
}
