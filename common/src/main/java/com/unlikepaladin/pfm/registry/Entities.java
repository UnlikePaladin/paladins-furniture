package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.entity.ChairEntity;
import com.unlikepaladin.pfm.entity.OfficeChairEntity;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;

public class Entities {
    public static final EntityType<ChairEntity> CHAIR = EntityType.Builder.of(ChairEntity::new, MobCategory.MISC)
            .sized(0.0F, 0.0F).fireImmune().noSummon().build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "chair")));

    public static final EntityType<OfficeChairEntity> OFFICE_CHAIR = EntityType.Builder.
            <OfficeChairEntity>of(OfficeChairEntity::new, MobCategory.MISC).sized(0.9F, 0.9F)
            .fireImmune().build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "office_chair")));

}
