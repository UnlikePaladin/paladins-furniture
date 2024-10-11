package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.entity.ChairEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public class Entities {
    public static final EntityType<ChairEntity> CHAIR = EntityType.Builder.create(ChairEntity::new, SpawnGroup.MISC).dimensions(0.0F, 0.0F).makeFireImmune().disableSummon().build("chair");

}
