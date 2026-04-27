package com.unlikepaladin.pfm.data;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class PFMBlockSettings {
    @ExpectPlatform
    public static BlockBehaviour.Properties breaksWithTool(BlockBehaviour.Properties settings, ToolType type) {
      throw new AssertionError();
    };
}
