package com.unlikepaladin.pfm.data;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Material;
import net.minecraft.util.StringIdentifiable;

public enum ToolType implements StringIdentifiable {
    AXE("axe"),
    HOE("hoe"),
    PICKAXE("pickaxe"),
    SHOVEL("shovel"),
    SWORD("sword"),
    NONE("none");
    private final String name;
    ToolType(String toolName) {
        this.name = toolName;
    }
    @Override
    public String asString() {
        return name;
    }

    public static ToolType getToolTypeFromMaterial(Material material) {
        if (material == Material.WOOD || material.isBurnable() || material == Material.NETHER_WOOD || material == Material.BAMBOO)
            return AXE;
        else if (material == Material.AGGREGATE || material == Material.SOIL)
            return HOE;
        else if (material == Material.STONE || material == Material.METAL)
            return PICKAXE;
        else
            return NONE;
    }
}
