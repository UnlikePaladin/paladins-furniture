package com.unlikepaladin.pfm.blocks.models;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.DyeableFurnitureBlock;
import com.unlikepaladin.pfm.data.materials.*;
import com.unlikepaladin.pfm.mixin.PFMSpriteContentsAccessor;
import com.unlikepaladin.pfm.runtime.PFMDataGenerator;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import com.unlikepaladin.pfm.runtime.TextureReloadQueue;
import de.androidpit.colorthief.ColorThief;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.util.RandomSource;

public class ModelHelper {
    public static List<TextureAtlasSprite> OAK_SPRITES_PLANKS_TO_REPLACE = null;
    public static List<TextureAtlasSprite> getOakPlankLogSprites() {
        if (OAK_SPRITES_PLANKS_TO_REPLACE == null) {
            Material planksId = new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.parse("minecraft:block/oak_planks"));
            Material logId = new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.parse("minecraft:block/oak_log"));
            OAK_SPRITES_PLANKS_TO_REPLACE = Arrays.asList(planksId.sprite(), logId.sprite());
        }
        return OAK_SPRITES_PLANKS_TO_REPLACE;
    }
    public static List<TextureAtlasSprite> OAK_SPRITES_BED_TO_REPLACE = null;
    public static List<TextureAtlasSprite> getOakBedSprites() {
        if (OAK_SPRITES_BED_TO_REPLACE == null) {
            Material planksId = new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.parse("minecraft:block/oak_planks"));
            Material bedId = Sheets.BED_TEXTURES[DyeColor.RED.getId()];
            OAK_SPRITES_BED_TO_REPLACE = Arrays.asList(planksId.sprite(), bedId.sprite());
        }
        return OAK_SPRITES_BED_TO_REPLACE;
    }
    public static List<TextureAtlasSprite> OAK_SPRITES_LOG_TOP_TO_REPLACE = null;
    public static List<TextureAtlasSprite> getOakLogLogTopSprites() {
        if (OAK_SPRITES_LOG_TOP_TO_REPLACE == null) {
            Material logId = new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.parse("minecraft:block/oak_log"));
            Material logTopId = new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.parse("minecraft:block/oak_log_top"));
            OAK_SPRITES_LOG_TOP_TO_REPLACE = Arrays.asList(logId.sprite(), logTopId.sprite());
        }
        return OAK_SPRITES_LOG_TOP_TO_REPLACE;
    }

    public static Set<ResourceLocation> GENERATED_TEXTURE_IDS = new HashSet<>();

    public static boolean containsIdentifier(ResourceLocation[] modelIds, ResourceLocation comparison) {
        return Arrays.stream(modelIds).anyMatch(identifier -> comparison.getPath().equals(identifier.getPath()) && comparison.getNamespace().equals(identifier.getNamespace()));
    }

    public static void generateTexture(TextureAtlasSprite baseTexture, TextureAtlasSprite color, int colorCount, ResourceLocation id) {
        if (GENERATED_TEXTURE_IDS.contains(id)) {
            return;
        }
        GENERATED_TEXTURE_IDS.add(id);
        int[] basePalette = convertPaletteToColorArray(generatePalette(baseTexture, colorCount));
        int[] colorPalette = convertPaletteToColorArray(generatePalette(color, colorCount));

        Integer[] base = Arrays.stream(basePalette).boxed().toArray(Integer[]::new);
        Integer[] target = Arrays.stream(colorPalette).boxed().toArray(Integer[]::new);

        Arrays.sort(base, Comparator.comparingDouble(ModelHelper::luminance));
        Arrays.sort(target, Comparator.comparingDouble(ModelHelper::luminance));

        Map<Integer, Integer> colorMap = new HashMap<>();
        for (int i = 0; i < basePalette.length; i++) {
            colorMap.put(base[i], target[i]);
        }

        TextureReloadQueue.recolorAndWriteImage(id, getSpriteBufferedImage(baseTexture), colorMap);
    }

    static double luminance(int argb) {
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;
        return 0.2126 * r + 0.7152 * g + 0.0722 * b;
    }

    public static ResourceLocation getTextureSpritePath(ResourceLocation id) {
        return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), String.format("textures/%s%s", id.getPath(), ".png"));
    }

    public static int[] convertPaletteToColorArray(int[][] palette) {
        int[] colors = new int[palette.length];
        for (int i = 0; i < palette.length; i++) {
            colors[i] = convertColor(palette[i]);
        }
        return colors;
    }

    public static int convertColor(int[] color) {
        int r = color[0];
        int g = color[1];
        int b = color[2];
        return (r << 16) | (g << 8) | b;
    }

    static Map<Pair<ResourceLocation, Integer>, int[][]> paletteCache = new HashMap<>();
    public static int[][] generatePalette(TextureAtlasSprite texture, int colorCount) {
        if (texture == null)
            return null;
        else if (paletteCache.containsKey(Pair.of(texture.contents().name(), colorCount)))
            return paletteCache.get(Pair.of(texture.contents().name(), colorCount));

        BufferedImage image = getSpriteBufferedImage(texture);
        int[][] palette = ColorThief.getPalette(image, colorCount, 5, false);
        paletteCache.put(new Pair<>(texture.contents().name(), colorCount), palette);
        String filename = texture.contents().name().getNamespace().replace("/", "") + "_" + texture.contents().name().getPath().replace("/", "") + "_original.png";
        //writeBufferedImageToFile(image, filename);

        //writePaletteToImage(palette, texture.getId().getNamespace().replace("/", "") + "_" + texture.getId().getPath().replace("/", "") + "_palette.png");
        return palette;
    }

    public static void writePaletteToImage(int[][] palette, String filename) throws IOException {
        int width = palette.length;
        int height = 1;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] convertedPalette = convertPaletteToColorArray(palette);
        Arrays.sort(convertedPalette);
        for (int x = 0; x < width; x++) {
            image.setRGB(x, 0, convertedPalette[x]);
        }

        writeBufferedImageToFile(image, filename);
    }

    public static void writeBufferedImageToFile(BufferedImage image, String filename) {
        try {
            File file = new File(filename);
            file.createNewFile();
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            PaladinFurnitureMod.GENERAL_LOGGER.error("Error while writing image to file", e);
        }
    }

    public static BufferedImage getSpriteBufferedImage(TextureAtlasSprite sprite) {
        int width = sprite.contents().width();
        int height = sprite.contents().height();
        // Upload the sprite to ensure underlying NativeImage data is present
        sprite.uploadFirstFrame();
        NativeImage atlasImage = ((PFMSpriteContentsAccessor)sprite.contents()).pfm$getImages()[0];
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int abgr = atlasImage.getPixelRGBA(i, j); // NativeImage returns ABGR (AABBGGRR)
                int argb = abgrToArgb(abgr);
                bufferedImage.setRGB(i, j, argb);
            }
        }
        return bufferedImage;
    }

    // Convert ABGR (AABBGGRR) to ARGB (AARRGGBB)
    public static int abgrToArgb(int abgr) {
        int a = (abgr >> 24) & 0xFF;
        int b = (abgr >> 16) & 0xFF;
        int g = (abgr >> 8) & 0xFF;
        int r = (abgr) & 0xFF;
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    // Convert single ARGB int (0xAARRGGBB) -> RGBA int (0xRRGGBBAA)
    public static int argbToRgbaInt(int argb) {
        int a = (argb >> 24) & 0xFF;
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;
        return (r << 24) | (g << 16) | (b << 8) | a;
    }

    public static BufferedImage nativeImageToBufferedImage(NativeImage nativeImage) {
        int width = nativeImage.getWidth();
        int height = nativeImage.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int abgr = nativeImage.getPixelRGBA(x, y);
                int argb = abgrToArgb(abgr);
                bufferedImage.setRGB(x, y, argb);
            }
        }
        return bufferedImage;
    }


    public static BlockType getBlockType(ResourceLocation identifier) {
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

    public static VariantBase<?> getVariant(ResourceLocation identifier) {
        VariantBase<?> var = getExtraCounterType(identifier);
        if (var == null) {
            var = getStoneType(identifier);
        }
        if (var == null) {
            var = getWoodType(identifier);
        }
        return var;
    }

    @Nullable
    public static ExtraCounterVariant getExtraCounterType(ResourceLocation identifier) {
        for (ExtraCounterVariant variant:
                ExtraCounterVariant.values()) {
            if (identifier.getPath().contains(variant.getPath()) && getBlockType(identifier) == BlockType.BLOCK) {
                return variant;
            }
        }
        return null;
    }

    @Nullable
    public static StoneVariant getStoneType(ResourceLocation identifier) {
        for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
            if (identifier.getPath().contains(variant.getPath()) && getBlockType(identifier) == BlockType.BLOCK) {
                return variant;
            }
        }
        return null;
    }
    public static WoodVariant getWoodType(ResourceLocation identifier){
        WoodVariant selectedVariant = null;
        for (WoodVariant woodVariant : WoodVariantRegistry.getVariants())
            if (identifier.getPath().contains(woodVariant.identifier.getPath())) {
                if (identifier.getPath().contains("dark") && !woodVariant.identifier.getPath().contains("dark") || (!identifier.getPath().contains(woodVariant.getNamespace()) && !woodVariant.isVanilla()))
                    continue;
                selectedVariant = woodVariant;
        }
        return selectedVariant != null ? selectedVariant : WoodVariantRegistry.OAK;
    }

    @ExpectPlatform
    public static BakedModel getModelFromIdentifier(ResourceLocation id) {
        throw new AssertionError();
    }

    public static DyeColor getColor(ResourceLocation identifier) {
        if (BuiltInRegistries.BLOCK.get(identifier) instanceof DyeableFurnitureBlock block) {
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

    public static ResourceLocation getVanillaConcreteColor(ResourceLocation identifier) {
        DyeColor color = getColor(identifier);
        if (!identifier.getPath().contains(color.getName()))
            return ResourceLocation.fromNamespaceAndPath("minecraft", "block/white_concrete");
        return ResourceLocation.fromNamespaceAndPath("minecraft", "block/"+ color.getName() + "_concrete");
    }

    public static Block getWoolColor(String string) {
        Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("minecraft", string+"_wool"));
        if (block != Blocks.AIR) {
            return block;
        }
        return Blocks.WHITE_WOOL;
    }

    public static ResourceLocation getTextureId(Block block) {
        return getTextureId(block, "");
    }
    public static final Map<Pair<String, String>, Pair<ResourceLocation, Integer>> blockToTextureMap = new HashMap<>();
    public static ResourceLocation getTextureId(Block block, String postfix) {
        if (postfix.isEmpty())
            postfix = null;
        Pair<String, String> pair = new Pair<>(block.toString(), postfix);
        if (blockToTextureMap.containsKey(pair) && (blockToTextureMap.get(pair).getFirst() != MissingTextureAtlasSprite.getLocation() || blockToTextureMap.get(pair).getSecond() > 3)) {
            return blockToTextureMap.get(pair).getFirst();
        }
        int attemptNum = 1;
        if (blockToTextureMap.containsKey(pair)) {
            attemptNum += blockToTextureMap.get(pair).getSecond();
        }
        if (postfix == null)
            postfix = "";

        ResourceLocation id;
        if (postfix.isEmpty() && !PFMDataGenerator.areAssetsRunning()) {
            BakedModel model = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(block.defaultBlockState());
            if (model != null) {
                List<BakedQuad> quadList = model.getQuads(block.defaultBlockState(), Direction.NORTH, RandomSource.create(42L));
                if (!quadList.isEmpty()) {
                    id = quadList.get(0).getSprite().contents().name();
                    if (id != null && id != MissingTextureAtlasSprite.getLocation()) {
                        blockToTextureMap.put(pair, new Pair<>(id, attemptNum));
                        return id;
                    }
                }
            }
        } else if (postfix.equals("_top") && !PFMDataGenerator.areAssetsRunning()) {
            BakedModel model = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(block.defaultBlockState());
            if (model != null) {
                List<BakedQuad> quadList = model.getQuads(block.defaultBlockState(), Direction.UP, RandomSource.create(42L));
                if (!quadList.isEmpty()) {
                    id = quadList.get(0).getSprite().contents().name();
                    if (id != null && id != MissingTextureAtlasSprite.getLocation()) {
                        blockToTextureMap.put(pair, new Pair<>(id, attemptNum));
                        return id;
                    }
                }
                quadList = model.getQuads(block.defaultBlockState(), Direction.DOWN, RandomSource.create(42L));
                if (!quadList.isEmpty()) {
                    id = quadList.get(0).getSprite().contents().name();
                    if (id != null && id != MissingTextureAtlasSprite.getLocation()) {
                        blockToTextureMap.put(pair, new Pair<>(id, attemptNum));
                        return id;
                    }
                }
            }
        }

        if (idExists(TextureMapping.getBlockTexture(block, postfix), PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)){
            id = TextureMapping.getBlockTexture(block, postfix);
        }
        else if(idExists(getLogId(block, postfix), PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
            id = getLogId(block, postfix);
        }
        else if (idExists(TextureMapping.getBlockTexture(block), PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
            id = TextureMapping.getBlockTexture(block);
        }
        else if (idExists(TextureMapping.getBlockTexture(block, "_side"), PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
            id = TextureMapping.getBlockTexture(block, "_side");
        }
        else if (idExists(TextureMapping.getBlockTexture(block, "_side_1"), PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
            id = TextureMapping.getBlockTexture(block, "_side_1");
        }
        else if (idExists(TextureMapping.getBlockTexture(block, "_bottom"), PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)){
            id = TextureMapping.getBlockTexture(block, "_bottom");
        }
        else if (idExists(TextureMapping.getBlockTexture(block, "_top"), PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)){
            id = TextureMapping.getBlockTexture(block, "_top");
        }
        else if (idExists(TextureMapping.getBlockTexture(block, "_middle"), PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)){
            id = TextureMapping.getBlockTexture(block, "_middle");
        }
        else if(idExists(getPlankId(block), PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
            id = getPlankId(block);
        }
        else if(idExists(getLogId(block, "_side"), PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
            id = getLogId(block, "_side");
        }
        else if(idExists(getLogId(block, "_side_1"), PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
            id = getLogId(block, "_side_1");
        }
        else if(idExists(getLogId(block, "_top"), PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
            id = getLogId(block, "_top");
        }
        else if(idExists(getLogId(block, "_middle"), PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
            id = getLogId(block, "_middle");
        }
        else if(idExists(getLogId(block, "_bottom"), PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
            id = getLogId(block, "_bottom");
        }
        else if (BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals("quark")) {
            id = TextureMapping.getBlockTexture(block, postfix);
        } else {
            PaladinFurnitureMod.GENERAL_LOGGER.warn("Couldn't find texture for, {}, this is attempt {} at finding it", block, attemptNum);
            id = MissingTextureAtlasSprite.getLocation();
        }
        blockToTextureMap.put(pair, new Pair<>(id, attemptNum));
        return id;
    }

    // For compatibility with Twilight Forest's Planks
    public static ResourceLocation getPlankId(Block block) {
        ResourceLocation identifier = BuiltInRegistries.BLOCK.getKey(block);
        String namespace = identifier.getNamespace();
        String path = identifier.getPath().replace("luphie_", "");
        if (path.contains("planks")) {
            path = path.replace("_planks", "").replace("plank_", "");
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path +"/planks");
            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES))
                return id;

            path = "planks_" + path;
            if (namespace.contains("pixelmon") && path.contains("ultra")) {
                path = path.replace("ultra_", "").replace("_ultra", "");
                path = "ultra_space/" + path;
            }
            if (namespace.equals("blue_skies")) {
                path = "wood/" + path;
            }
            id = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path);
            path = path.replace("mining", "mine").replace("sorting", "sort").replace("transformation", "trans").replace("dark", "darkwood").replace("alpha_", "alpha_oak_").replace("flowering_pink", "flowerypink").replace("flowering_purple", "floweringpurple");
            ResourceLocation id2 = ResourceLocation.fromNamespaceAndPath(namespace, "block/wood/" + path);
            ResourceLocation id3 = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path.replace("planks_", "") + "planks");
            ResourceLocation id4 = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path.replace("planks_", "") + "_planks");
            ResourceLocation id5 = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path.replace("planks_", "") + "plankstext");
            ResourceLocation id6 = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path.replace("planks_", "") + "plankretext");
            ResourceLocation id7 = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path.replace("planks_", "") + "_planks0");
            ResourceLocation id8 = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path.replace("planks_", "") + "_planks1");

            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES))
                return id;
            else if (idExists(id2, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES))
                return id2;
            else if (idExists(id3, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES))
                return id3;
            else if (idExists(id4, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES))
                return id4;
            else if (idExists(id5, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES))
                return id5;
            else if (idExists(id6, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES))
                return id6;
            else if (idExists(id7, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES))
                return id7;
            else if (idExists(id8, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES))
                return id8;
            else
                return ResourceLocation.fromNamespaceAndPath(namespace, "block/wood/" + path+ "_0");
        }
        else
            return ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path);
    }

    public static ResourceLocation getLogId(Block block, String postFix) {
        ResourceLocation identifier = BuiltInRegistries.BLOCK.getKey(block);
        String namespace = identifier.getNamespace();
        String path = identifier.getPath().replace("luphie_", "");
        if (namespace.contains("luphieclutteredmod") && path.contains("flowering_log")) {
            path = path.replace("flowering_log", "flowering_yellow_log");
        }
        if (namespace.contains("pixelmon") && path.contains("ultra")) {
            path = path.replace("ultra_", "").replace("_ultra", "");
            path = "ultra_space/" + path;
        }
        if (namespace.equals("blue_skies")) {
            path = "wood/" + path;
        }
        if (namespace.equals("byg") && path.contains("pedu"))
            path = path.replace("pedu", "log");
        if (path.contains("log") || path.contains("stem")) {
            if (!path.contains("_log")) {
                path = path.replace("log", "_log");
            }
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path);
            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
            path = path.replace("stem", "log").replace("log", "bark");
            path += postFix;
            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }

            path = path.replace("stripped", "striped");
            id = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path);
            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
            path = path.replace("striped", "stripped");
            path = path.replace("bark", "log");
            id = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path);
            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
            path = path.replace("stripped", "striped");
            id = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path);
            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }

            path = path.contains("striped") ? "stripped_"+path.replace("_striped", "") : path;
            id = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path);
            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
            path = path.replace("stripped", "striped");
            id = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path);
            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
            String loc = identifier.getPath().contains("stripped") || identifier.getPath().contains("striped") ? "stripped_log" : "log";
            path = path.replace("striped_", "").replace(postFix, "").replace("_log", "");

            id = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path+ "/" + loc + postFix);
            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
            id = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path+ "/" + loc.replace("log", "stem") + postFix);
            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
            id = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path+ "/" + loc + "/" + postFix.replace("_", ""));
            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
            id = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path+ "/" + loc.replace("log", "stem") + "/" + postFix.replace("_", ""));
            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
            id = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path+ "/" + loc);
            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
            id = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path+ "/" + loc.replace("log", "stem"));
            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
            id = ResourceLocation.fromNamespaceAndPath(namespace, "block/stripped_" + path+ "_log");
            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
            id = ResourceLocation.fromNamespaceAndPath(namespace, "block/stripped_" + path+ "_stem");
            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
            id = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path+ "_log_stripped");
            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
            id = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path+ "_stem_stripped");
            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
        } else if (path.contains("reed")) {
            path = path.replace("nether_", "").replace("reed", "reeds");
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path);
            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)){
                return id;
            }
            path += postFix;
            id = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path);
            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
            id = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path.replace("planks", "roof"));
            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
        }
        if (path.contains("alpha_") && namespace.contains("regions")) {
            path = !path.contains("alpha_oak") ? path.replace("alpha", "alpha_oak") : path;
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path);
            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)){
                return id;
            }
            path += postFix;
            id = ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path);
            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
            id = ResourceLocation.fromNamespaceAndPath(namespace, "block/alpha_oak_log" + postFix);
            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
            id = ResourceLocation.fromNamespaceAndPath(namespace, "block/alpha_oak_log");
            if (idExists(id, PackType.CLIENT_RESOURCES, IdLocation.TEXTURES)) {
                return id;
            }
        }
        return ResourceLocation.fromNamespaceAndPath(namespace, "block/" + path);
    }

    private static final HashMap<ResourceLocation, Boolean> idCacheMap = new HashMap<>();
    public static boolean idExists(ResourceLocation id, PackType resourceType, IdLocation idLocation) {
        if (idCacheMap.containsKey(id)) {
            return idCacheMap.get(id);
        }
        ResourceLocation id2 = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), idLocation.getSerializedName() + "/" + id.getPath() + idLocation.getFileType());
        AtomicBoolean exists = new AtomicBoolean(false);
        for (PackResources rp : PFMRuntimeResources.RESOURCE_PACK_LIST) {
            if (exists.get())
                break;

            rp.listResources(resourceType, id2.getNamespace(), id2.getPath(), (identifier, supplier) -> {
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

    public enum IdLocation implements StringRepresentable {
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
        public String getSerializedName() {
            return name;
        }

        public String getFileType() {
            return fileType;
        }
    }
}
