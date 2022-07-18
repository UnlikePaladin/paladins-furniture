package com.unlikepaladin.pfm.data;

import com.unlikepaladin.pfm.blocks.SimpleBed;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FurnitureBlock extends Material {
    private Block baseMaterial;
    private Block secondMaterial;
    private Block slab;

    private final String furnitureName;
    public FurnitureBlock(Block block, String furnitureName) {
        super(block);
        this.furnitureName = furnitureName;
    }

    public Block getSecondMaterial() {
        if (block.getTranslationKey().contains("stripped")) {
            return getStrippedSecondMaterial();
        }
        String secondMaterial = this.block.getLootTableId().getPath();
        if (secondMaterial.contains("raw")) {
            secondMaterial = secondMaterial.replace("raw_", "");
        }
        if (block.getDefaultState().getMaterial().equals(net.minecraft.block.Material.NETHER_WOOD) && (!secondMaterial.contains("stem"))) {
            secondMaterial = secondMaterial.replace("blocks/", "").replace(furnitureName, "stem");
        }
        else if (block.getDefaultState().getMaterial().equals(net.minecraft.block.Material.WOOD) && !secondMaterial.contains("log") && (!secondMaterial.contains("light_wood")  || !secondMaterial.contains("dark_wood"))) {
            secondMaterial = secondMaterial.replace("blocks/", "").replace(furnitureName, "log");
        }
        else if(secondMaterial.contains("andesite")){
            secondMaterial = "polished_andesite";
        }
        else if(secondMaterial.contains("blackstone")){
            secondMaterial = "polished_blackstone";
        }
        else if(secondMaterial.contains("calcite")){
            secondMaterial = "calcite";
        }
        else if(secondMaterial.contains("dark_concrete")){
            secondMaterial = "white_concrete";
        }
        else if(secondMaterial.contains("concrete")){
            secondMaterial = "raw_concrete";
            this.secondMaterial = Registry.BLOCK.get(new Identifier("pfm:" + secondMaterial));
            return this.secondMaterial;
        }
        else if(secondMaterial.contains("deepslate_tile")){
            secondMaterial = "deepslate_tiles";
        }
        else if(secondMaterial.contains("deepslate")){
            secondMaterial = "polished_deepslate";
        }
        else if(secondMaterial.contains("diorite")){
            secondMaterial = "polished_diorite";
        }
        else if(secondMaterial.contains("granite")){
            secondMaterial = "polished_granite";
        }
        else if(secondMaterial.contains("stone")){
            secondMaterial = "smooth_stone";
        }
        else if(secondMaterial.contains("netherite")){
            secondMaterial = "netherite_block";
        }
        else if(secondMaterial.contains("light_wood") || secondMaterial.contains("dark_wood")){
            secondMaterial = "smooth_quartz_block";
        }
        if ((secondMaterial.contains("white") || secondMaterial.contains("gray")) && furnitureName.contains("modern_stool")) {
            secondMaterial = secondMaterial.replace("white", "");
            secondMaterial = secondMaterial.replace("light_gray", "");
            secondMaterial = secondMaterial.replace("gray", "");
            secondMaterial = "stripped".concat(secondMaterial);
        }
        else {
            secondMaterial = secondMaterial.replace("blocks/", "").replace(furnitureName, "");
        }
        if (secondMaterial.contains("stemlog")) {
            secondMaterial = secondMaterial.replace("stemlog", "stem");
        }
        this.secondMaterial = Registry.BLOCK.get(new Identifier("minecraft:" + secondMaterial));
            return this.secondMaterial;
    }

    public Block getBaseMaterial() {
        if (block.getTranslationKey().contains("stripped")) {
            return getStrippedBaseMaterial();
        }
        String baseMaterial = this.block.getLootTableId().getPath();
        if(baseMaterial.contains("andesite")){
            baseMaterial = "stripped_oak_log";
        }
        else if(baseMaterial.contains("blackstone")){
            baseMaterial = "stripped_crimson_stem";
        }
        else if(baseMaterial.contains("calcite")){
            baseMaterial = "stripped_warped_stem";
        }
        else if(baseMaterial.contains("dark_concrete")){
            baseMaterial = "gray_concrete";
        }
        else if(baseMaterial.contains("concrete")){
            baseMaterial = "white_concrete";
        }
        else if(baseMaterial.contains("deepslate_tile")){
            baseMaterial = "smooth_quartz";
        }
        else if(baseMaterial.contains("deepslate")){
            baseMaterial = "dark_oak_planks";
        }
        else if(baseMaterial.contains("diorite")){
            baseMaterial = "stripped_birch_log";
        }
        else if(baseMaterial.contains("granite")){
            baseMaterial = "white_terracotta";
        }
        else if(baseMaterial.contains("stone")){
            baseMaterial = "white_concrete";
        }
        else if(baseMaterial.contains("netherite")){
            baseMaterial = "ancient_debris";
        }
        else if(baseMaterial.contains("white")){
            baseMaterial = "white_concrete";
        }
        else if(baseMaterial.contains("light_gray")){
            baseMaterial = "light_gray_concrete";
        }
        else if(baseMaterial.contains("gray")){
            baseMaterial = "gray_concrete";
        }
        else if(baseMaterial.contains("light_wood")){
            baseMaterial = "oak_planks";
        }
        else if(baseMaterial.contains("dark_wood")){
            baseMaterial = "dark_oak_planks";
        }
        else {
            if (baseMaterial.contains("log") || baseMaterial.contains("stem")) {
                baseMaterial = baseMaterial.replace("log", "");
                baseMaterial = baseMaterial.replace("stem", "");
            }
            baseMaterial = baseMaterial.replace("blocks/", "").replace(furnitureName, "planks");
        }
        this.baseMaterial =  Registry.BLOCK.get(new Identifier("minecraft:" + baseMaterial));
        return this.baseMaterial;
    }
    public Block getFroggyChairMaterial() {
        String baseMaterial = this.block.getLootTableId().getPath();
        baseMaterial = baseMaterial.replace("blocks/", "").replace(furnitureName, "");
        if (baseMaterial.matches("froggy_chair")) {
            this.baseMaterial =  Registry.BLOCK.get(new Identifier("minecraft:" + "lime_concrete"));
            return this.baseMaterial;
        }
        baseMaterial = baseMaterial.concat("_concrete");
        this.baseMaterial =  Registry.BLOCK.get(new Identifier("minecraft:" + baseMaterial));
        return this.baseMaterial;
    }
    public Block getFridgeMaterial() {
        String baseMaterial = this.block.getLootTableId().getPath();
        baseMaterial = baseMaterial.replace("blocks/", "").replace(furnitureName, "");
        if (baseMaterial.contains("iron")) {
            this.baseMaterial =  Registry.BLOCK.get(new Identifier("minecraft:" + "iron_block"));
            return this.baseMaterial;
        }
        else if (baseMaterial.contains("xbox")) {
            this.baseMaterial =  Registry.BLOCK.get(new Identifier("minecraft:" + "black_concrete"));
            return this.baseMaterial;
        }
        baseMaterial = baseMaterial.concat("concrete");
        this.baseMaterial =  Registry.BLOCK.get(new Identifier("minecraft:" + baseMaterial));
        return this.baseMaterial;
    }
    public Block getSlab(){
        String slabName = this.block.getLootTableId().getPath().replace("blocks/", "").replace(furnitureName, "slab");
        if (block.getTranslationKey().contains("stripped")) {
            slabName.replace("stripped_", "");
        }
        this.slab =  Registry.BLOCK.get(new Identifier("minecraft:" + slabName));
        return slab;
    }


    public Block getStrippedBaseMaterial() {
        String secondMaterial = this.block.getLootTableId().getPath().replace("blocks/", "");
        if (secondMaterial.contains("raw_")) {
            secondMaterial = secondMaterial.replace("raw_", "");
        }
        if (block.getDefaultState().getMaterial().equals(net.minecraft.block.Material.NETHER_WOOD) && !secondMaterial.contains("stem")) {
            secondMaterial = secondMaterial.replace(furnitureName, "stem");
        }
        else if (block.getDefaultState().getMaterial().equals(net.minecraft.block.Material.WOOD) && !secondMaterial.contains("log")) {
            secondMaterial = secondMaterial.replace(furnitureName, "log");
        }
        else {
            secondMaterial = secondMaterial.replace(furnitureName, "");
        }
        this.secondMaterial = Registry.BLOCK.get(new Identifier("minecraft:" + secondMaterial));
        return this.secondMaterial;
    }

    public Block getStrippedSecondMaterial() {
        String baseMaterial = this.block.getLootTableId().getPath().replace("blocks/", "");
        if (baseMaterial.contains("log") || baseMaterial.contains("stem")) {
            baseMaterial = baseMaterial.replace("log", "");
            baseMaterial = baseMaterial.replace("stem", "");
        }
        if (baseMaterial.contains("raw_")) {
            baseMaterial = baseMaterial.replace("raw_", "");
        }
        baseMaterial = baseMaterial.replace("stripped_", "").replace(furnitureName, "planks");
        this.baseMaterial =  Registry.BLOCK.get(new Identifier("minecraft:" + baseMaterial));
        return this.baseMaterial;
    }

    public Block getArmChairMaterial() {
        String baseMaterial = this.block.getLootTableId().getPath();
        baseMaterial = baseMaterial.replace("blocks/", "").replace(furnitureName, "");
        if (baseMaterial.contains("leather")) {
            this.baseMaterial =  Registry.BLOCK.get(new Identifier("pfm:" + "leather_block"));
            return this.baseMaterial;
        }
        else if (baseMaterial.contains("standard")) {
            this.baseMaterial =  Registry.BLOCK.get(new Identifier("minecraft:" + "white_wool"));
            return this.baseMaterial;
        }
        baseMaterial = baseMaterial.concat("wool");
        this.baseMaterial =  Registry.BLOCK.get(new Identifier("minecraft:" + baseMaterial));
        return this.baseMaterial;
    }

    public Block getBed() {
        if (block instanceof SimpleBed){
            String color = ((SimpleBed) block).getColor().getName();
            return Registry.BLOCK.get(new Identifier("minecraft:" + color + "_bed"));
        }
        return null;
    }

    public Block getFence() {
        String baseMaterial = this.block.getLootTableId().getPath();
            if (baseMaterial.contains("log") || baseMaterial.contains("stem")) {
                baseMaterial = baseMaterial.replace("log", "");
                baseMaterial = baseMaterial.replace("stem", "");
            }
        baseMaterial = baseMaterial.replace("blocks/", "").replace(furnitureName, "fence");
        this.baseMaterial =  Registry.BLOCK.get(new Identifier("minecraft:" + baseMaterial));
        return this.baseMaterial;
    }
}
