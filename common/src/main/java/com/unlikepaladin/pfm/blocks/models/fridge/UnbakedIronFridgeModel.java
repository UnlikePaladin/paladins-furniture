package com.unlikepaladin.pfm.blocks.models.fridge;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.ModelBaker;
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
    public static final List<String> FRIDGE_MODEL_PARTS_BASE = new ArrayList<>() {
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

    public static final List<ResourceLocation> ALL_MODEL_IDS = new ArrayList<>() {
        {
            for (String part : FRIDGE_MODEL_PARTS_BASE) {
                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, part));
            }
        }
    };


    private static final ResourceLocation PARENT = ResourceLocation.parse("block/block");
    private final Material frameTex;

    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        return List.of(frameTex);
    }

    public static final List<ResourceLocation> IRON_FRIDGE_MODEL_IDS = new ArrayList<>() { {
        add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/iron_fridge"));
    }};

    public UnbakedIronFridgeModel() {
        this.frameTex = new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath("minecraft", "block/iron_block"));
    }
    @Nullable
    @Override
    public BakedModel bake(ModelBaker loader, Function<Material, TextureAtlasSprite> textureGetter, ModelState rotationContainer) {
        Map<String, BakedModel> bakedModels = new LinkedHashMap<>();
        for (String modelPart : FRIDGE_MODEL_PARTS_BASE) {
            bakedModels.put(modelPart, loader.bake(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, modelPart), rotationContainer));
        }
        return getBakedModel(textureGetter.apply(frameTex), rotationContainer, bakedModels, FRIDGE_MODEL_PARTS_BASE);
    }

    @ExpectPlatform
    public static BakedModel getBakedModel(TextureAtlasSprite frame, ModelState settings, Map<String, BakedModel> bakedModels, List<String> MODEL_PARTS) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolve(Resolver resolver) {
        for (ResourceLocation c : ALL_MODEL_IDS)
            resolver.resolve(c);
    }
}
