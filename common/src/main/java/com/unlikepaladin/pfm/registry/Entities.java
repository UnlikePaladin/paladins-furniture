package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.entity.ChairEntity;
import com.unlikepaladin.pfm.entity.OfficeChairEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public class Entities {
    public static final EntityType<ChairEntity> CHAIR = EntityType.Builder.create(ChairEntity::new, SpawnGroup.MISC).setDimensions(0.0F, 0.0F).makeFireImmune().disableSummon().build("chair");
    public static final EntityType<OfficeChairEntity> OFFICE_CHAIR = EntityType.Builder.create(OfficeChairEntity::new, SpawnGroup.MISC).setDimensions(1.0F, 1.0F).makeFireImmune().build("office_chair");

}
