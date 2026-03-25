package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.entity.ChairEntity;
import com.unlikepaladin.pfm.entity.OfficeChairEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class Entities {
    public static final EntityType<ChairEntity> CHAIR = EntityType.Builder.of(ChairEntity::new, MobCategory.MISC)
            .sized(0.0F, 0.0F).fireImmune().noSummon().build("chair");

    public static final EntityType<OfficeChairEntity> OFFICE_CHAIR = EntityType.Builder.
            <OfficeChairEntity>of(OfficeChairEntity::new, MobCategory.MISC).sized(0.9F, 0.9F)
            .fireImmune().build("office_chair");

}
