package com.unlikepaladin.pfm.mixin;

import com.mojang.blaze3d.TracyFrameCapture;
import com.mojang.blaze3d.platform.FramerateLimitTracker;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Queue;

@Mixin(Minecraft.class)
public interface PFMMinecraftClientAcccessor {
    @Nullable
    @Accessor("tracyFrameCapture")
    TracyFrameCapture getFrameCapturer();

    @Accessor("progressTasks")
    Queue<Runnable> getRenderTasks();

    @Mutable
    @Accessor("framerateLimitTracker")
    void setInactivityFpsLimiter(FramerateLimitTracker inactivityFpsLimiter);
}
