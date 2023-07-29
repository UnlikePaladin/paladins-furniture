package com.unlikepaladin.pfm.blocks.models;

import com.unlikepaladin.pfm.blocks.DyeableFurnitureBlock;
import com.unlikepaladin.pfm.data.materials.*;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import com.unlikepaladin.pfm.runtime.PFMDataGen;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.model.Texture;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
        for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
            if (identifier.getPath().contains(variant.getPath())) {
                return BlockType.PLANKS;
            }
        }
        return BlockType.BLOCK;
    }

    public static VariantBase getVariant(Identifier identifier) {
        VariantBase var = getExtraCounterType(identifier);
        if (var == ExtraCounterVariant.DEEPSLATE_TILE && !identifier.getPath().contains("deepslate_tile")) {
            var = getStoneType(identifier);
        }
        if ((var == StoneVariant.STONE && !identifier.getPath().contains("stone")) || (var == ExtraCounterVariant.DEEPSLATE_TILE && !identifier.getPath().contains("deepslate_tile"))) {
            var = getWoodType(identifier);
        }
        return var;
    }

    public static ExtraCounterVariant getExtraCounterType(Identifier identifier) {
        for (ExtraCounterVariant variant:
                ExtraCounterVariant.values()) {
            if (identifier.getPath().contains(variant.getPath())) {
                return variant;
            }
        }
        return ExtraCounterVariant.DEEPSLATE_TILE;
    }
    public static StoneVariant getStoneType(Identifier identifier) {
        for (StoneVariant variant:
             StoneVariant.values()) {
            if (identifier.getPath().contains(variant.getPath())) {
                return variant;
            }
        }
        return StoneVariant.STONE;
    }
    public static WoodVariant getWoodType(Identifier identifier){
        WoodVariant selectedVariant = null;
        for (WoodVariant woodVariant : WoodVariantRegistry.getVariants())
            if (identifier.getPath().contains(woodVariant.identifier.getPath())) {
                if (identifier.getPath().contains("dark") && !woodVariant.identifier.getPath().contains("dark"))
                    continue;
                selectedVariant = woodVariant;
        }
        return selectedVariant != null ? selectedVariant : WoodVariantRegistry.OAK;
    }

    public static DyeColor getColor(Identifier identifier) {
        if (Registry.BLOCK.get(identifier) instanceof DyeableFurnitureBlock block) {
            return block.getPFMColor();
        }
        for (DyeColor color : DyeColor.values()) {
            if (identifier.getPath().contains(color.getName())){
                if (!identifier.getPath().contains("light") && color.getName().contains("light"))  {
                    continue;
                } else if (identifier.getPath().contains("light") && !color.getName().contains("light"))  {
                    continue;
                }
                return color;
            }
        }
        return DyeColor.RED;
    }

    public static Identifier getVanillaConcreteColor(Identifier identifier) {
        DyeColor color = getColor(identifier);
        if (!identifier.getPath().contains(color.getName()))
            return new Identifier("minecraft", "block/white_concrete");
        return new Identifier("minecraft", "block/"+ color.getName() + "_concrete");
    }

    public static Block getWoolColor(String string) {
        Block block = Registry.BLOCK.get(new Identifier("minecraft", string+"_wool"));
        if (block != Blocks.AIR) {
            return block;
        }
        return Blocks.WHITE_WOOL;
    }

    public static Identifier getTextureId(Block block) {
        return getTextureId(block, "");
    }
    private static final Map<Pair<Block, String>, Identifier> blockToTextureMap = new HashMap<>();
    public static Identifier getTextureId(Block block, String postfix) {
        Pair<Block, String> pair = new Pair<>(block, postfix);
        if (blockToTextureMap.containsKey(pair))
            return blockToTextureMap.get(pair);
        Identifier id;
        if (!postfix.isEmpty() && idExists(Texture.getSubId(block, postfix), ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES))
            id = Texture.getSubId(block, postfix);
        else if (idExists(Texture.getId(block), ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
            id = Texture.getId(block);
        }
        else if (idExists(Texture.getSubId(block, "_side"), ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
            id = Texture.getSubId(block, "_side");
        }
        else if (idExists(Texture.getSubId(block, "_bottom"), ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)){
            id = Texture.getSubId(block, "_bottom");
        }
        else if (idExists(Texture.getSubId(block, "_top"), ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)){
            id = Texture.getSubId(block, "_top");
        }
        else if(idExists(getPlankId(block), ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
            id = getPlankId(block);
        }
        else {
            PFMDataGen.LOGGER.warn("Couldn't find texture for, {}", block);
            id =  Texture.getId(Blocks.BEDROCK);
        }
        blockToTextureMap.put(pair, id);
        return id;
    }

    //For compatibility with Twilight Forest's Planks
    public static Identifier getPlankId(Block block) {
        Identifier identifier = Registry.BLOCK.getId(block);
        String namespace = identifier.getNamespace();
        String path = identifier.getPath();
        if (path.contains("planks")) {
            path = path.replace("_planks", "");
            path = "planks_" + path;
            Identifier id = new Identifier(namespace, "block/wood/" + path);
            if (idExists(id, ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES))
                return id;
            else
                return new Identifier(namespace, "block/wood/" + path+ "_0");
        }
        else
            return new Identifier(namespace, "block/" + path);
    }

    private static final HashMap<Identifier, Boolean> idCacheMap = new HashMap<>();
    public static boolean idExists(Identifier id, ResourceType resourceType, IdLocation idLocation) {
        Identifier id2 = new Identifier(id.getNamespace(), idLocation.asString() + "/" + id.getPath() + idLocation.getFileType());
        if (idCacheMap.containsKey(id)) {
            return idCacheMap.get(id);
        }
        for (ResourcePack rp : PFMRuntimeResources.RESOURCE_PACK_LIST) {
            if (rp.contains(resourceType, id2)) {
                idCacheMap.put(id, true);
                return true;
            }
        }
        idCacheMap.put(id, false);
        return false;
    }

    public enum IdLocation implements StringIdentifiable {
        TEXTURES("textures", ".png"),
        MODELS("models"),
        BLOCKSTATES("blockstates"),
        RECIPES("recipes"),
        TAGS("tags"),
        LOOT_TABLES("loot_tables"),
        STRUCTURES("structures"),
        ADVANCEMENTS("advancements");
        private final String name;
        private final String fileType;
        IdLocation(String name, String fileType) {
            this.name = name;
            this.fileType = fileType;
        }

        IdLocation(String name) {
            this.name = name;
            this.fileType = ".json";
        }

        @Override
        public String asString() {
            return name;
        }

        public String getFileType() {
            return fileType;
        }
    };
}
