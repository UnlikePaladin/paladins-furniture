package com.unlikepaladin.pfm.blocks.models.mirror;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class UnbakedMirrorModel implements UnbakedModel {
    public static final String[] BASE_MODEL_PARTS = new String[] {"block/mirror/mirror_base", "block/mirror/mirror_top", "block/mirror/mirror_bottom", "block/mirror/mirror_left","block/mirror/mirror_right", "block/mirror/mirror_right_top", "block/mirror/mirror_left_top", "block/mirror/mirror_right_bottom", "block/mirror/mirror_left_bottom"};
    public static final ResourceLocation[] DEFAULT_TEXTURES = new ResourceLocation[] {ResourceLocation.fromNamespaceAndPath("minecraft","block/white_concrete"), ResourceLocation.fromNamespaceAndPath("minecraft","block/glass"), ResourceLocation.fromNamespaceAndPath("pfm","block/mirror")};
    private static final ResourceLocation PARENT = ResourceLocation.parse("block/block");
    public static final ResourceLocation[] MIRROR_MODEL_IDS = {ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/white_mirror"), ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/gray_mirror")};
    private final List<String> MODEL_PARTS;
    protected final Material reflectTex;
    protected final Material glassTex;
    protected final Material frameTex;
    public UnbakedMirrorModel(ResourceLocation reflect, ResourceLocation defaultFrameTexture, ResourceLocation glass, List<String> modelParts, DyeColor color) {
        this.reflectTex = new Material(InventoryMenu.BLOCK_ATLAS, reflect);
        this.frameTex = new Material(InventoryMenu.BLOCK_ATLAS, defaultFrameTexture);
        this.glassTex = new Material(InventoryMenu.BLOCK_ATLAS, glass);
        for(String modelPartName : BASE_MODEL_PARTS){
            String s = modelPartName;
            if (color != DyeColor.WHITE)
                s = s.replace("mirror", color.getName()+"_mirror");
            modelParts.add(s);
        }
        MODEL_PARTS = modelParts;
    }

    public static final List<ResourceLocation> ALL_MODEL_IDS = new ArrayList<>() {
        {
            for (String part : BASE_MODEL_PARTS) {
                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, part));
            }
            for (String part : BASE_MODEL_PARTS) {
                part = part.replace("mirror", "gray_mirror");
                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, part));
            }
        }
    };

    public Collection<Material> getTextureDependencies(Function<ResourceLocation, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        List<Material> list = new ArrayList<>(2);
        list.add(glassTex);
        list.add(frameTex);
        list.add(reflectTex);
        return list;
    }

    @Nullable
    @Override
    public BakedModel bake(ModelBaker loader, Function<Material, TextureAtlasSprite> textureGetter, ModelState rotationContainer) {
        Map<String,BakedModel> bakedModels = new LinkedHashMap<>();
        for (String modelPartName: MODEL_PARTS) {
            bakedModels.put(modelPartName, loader.bake(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, modelPartName), rotationContainer));
        }
        return getBakedModel(textureGetter.apply(frameTex), textureGetter.apply(glassTex), textureGetter.apply(reflectTex), rotationContainer, bakedModels, MODEL_PARTS);
    }

    @ExpectPlatform
    public static BakedModel getBakedModel(TextureAtlasSprite frame, TextureAtlasSprite glassTex, TextureAtlasSprite reflectTex, ModelState settings, Map<String,BakedModel> bakedModels, List<String> MODEL_PARTS) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        for (String c : MODEL_PARTS)
            resolver.resolve(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, c));
    }
}
