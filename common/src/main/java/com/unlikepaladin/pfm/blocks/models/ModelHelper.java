package com.unlikepaladin.pfm.blocks.models;

import com.unlikepaladin.pfm.blocks.DyeableFurniture;
import com.unlikepaladin.pfm.blocks.materials.BlockType;
import com.unlikepaladin.pfm.blocks.materials.MaterialEnum;
import com.unlikepaladin.pfm.blocks.materials.StoneVariant;
import com.unlikepaladin.pfm.blocks.materials.WoodVariant;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModelHelper {
    public static boolean containsIdentifier(Identifier[] modelIds, Identifier comparison) {
        AtomicBoolean contains = new AtomicBoolean(false);
        Arrays.stream(modelIds).forEach(identifier -> {
            if (comparison.getPath().equals(identifier.getPath())){
                contains.set(true);
            }
        });
        return contains.get();
    }

    public static BlockType getBlockType(Identifier identifier) {
        if (identifier.getPath().contains("stripped_")) {
            return BlockType.STRIPPED_LOG;
        }
        for (WoodVariant variant : WoodVariant.values()) {
            if (identifier.getPath().contains(variant.asString())) {
                return BlockType.PLANKS;
            }
        }
        return BlockType.BLOCK;
    }

    public static MaterialEnum getVariant(Identifier identifier) {
        MaterialEnum var = getStoneType(identifier);
        if (var == StoneVariant.STONE && !identifier.getPath().contains("stone")) {
            var = getWoodType(identifier);
        }
        return var;
    }
    public static StoneVariant getStoneType(Identifier identifier) {
        for (StoneVariant variant:
             StoneVariant.values()) {
            if (identifier.getPath().contains(variant.asString())) {
                return variant;
            }
        }
        return StoneVariant.STONE;
    }
    public static WoodVariant getWoodType(Identifier identifier){
        if (identifier.getPath().contains("dark_oak")) {
            return WoodVariant.DARK_OAK;
        }
        else if (identifier.getPath().contains("oak")) {
            return WoodVariant.OAK;
        }
        else if (identifier.getPath().contains("birch")) {
            return WoodVariant.BIRCH;
        }
        else if (identifier.getPath().contains("acacia")) {
            return WoodVariant.ACACIA;
        }
        else if (identifier.getPath().contains("jungle")) {
            return WoodVariant.JUNGLE;
        }
        else if (identifier.getPath().contains("spruce")) {
            return WoodVariant.SPRUCE;
        }
        else if (identifier.getPath().contains("crimson")) {
            return WoodVariant.CRIMSON;
        }
        else if (identifier.getPath().contains("warped")) {
            return WoodVariant.WARPED;
        }
        return WoodVariant.OAK;
    }

    public static DyeColor getColor(Identifier identifier) {
        if (Registry.BLOCK.get(identifier) instanceof DyeableFurniture block) {
            return block.getPFMColor();
        }
        for (DyeColor color : DyeColor.values()) {
            if (identifier.getPath().contains(color.getName())){
                if (identifier.getPath().contains("light") && !color.getName().contains("light"))  {
                    continue;
                }
                return color;
            }
        }
        return DyeColor.RED;
    }
}
