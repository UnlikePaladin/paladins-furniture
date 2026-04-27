package com.unlikepaladin.pfm.data;


import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.material.Material;

public enum ToolType implements StringRepresentable {
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
    public String getSerializedName() {
        return name;
    }

    public static ToolType getToolTypeFromMaterial(Material material) {
        if (material == Material.WOOD || material.isFlammable() || material == Material.NETHER_WOOD || material == Material.BAMBOO)
            return AXE;
        else if (material == Material.SAND || material == Material.DIRT)
            return HOE;
        else if (material == Material.STONE || material == Material.METAL)
            return PICKAXE;
        else
            return NONE;
    }
}
