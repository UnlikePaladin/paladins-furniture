package com.unlikepaladin.pfm.mixin.neoforge;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(HandledScreens.class)
public interface HandledScreensAccessor {
    @Invoker("register")
    static <M extends ScreenHandler, U extends Screen & ScreenHandlerProvider<M>> void register(
            ScreenHandlerType<? extends M> type, HandledScreens.Provider<M, U> provider
    ) {
        throw new AssertionError();
    }
}
