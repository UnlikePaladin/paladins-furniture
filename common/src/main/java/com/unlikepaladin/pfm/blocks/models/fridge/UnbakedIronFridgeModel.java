package com.unlikepaladin.pfm.blocks.models.fridge;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class UnbakedIronFridgeModel implements UnbakedModel {
    public static final List<String> FRIDGE_MODEL_PARTS_BASE = new ArrayList<String>() {
        {
            add("block/iron_fridge/iron_fridge_single");
            add("block/iron_fridge/iron_fridge_top");
            add("block/iron_fridge/iron_fridge_middle");
            add("block/iron_fridge/iron_fridge_bottom");
            add("block/iron_fridge/iron_fridge");
            add("block/iron_fridge/iron_fridge_single_open");
            add("block/iron_fridge/iron_fridge_top_open");
            add("block/iron_fridge/iron_fridge_middle_open");
            add("block/iron_fridge/iron_fridge_bottom_open");
            add("block/iron_fridge/iron_fridge_open");
        }
    };

    public static final List<ResourceLocation> ALL_MODEL_IDS = new ArrayList<ResourceLocation>() {
        {
            for (String part : FRIDGE_MODEL_PARTS_BASE) {
                add(new ResourceLocation(PaladinFurnitureMod.MOD_ID, part));
            }
        }
    };


    private static final ResourceLocation PARENT = new ResourceLocation("block/block");
    private final Material frameTex;

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Collections.singleton(PARENT);
    }

    @Override
    public Collection<Material> getMaterials(Function<ResourceLocation, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        return Collections.singleton(frameTex);
    }

    public static final List<ResourceLocation> IRON_FRIDGE_MODEL_IDS = new ArrayList<ResourceLocation>() { {
        add(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/iron_fridge"));
    }};

    public UnbakedIronFridgeModel() {
        this.frameTex = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation("minecraft", "block/iron_block"));
    }
    @Nullable
    @Override
    public BakedModel bake(ModelBakery loader, Function<Material, TextureAtlasSprite> textureGetter, ModelState rotationContainer, ResourceLocation modelId) {
        Map<String, BakedModel> bakedModels = new LinkedHashMap<>();
        for (String modelPart : FRIDGE_MODEL_PARTS_BASE) {
            bakedModels.put(modelPart, loader.bake(new ResourceLocation(PaladinFurnitureMod.MOD_ID, modelPart), rotationContainer));
        }
        return getBakedModel(textureGetter.apply(frameTex), rotationContainer, bakedModels, FRIDGE_MODEL_PARTS_BASE);
    }

    @ExpectPlatform
    public static BakedModel getBakedModel(TextureAtlasSprite frame, ModelState settings, Map<String, BakedModel> bakedModels, List<String> MODEL_PARTS) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }
}
