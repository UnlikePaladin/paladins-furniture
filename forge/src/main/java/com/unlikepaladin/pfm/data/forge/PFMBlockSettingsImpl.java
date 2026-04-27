package com.unlikepaladin.pfm.data.forge;

import com.unlikepaladin.pfm.data.ToolType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class PFMBlockSettingsImpl {
    public static BlockBehaviour.Properties breaksWithTool(BlockBehaviour.Properties settings, ToolType type) {
        return type != ToolType.NONE ? settings.harvestTool(getToolTag(type)) : settings;
    }

    public static net.minecraftforge.common.ToolType getToolTag(ToolType type) {
        switch (type) {
            case AXE:
                return net.minecraftforge.common.ToolType.AXE;
            case HOE:
                return net.minecraftforge.common.ToolType.HOE;
            case SHOVEL:
                return net.minecraftforge.common.ToolType.SHOVEL;
            case PICKAXE:
                return net.minecraftforge.common.ToolType.PICKAXE;
        }
        return null;
    }
}
