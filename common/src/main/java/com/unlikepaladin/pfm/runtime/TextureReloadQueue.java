package com.unlikepaladin.pfm.runtime;

// Java
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import com.unlikepaladin.pfm.data.materials.StoneVariantRegistry;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class TextureReloadQueue {
    /**
     * Finds the closest color in the palette using Euclidean distance in RGB space.
     * @param srcArgb The source color in ARGB format (0xAARRGGBB)
     * @param palette The color palette mapping (ARGB -> ARGB)
     * @return The closest mapped color from the palette, or the original if palette is empty
     */
    private static int findClosestColor(int srcArgb, Map<Integer, Integer> palette) {
        if (palette.isEmpty()) {
            return srcArgb;
        }

        // Extract RGB from ARGB format (0xAARRGGBB)
        int srcR = (srcArgb >> 16) & 0xFF;
        int srcG = (srcArgb >> 8) & 0xFF;
        int srcB = srcArgb & 0xFF;

        int closestColor = srcArgb;
        double minDistance = Double.MAX_VALUE;

        for (Map.Entry<Integer, Integer> entry : palette.entrySet()) {
            int paletteKey = entry.getKey();
            // Palette keys are in ARGB format (0xAARRGGBB)
            int palR = (paletteKey >> 16) & 0xFF;
            int palG = (paletteKey >> 8) & 0xFF;
            int palB = paletteKey & 0xFF;

            // Euclidean distance in RGB space
            double distance = Math.sqrt(
                    Math.pow(srcR - palR, 2) +
                    Math.pow(srcG - palG, 2) +
                    Math.pow(srcB - palB, 2)
            );

            if (distance < minDistance) {
                minDistance = distance;
                closestColor = entry.getValue();
            }
        }

        return closestColor;
    }

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
                        // Use Euclidean distance to find the closest color, cache it for future use
                        mappedArgb = findClosestColor(srcArgbNoAlpha, palette);
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
        Resource resource = resourceManager.getResource(path);

        Sprite original = spriteAtlas.getSprite(id);

        AnimationResourceMetadata animationResourceMetadata = resource.getMetadata(AnimationResourceMetadata.READER);
        if (animationResourceMetadata == null) {
            animationResourceMetadata = AnimationResourceMetadata.EMPTY;
        }

        PngFile pngFile = new PngFile(resource.toString(), resource.getInputStream());
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
        int mipMapSize = ((PFMSpriteExtensions)original).pfm$getMipmapLevel();

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
}
