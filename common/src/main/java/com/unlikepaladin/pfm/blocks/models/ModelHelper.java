package com.unlikepaladin.pfm.blocks.models;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.DyeableFurnitureBlock;
import com.unlikepaladin.pfm.data.materials.*;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import com.unlikepaladin.pfm.runtime.PFMDataGen;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.TextureMap;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.registry.Registry;

import java.io.File;
import java.io.IOException;
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
        if (Registries.BLOCK.get(identifier) instanceof DyeableFurnitureBlock block) {
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
        Block block = Registries.BLOCK.get(new Identifier("minecraft", string+"_wool"));
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
        if (blockToTextureMap.containsKey(pair)) {
            return blockToTextureMap.get(pair);
        }
        Identifier id;
        if (!postfix.isEmpty() && idExists(TextureMap.getSubId(block, postfix), ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)){
            id = TextureMap.getSubId(block, postfix);
        }
        else if(idExists(getLogId(block, postfix), ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
            id = getLogId(block, postfix);
        }
        else if (idExists(TextureMap.getId(block), ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
            id = TextureMap.getId(block);
        }
        else if (idExists(TextureMap.getSubId(block, "_side"), ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
            id = TextureMap.getSubId(block, "_side");
        }
        else if (idExists(TextureMap.getSubId(block, "_side_1"), ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
            id = TextureMap.getSubId(block, "_side_1");
        }
        else if (idExists(TextureMap.getSubId(block, "_bottom"), ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)){
            id = TextureMap.getSubId(block, "_bottom");
        }
        else if (idExists(TextureMap.getSubId(block, "_top"), ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)){
            id = TextureMap.getSubId(block, "_top");
        }
        else if (idExists(TextureMap.getSubId(block, "_middle"), ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)){
            id = TextureMap.getSubId(block, "_middle");
        }
        else if(idExists(getPlankId(block), ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
            id = getPlankId(block);
        }
        else if(idExists(getLogId(block, "_side"), ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
            id = getLogId(block, "_side");
        }
        else if(idExists(getLogId(block, "_side_1"), ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
            id = getLogId(block, "_side_1");
        }
        else if(idExists(getLogId(block, "_top"), ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
            id = getLogId(block, "_top");
        }
        else if(idExists(getLogId(block, "_middle"), ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
            id = getLogId(block, "_middle");
        }
        else if(idExists(getLogId(block, "_bottom"), ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
            id = getLogId(block, "_bottom");
        }
        else {
            PFMDataGen.LOGGER.warn("Couldn't find texture for, {}", block);
            id =  TextureMap.getId(Blocks.BEDROCK);
        }
        blockToTextureMap.put(pair, id);
        return id;
    }

    //For compatibility with Twilight Forest's Planks
    public static Identifier getPlankId(Block block) {
        Identifier identifier = Registries.BLOCK.getId(block);
        String namespace = identifier.getNamespace();
        String path = identifier.getPath();
        if (path.contains("planks")) {
            path = path.replace("_planks", "");
            path = "planks_" + path;
            Identifier id = new Identifier(namespace, "block/wood/" + path);
            path = path.replace("mining", "mine").replace("sorting", "sort").replace("transformation", "trans").replace("dark", "darkwood");

            if (idExists(id, ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES))
                return id;
            else
                return new Identifier(namespace, "block/wood/" + path+ "_0");
        }
        else
            return new Identifier(namespace, "block/" + path);
    }

    public static Identifier getLogId(Block block, String postFix) {
        Identifier identifier = Registries.BLOCK.getId(block);
        String namespace = identifier.getNamespace();
        String path = identifier.getPath();
        if (path.contains("log")) {
            path = path.replace("log", "bark");
            path += postFix;
            Identifier id = new Identifier(namespace, "block/" + path);
            if (idExists(id, ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }

            path = path.replace("stripped", "striped");
            id = new Identifier(namespace, "block/" + path);
            if (idExists(id, ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
            path = path.replace("striped", "stripped");
            path = path.replace("bark", "log");
            id = new Identifier(namespace, "block/" + path);
            if (idExists(id, ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
            path = path.replace("stripped", "striped");
            id = new Identifier(namespace, "block/" + path);
            if (idExists(id, ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }

            path = path.contains("striped") ? "stripped_"+path.replace("_striped", "") : path;
            id = new Identifier(namespace, "block/" + path);
            if (idExists(id, ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
            path = path.replace("stripped", "striped");
            id = new Identifier(namespace, "block/" + path);
            if (idExists(id, ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
        } else if (path.contains("reed")) {
            path = path.replace("nether_", "").replace("reed", "reeds");
            Identifier id = new Identifier(namespace, "block/" + path);
            if (idExists(id, ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)){
                return id;
            }
            path += postFix;
            id = new Identifier(namespace, "block/" + path);
            if (idExists(id, ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
            id = new Identifier(namespace, "block/" + path.replace("planks", "roof"));
            if (idExists(id, ResourceType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
        }
        return new Identifier(namespace, "block/" + path);
    }

    private static final HashMap<Identifier, Boolean> idCacheMap = new HashMap<>();
    public static boolean idExists(Identifier id, ResourceType resourceType, IdLocation idLocation) {
        if (idCacheMap.containsKey(id)) {
            return idCacheMap.get(id);
        }
        Identifier id2 = new Identifier(id.getNamespace(), idLocation.asString() + "/" + id.getPath() + idLocation.getFileType());
        AtomicBoolean exists = new AtomicBoolean(false);
        for (ResourcePack rp : PFMRuntimeResources.RESOURCE_PACK_LIST) {
            if (exists.get())
                break;

            rp.findResources(resourceType, id2.getNamespace(), id2.getPath(), (identifier, supplier) -> {
                try {
                    supplier.get().read();
                    supplier.get().close();
                    exists.set(true);
                } catch (IOException e) {
                    exists.set(false);
                }
            });
        }
        idCacheMap.put(id, exists.get());
        return exists.get();
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
