package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.client.PFMClientExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Minecraft.class)
public abstract class PFMMinecraftClientMixin extends ReentrantBlockableEventLoop<Runnable> implements PFMClientExtension {
    @Shadow @Final private TextureManager textureManager;

    protected PFMMinecraftClientMixin() {
        super("Client");
    }

    @Override
    public void invoke$runTasks() {
        this.runAllTasks();
    }
}
