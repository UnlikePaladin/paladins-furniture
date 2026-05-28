package com.unlikepaladin.pfm.entity.render.state;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

public class OfficeChairEntityRenderState extends LivingEntityRenderState {
    public float invulnerableTime;
    public float wheelSpinAngle;
    public DyeColor color;
    public Vec3 velocity;
    public float maxHealth;
    public float health;
    public Level world;
    public Vec3 pos;
    public RandomSource random;
    public boolean isDarkenedDim;
}
