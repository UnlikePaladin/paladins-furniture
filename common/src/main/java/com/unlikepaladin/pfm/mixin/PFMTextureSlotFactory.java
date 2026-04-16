package com.unlikepaladin.pfm.mixin;

import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Set;

@Mixin(TextureSlot.class)
public interface PFMTextureSlotFactory {
    @Invoker("<init>")
    static TextureSlot newTextureKey(String name, @Nullable TextureSlot parent){
        throw new AssertionError();
    }
}
