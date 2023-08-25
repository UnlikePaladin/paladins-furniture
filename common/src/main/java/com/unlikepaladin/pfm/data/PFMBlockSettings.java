package com.unlikepaladin.pfm.data;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.AbstractBlock.Settings;

public class PFMBlockSettings {
    @ExpectPlatform
    public static Settings breaksWithTool(Settings settings, ToolType type) {
      throw new AssertionError();
    };
}
