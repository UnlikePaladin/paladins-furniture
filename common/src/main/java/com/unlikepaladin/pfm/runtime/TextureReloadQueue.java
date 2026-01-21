package com.unlikepaladin.pfm.runtime;

// Java
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import com.unlikepaladin.pfm.data.materials.StoneVariantRegistry;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.ducks.PFMSpriteAtlasTexturesExtensions;
import com.unlikepaladin.pfm.ducks.PFMSpriteExtensions;
import com.unlikepaladin.pfm.mixin.PFMSpriteAtlasTextureAccessor;
import com.unlikepaladin.pfm.utilities.PFMFileUtil;
import com.unlikepaladin.pfm.utilities.Version;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.metadata.AnimationResourceMetadata;
import net.minecraft.client.texture.*;
import net.minecraft.client.util.PngFile;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public final class TextureReloadQueue {

    public static void recolorAndWriteImage(Identifier identifier, BufferedImage base, Map<Integer, Integer> palette) {
        try {
            BufferedImage recolored = new BufferedImage(base.getWidth(), base.getHeight(), BufferedImage.TYPE_INT_ARGB);
            for(int y = 0; y < base.getHeight(); y++) {
                for(int x = 0; x < base.getWidth(); x++) {
                    int srcArgb = base.getRGB(x, y);
                    int srcArgbNoAlpha = srcArgb | 0xFF000000;  // Set alpha to 255 for palette lookup
                    int srcAlpha = (srcArgb >> 24) & 0xFF;      // Preserve original alpha

                    int mappedArgb;
                    if (palette.containsKey(srcArgbNoAlpha)) {
                        mappedArgb = palette.get(srcArgbNoAlpha);
                    } else {
                        // Use lab color distance to find closest color in palette, black magic
                        mappedArgb = findClosestColorLab(srcArgbNoAlpha, palette);
                        palette.put(srcArgbNoAlpha, mappedArgb);
                    }

                    // Apply original alpha to the mapped color
                    int finalArgb = (srcAlpha << 24) | (mappedArgb & 0x00FFFFFF);

                    recolored.setRGB(x, y, finalArgb);
                }
            }

            // write image
            String file = PFMRuntimeResources.getAssetPackDirectory().resolve("assets/pfm/textures/").toString();
            try {
                Files.createDirectories(Path.of(file));
                Files.createDirectory(Path.of(file+"/block"));
            } catch (FileAlreadyExistsException ignored) {
            } catch (IOException e) {
                PaladinFurnitureMod.GENERAL_LOGGER.error("Failed to create directories for recolored image {}: {}", identifier, e.getMessage());
            }


            ModelHelper.writeBufferedImageToFile(recolored, file +'/'+ identifier.getPath() + ".png");
        } catch(Throwable e) {
            PaladinFurnitureMod.GENERAL_LOGGER.error("Failed to generate recolored image {}: {}", identifier, e.getMessage());
            throw new RuntimeException(e);
        }
        requestReload(identifier);
    }

    public static final List<Identifier> list = Collections.synchronizedList(new ArrayList<>());

    public static void requestReload(Identifier id) {
        if (id != null) list.add(id);
    }

    @ExpectPlatform
    public static void registerTextureReload() {

    }


    static void reloadSingleSprite(ResourceManager resourceManager, SpriteAtlasTexture spriteAtlas, Identifier id) throws IOException {
        Identifier path = ModelHelper.getTextureSpritePath(id);
        Optional<Resource> optionalResource = resourceManager.getResource(path);

        if (optionalResource.isEmpty()) {
            PaladinFurnitureMod.GENERAL_LOGGER.error("Failed to reload texture at {}, resource not found", id);
            return;
        }

        Resource resource = optionalResource.get();
        Sprite original = spriteAtlas.getSprite(id);

        AnimationResourceMetadata animationResourceMetadata = resource.getMetadata().decode(AnimationResourceMetadata.READER)
                .orElse(AnimationResourceMetadata.EMPTY);

        PngFile pngFile = new PngFile(path::toString, resource.getInputStream());
        Sprite.Info info = new Sprite.Info(id, pngFile.width, pngFile.height, animationResourceMetadata);

        SpriteCoordinates coords = PFMSpriteRegistry.PFM_SPRITE_COORDINATES.get(id);
        int x, y;
        int atlasWidth, atlasHeight;

        if (coords != null) {
            x = coords.x;
            y = coords.y;
            atlasWidth = coords.atlasWidth;
            atlasHeight = coords.atlasHeight;
        } else {
            x = original.getX();
            y = original.getY();
            atlasWidth = Math.round(original.getX() / original.getMinU());
            atlasHeight = Math.round(original.getY() / original.getMinV());
        }
        int mipMapSizeConfig = MinecraftClient.getInstance().options.getMipmapLevels().getValue();
        Integer maxLevelWhenStiching = ((PFMSpriteAtlasTexturesExtensions)spriteAtlas).pfm$getMaxLevel();
        int mipMapSize = maxLevelWhenStiching != null ? Math.min(Math.min(mipMapSizeConfig, ((PFMSpriteExtensions)original).pfm$getMipmapLevel()), maxLevelWhenStiching) : Math.min(mipMapSizeConfig, ((PFMSpriteExtensions)original).pfm$getMipmapLevel());

        Sprite newSprite = ((PFMSpriteAtlasTextureAccessor)spriteAtlas).invoke$loadSprite(resourceManager, info, atlasWidth, atlasHeight, mipMapSize, x, y);

        ((PFMSpriteAtlasTextureAccessor)spriteAtlas).pfm$getSprites().put(id, newSprite);

        newSprite.upload();
        original.close();
    }

    public static void reloadSpritesOnClientThread(List<Identifier> id) {
        TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
        ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
        AbstractTexture abstractTexture = textureManager.getTexture(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
        try {
            if (abstractTexture instanceof SpriteAtlasTexture spriteAtlas) {
                spriteAtlas.bindTexture();

                for (Identifier spriteId : id) {
                    reloadSingleSprite(resourceManager, spriteAtlas, spriteId);
                }
            }
        } catch (IOException e) {
            PaladinFurnitureMod.GENERAL_LOGGER.error("Failed to reload texture at {}", id, e);
        }

        MinecraftClient.getInstance().execute(
                () -> {
                    MinecraftClient.getInstance().worldRenderer.reload();
                    List<Identifier> variants = new ArrayList<>();

                    WoodVariantRegistry.getVariants().stream().sorted().forEach(woodVariant -> variants.add(woodVariant.identifier));
                    StoneVariantRegistry.getVariants().stream().sorted().forEach(stoneVariant -> variants.add(stoneVariant.identifier));

                    try {
                        Path output = PFMRuntimeResources.getAssetPackDirectory();
                        PFMGenerator.PFMCache.createAndWriteCacheToDisk(output, variants, PaladinFurnitureMod.GENERAL_LOGGER);
                    } catch (IOException ignored) {

                    }
                }
        );

    }

    public record SpriteCoordinates(int x, int y, int width, int height, int atlasWidth, int atlasHeight) {
    }

    /*
    Color distance calculation in CIE Lab color space
    Reference: https://en.wikipedia.org/wiki/CIELAB_color_space#CIEDE2000
    1. Convert sRGB to linear RGB
    2. Convert linear RGB to XYZ (D65)
    3. Convert XYZ to Lab
    4. Compute Euclidean distance in Lab space
    Code generated with the help of ChatGPT, i literally don't know color science
    this is black magic to me
     */
    // sRGB (0–255) → linear RGB (0–1)
    private static double pivotRgb(double n) {
        n /= 255.0;
        return (n <= 0.04045)
                ? (n / 12.92)
                : Math.pow((n + 0.055) / 1.055, 2.4);
    }

    // XYZ → Lab helper
    private static double pivotXyz(double n) {
        return (n > 0.008856)
                ? Math.cbrt(n)
                : (7.787 * n + 16.0 / 116.0);
    }

    // ARGB → Lab (D65)
    private static double[] argbToLab(int argb) {
        int r8 = (argb >> 16) & 0xFF;
        int g8 = (argb >> 8) & 0xFF;
        int b8 = argb & 0xFF;

        // Linear RGB
        double r = pivotRgb(r8);
        double g = pivotRgb(g8);
        double b = pivotRgb(b8);

        // RGB → XYZ (D65)
        double x = r * 0.4124564 + g * 0.3575761 + b * 0.1804375;
        double y = r * 0.2126729 + g * 0.7151522 + b * 0.0721750;
        double z = r * 0.0193339 + g * 0.1191920 + b * 0.9503041;

        // Normalize by reference white (D65)
        x /= 0.95047;
        y /= 1.00000;
        z /= 1.08883;

        double fx = pivotXyz(x);
        double fy = pivotXyz(y);
        double fz = pivotXyz(z);

        double L = Math.max(0, 116 * fy - 16);
        double a = 500 * (fx - fy);
        double b2 = 200 * (fy - fz);

        return new double[]{L, a, b2};
    }

    private static double deltaE(double[] lab1, double[] lab2) {
        double dL = lab1[0] - lab2[0];
        double da = lab1[1] - lab2[1];
        double db = lab1[2] - lab2[2];
        return dL * dL + da * da + db * db; // squared distance (faster)
    }

    private static int findClosestColorLab(int srcArgb, Map<Integer, Integer> palette) {
        if (palette.isEmpty()) {
            return srcArgb;
        }

        double[] srcLab = argbToLab(srcArgb);

        int closestColor = srcArgb;
        double minDistance = Double.MAX_VALUE;

        for (Map.Entry<Integer, Integer> entry : palette.entrySet()) {
            int palArgb = entry.getKey();

            double[] palLab = argbToLab(palArgb);
            double dist = deltaE(srcLab, palLab);

            if (dist < minDistance) {
                minDistance = dist;
                closestColor = entry.getValue();
            }
        }

        return closestColor;
    }
}
