package com.unlikepaladin.pfm.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.InactivityFpsLimiter;
import net.minecraft.client.util.tracy.TracyFrameCapturer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Queue;

@Mixin(MinecraftClient.class)
public interface PFMMinecraftClientAcccessor {
    @Nullable
    @Accessor("tracyFrameCapturer")
    TracyFrameCapturer getFrameCapturer();


    @Mutable
    @Accessor("inactivityFpsLimiter")
    void setInactivityFpsLimiter(InactivityFpsLimiter inactivityFpsLimiter);
}
