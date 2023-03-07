package com.unlikepaladin.pfm.data;

import com.unlikepaladin.pfm.blocks.DyeableFurniture;
import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FurnitureBlock extends Material {
    private Item baseMaterial;
    private Item secondMaterial;
    private Block slab;

    private final String furnitureName;
    public FurnitureBlock(Block block, String furnitureName) {
        super(block);
        this.furnitureName = furnitureName;
    }

    public Item getSecondaryMaterial() {
        if (block.getTranslationKey().contains("stripped")) {
            return getStrippedSecondMaterial();
        }
        String secondMaterial = this.block.getLootTableId().getPath();
        if (secondMaterial.contains("raw")) {
            secondMaterial = secondMaterial.replace("raw_", "");
        }

        if(secondMaterial.contains("andesite")){
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
            this.secondMaterial = Registry.ITEM.get(new Identifier("pfm:" + secondMaterial));
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
        else if(secondMaterial.contains("smooth_stone")){
            secondMaterial = "smooth_stone";
        }
        else if(secondMaterial.contains("stone")){
            secondMaterial = "cobblestone";
        }
        else if(secondMaterial.contains("netherite")){
            secondMaterial = "netherite_block";
        }
        else if(secondMaterial.contains("light_wood")){
            secondMaterial = "oak_planks";
        }
        else if(secondMaterial.contains("dark_wood")){
            secondMaterial = "dark_oak_planks";
        }
        else if (secondMaterial.contains("quartz")) {
            secondMaterial = "quartz_block";
        }
        else if ((secondMaterial.contains("white") || secondMaterial.contains("gray")) && furnitureName.contains("modern_stool")) {
            secondMaterial = secondMaterial.replace("white", "");
            secondMaterial = secondMaterial.replace("light_gray", "");
            secondMaterial = secondMaterial.replace("gray", "");
            secondMaterial = "stripped".concat(secondMaterial);
        }
        if (block.getDefaultState().getMaterial().equals(net.minecraft.block.Material.NETHER_WOOD) && (!secondMaterial.contains("stem"))) {
            secondMaterial = secondMaterial.replace("blocks/", "").replace(furnitureName, "stem");
        }
        else if (block.getDefaultState().getMaterial().equals(net.minecraft.block.Material.WOOD) && !secondMaterial.contains("log")) {
            secondMaterial = secondMaterial.replace("blocks/", "").replace(furnitureName, "log");
        }
        else {
            secondMaterial = secondMaterial.replace("blocks/", "").replace(furnitureName, "");
        }
        if (secondMaterial.contains("stemlog")) {
            secondMaterial = secondMaterial.replace("stemlog", "stem");
        }
        this.secondMaterial = Registry.ITEM.get(new Identifier("minecraft:" + secondMaterial));
        return this.secondMaterial;
    }

    public Item getBaseMaterial() {
        if (block.getTranslationKey().contains("stripped")) {
            return getStrippedBaseMaterial();
        }
        String baseMaterial = this.block.getLootTableId().getPath();
        if(baseMaterial.contains("andesite")){
            baseMaterial = "stripped_oak_log";
        }
        else if (baseMaterial.contains("quartz")) {
            baseMaterial = "quartz_block";
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
        else if(baseMaterial.contains("smooth_stone")){
            baseMaterial = "white_concrete";
        }
        else if(baseMaterial.contains("stone")){
            baseMaterial = "stone";
        }
        else if(baseMaterial.contains("netherite")){
            baseMaterial = "ancient_debris";
        }
        else if(baseMaterial.contains("white") && !baseMaterial.contains("bed")){
            baseMaterial = "white_concrete";
        }
        else if(baseMaterial.contains("light_gray") && !baseMaterial.contains("bed")){
            baseMaterial = "light_gray_concrete";
        }
        else if(baseMaterial.contains("gray") && !baseMaterial.contains("bed")){
            baseMaterial = "gray_concrete";
        }
        else if(baseMaterial.contains("light_wood") || baseMaterial.contains("dark_wood")){
            baseMaterial = "smooth_quartz";
        }
        else {
            if (baseMaterial.contains("log") || baseMaterial.contains("stem")) {
                baseMaterial = baseMaterial.replace("log", "");
                baseMaterial = baseMaterial.replace("stem", "");
            }
            baseMaterial = baseMaterial.replace("blocks/", "").replace(furnitureName, "planks");
        }
        this.baseMaterial = Registry.ITEM.get(new Identifier("minecraft:" + baseMaterial));
        return this.baseMaterial;
    }
    public Item getFroggyChairMaterial() {
        String baseMaterial = this.block.getLootTableId().getPath();
        baseMaterial = baseMaterial.replace("blocks/", "").replace(furnitureName, "");
        if (baseMaterial.matches("froggy_chair")) {
            this.baseMaterial = Registry.ITEM.get(new Identifier("minecraft:" + "lime_concrete"));
            return this.baseMaterial;
        }
        baseMaterial = baseMaterial.concat("_concrete");
        this.baseMaterial = Registry.ITEM.get(new Identifier("minecraft:" + baseMaterial));
        return this.baseMaterial;
    }
    public Item getFridgeMaterial() {
        String baseMaterial = this.block.getLootTableId().getPath();
        baseMaterial = baseMaterial.replace("blocks/", "").replace(furnitureName, "");
        if (baseMaterial.contains("iron")) {
            this.baseMaterial =  Registry.ITEM.get(new Identifier("minecraft:" + "iron_ingot"));
            return this.baseMaterial;
        }
        else if (baseMaterial.contains("xbox")) {
            this.baseMaterial = Registry.ITEM.get(new Identifier("minecraft:" + "black_concrete"));
            return this.baseMaterial;
        }
        baseMaterial = baseMaterial.concat("concrete");
        this.baseMaterial = Registry.ITEM.get(new Identifier("minecraft:" + baseMaterial));
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


    public Item getStrippedBaseMaterial() {
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
        this.secondMaterial = Registry.ITEM.get(new Identifier("minecraft:" + secondMaterial));
        return this.secondMaterial;
    }

    public Item getStrippedSecondMaterial() {
        String baseMaterial = this.block.getLootTableId().getPath().replace("blocks/", "");
        if (baseMaterial.contains("log") || baseMaterial.contains("stem")) {
            baseMaterial = baseMaterial.replace("log", "");
            baseMaterial = baseMaterial.replace("stem", "");
        }
        if (baseMaterial.contains("raw_")) {
            baseMaterial = baseMaterial.replace("raw_", "");
        }
        baseMaterial = baseMaterial.replace("stripped_", "").replace(furnitureName, "planks");
        this.baseMaterial = Registry.ITEM.get(new Identifier("minecraft:" + baseMaterial));
        return this.baseMaterial;
    }

    public Item getArmChairMaterial() {
        String baseMaterial = this.block.getLootTableId().getPath();
        baseMaterial = baseMaterial.replace("blocks/", "").replace(furnitureName, "");
        if (baseMaterial.contains("leather")) {
            this.baseMaterial = Registry.ITEM.get(new Identifier("pfm:" + "leather_block"));
            return this.baseMaterial;
        }
        else if (baseMaterial.contains("standard")) {
            this.baseMaterial = Registry.ITEM.get(new Identifier("minecraft:" + "white_wool"));
            return this.baseMaterial;
        }
        if (this.block instanceof DyeableFurniture) {
            String color = ((DyeableFurniture) this.block).getPFMColor().toString();
            this.baseMaterial = Registry.ITEM.get(new Identifier("minecraft:" + color + "_wool"));
            return this.baseMaterial;
        }
        baseMaterial = baseMaterial.concat("wool");
        this.baseMaterial = Registry.ITEM.get(new Identifier("minecraft:" + baseMaterial));
        return this.baseMaterial;
    }

    public Block getBed() {
        if (block instanceof SimpleBedBlock){
            String color = ((SimpleBedBlock) block).getColor().getName();
            return Registry.BLOCK.get(new Identifier("minecraft:" + color + "_bed"));
        }
        return null;
    }

    public Item getFence() {
        String baseMaterial = this.block.getLootTableId().getPath();
            if (baseMaterial.contains("log") || baseMaterial.contains("stem")) {
                baseMaterial = baseMaterial.replace("log", "");
                baseMaterial = baseMaterial.replace("stem", "");
            }
        baseMaterial = baseMaterial.replace("blocks/", "").replace(furnitureName, "fence");
        this.baseMaterial = Registry.ITEM.get(new Identifier("minecraft:" + baseMaterial));
        return this.baseMaterial;
    }

    public Item getSecondaryStoneMaterial() {
        String secondMaterial = this.block.getLootTableId().getPath();
        if(secondMaterial.contains("andesite")){
            secondMaterial = "andesite";
        }
        else if(secondMaterial.contains("blackstone")){
            secondMaterial = "blackstone";
        }
        else if(secondMaterial.contains("calcite")){
            secondMaterial = "stripped_warped_stem";
        }
        else if(secondMaterial.contains("dark_concrete")){
            secondMaterial = "white_concrete";
        }
        else if(secondMaterial.contains("concrete")){
            secondMaterial = "raw_concrete";
            this.secondMaterial = Registry.ITEM.get(new Identifier("pfm:" + secondMaterial));
            return this.secondMaterial;
        }
        else if(secondMaterial.contains("deepslate")){
            secondMaterial = "deepslate";
        }
        else if(secondMaterial.contains("diorite")){
            secondMaterial = "diorite";
        }
        else if(secondMaterial.contains("granite")){
            secondMaterial = "granite";
        }
        else if(secondMaterial.contains("stone")){
            secondMaterial = "cobblestone";
        }
        else if(secondMaterial.contains("netherite")){
            secondMaterial = "netherite_block";
        }
        else if (secondMaterial.contains("quartz")) {
            secondMaterial = "quartz_block";
        }
        else if (secondMaterial.contains("gray") || secondMaterial.contains("white")) {
            secondMaterial = "dark_oak_planks";
        }
        else {
            secondMaterial = secondMaterial.replace("blocks/", "").replace(furnitureName, "");
        }
        this.secondMaterial = Registry.ITEM.get(new Identifier("minecraft:" + secondMaterial));
        return this.secondMaterial;
    }

    public Item getBaseStoneMaterial() {
        String baseMaterial = this.block.getLootTableId().getPath();
        if(baseMaterial.contains("andesite")){
            baseMaterial = "polished_andesite";
        }
        else if(baseMaterial.contains("blackstone")){
            baseMaterial = "polished_blackstone";
        }
        else if(baseMaterial.contains("calcite")){
            baseMaterial = "calcite";
        }
        else if(baseMaterial.contains("dark_concrete")){
            baseMaterial = "white_concrete";
        }
        else if(baseMaterial.contains("concrete")){
            baseMaterial = "raw_concrete";
            this.baseMaterial = Registry.ITEM.get(new Identifier("pfm:" + baseMaterial));
            return this.baseMaterial;
        }
        else if(baseMaterial.contains("deepslate")){
            baseMaterial = "polished_deepslate";
        }
        else if(baseMaterial.contains("diorite")){
            baseMaterial = "polished_diorite";
        }
        else if(baseMaterial.contains("granite")){
            baseMaterial = "polished_granite";
        }
        else if(baseMaterial.contains("stone")){
            baseMaterial = "stone";
        }
        else if(baseMaterial.contains("netherite")){
            baseMaterial = "netherite_block";
        }
        else if (baseMaterial.contains("quartz")) {
            baseMaterial = "quartz_block";
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
        else {
            baseMaterial = baseMaterial.replace("blocks/", "").replace(furnitureName, "");
        }
        this.baseMaterial = Registry.ITEM.get(new Identifier("minecraft:" + baseMaterial));
        return this.baseMaterial;
    }

    public Item getPlateMaterial() {
        String baseMaterial = this.block.getLootTableId().getPath();
        if (baseMaterial.contains("basic")) {
            baseMaterial = "white_concrete";
        }
        else  {
            baseMaterial = baseMaterial.replace("blocks/", "").replace(furnitureName, "block");
        }
        this.baseMaterial = Registry.ITEM.get(new Identifier("minecraft:" + baseMaterial));
        return this.baseMaterial;
    }

    public Item getPlateDecoration() {
        String baseMaterial = this.block.getLootTableId().getPath();
        if (baseMaterial.contains("basic")) {
            baseMaterial = "white_concrete";
        }
        else  {
            baseMaterial = baseMaterial.replace("blocks/", "").replace(furnitureName, "block");
        }
        this.baseMaterial = Registry.ITEM.get(new Identifier("minecraft:" + baseMaterial));
        return this.baseMaterial;
    }

    public Item getCutleryMaterial() {
        String baseMaterial = this.block.getLootTableId().getPath();
        if (baseMaterial.contains("basic")) {
            baseMaterial = "light_gray_concrete";
        }
        else  {
            baseMaterial = baseMaterial.replace("blocks/", "").replace(furnitureName, "block");
        }
        this.baseMaterial = Registry.ITEM.get(new Identifier("minecraft:" + baseMaterial));
        return this.baseMaterial;
    }
}
