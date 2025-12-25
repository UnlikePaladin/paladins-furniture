package com.unlikepaladin.pfm.entity.render.state;

import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class OfficeChairEntityRenderState extends LivingEntityRenderState {
    public float timeUntilRegen;
    public float wheelSpinAngle;
    public DyeColor color;
    public Vec3d velocity;
    public float maxHealth;
    public float health;
    public World world;
    public Vec3d pos;
    public Random random;
    public boolean isDarkenedDim;
}
