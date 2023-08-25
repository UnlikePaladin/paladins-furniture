package com.unlikepaladin.pfm.data.fabric;

import com.unlikepaladin.pfm.data.ToolType;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;

public class PFMBlockSettingsImpl {
    public static AbstractBlock.Settings breaksWithTool(AbstractBlock.Settings settings, ToolType type) {
        return type != ToolType.NONE ? FabricBlockSettings.copyOf(settings).breakByTool(getToolTag(type)) : settings;
    }

    public static Tag<Item> getToolTag(ToolType type) {
        switch (type) {
            case AXE:
                return FabricToolTags.AXES;
            case HOE:
                return FabricToolTags.HOES;
            case SHOVEL:
                return FabricToolTags.SHOVELS;
            case PICKAXE:
                return FabricToolTags.PICKAXES;
            case SWORD:
                return FabricToolTags.SWORDS;
        }
        return null;
    }
}
