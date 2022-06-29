package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.entity.ChairEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EntityRegistry {
    public static final EntityType<ChairEntity> CHAIR = FabricEntityTypeBuilder.create(SpawnGroup.MISC, ChairEntity::new).dimensions(EntityDimensions.fixed(0.0F, 0.0F)).fireImmune().disableSummon().build();
    public static void registerEntity(String name, EntityType<? extends LivingEntity> entity) {
        Registry.register(Registry.ENTITY_TYPE, new Identifier(PaladinFurnitureMod.MOD_ID, name), entity);
    }
    public static void registerEntities()
    {
        registerEntity("chair", CHAIR);
        FabricDefaultAttributeRegistry.register(CHAIR, ChairEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 0));
    }
}
