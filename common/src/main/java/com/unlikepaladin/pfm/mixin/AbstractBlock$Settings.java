package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.ducks.AbstractBlock$SettingsExtension;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Function;

@Mixin(BlockBehaviour.Properties.class)
public class AbstractBlock$Settings implements AbstractBlock$SettingsExtension {
    @Shadow
    private Function<BlockState, MaterialColor> materialColor;
    @Override
    public BlockBehaviour.Properties pfm$setMapColor(MaterialColor color) {
        materialColor = blockState -> color;
        return (BlockBehaviour.Properties)(Object)(this);
    }
}
