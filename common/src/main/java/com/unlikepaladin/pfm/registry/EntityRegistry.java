package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.compat.PFMModCompatibility;
import com.unlikepaladin.pfm.entity.ChairEntity;
import com.unlikepaladin.pfm.entity.OfficeChairEntity;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class EntityRegistry {

    public static void registerEntityTypes() {
        registerEntityType("chair", Entities.CHAIR);
        registerEntityType("office_chair", Entities.OFFICE_CHAIR);
        PaladinFurnitureMod.pfmModCompatibilities.forEach(PFMModCompatibility::registerEntityTypes);
    }

    public static void registerAttributes() {
        registerAttribute(Entities.CHAIR, ChairEntity.createMobAttributes());
        registerAttribute(Entities.OFFICE_CHAIR, OfficeChairEntity.createMobAttributes());
    }

    @ExpectPlatform
    public static void registerAttribute(EntityType<? extends LivingEntity> entityType, AttributeSupplier.Builder builder) {
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static void registerEntityType(String id, EntityType<?> entityType) {
        throw new RuntimeException();
    }
}
