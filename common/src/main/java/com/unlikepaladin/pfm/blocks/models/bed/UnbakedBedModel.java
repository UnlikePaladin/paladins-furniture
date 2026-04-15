package com.unlikepaladin.pfm.blocks.models.bed;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.runtime.PFMBakedModelContainer;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;


@Environment(EnvType.CLIENT)
public class UnbakedBedModel implements UnbakedModel {
    public static final ResourceLocation[] BED_MODEL_PARTS_BASE = new ResourceLocation[] {
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/mattresses/red_foot_mattress"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/mattresses/red_head_mattress"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/foot/simple_bed_foot"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/head/simple_bed_head"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/foot/simple_bed_foot_right"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/foot/simple_bed_foot_left"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/head/simple_bed_head_right"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/head/simple_bed_head_left"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/bunk/foot/simple_bed_foot_right"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/bunk/foot/simple_bed_foot_left"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/bunk/head/simple_bed_head"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/full/simple_bed"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_bed/mattresses/red_foot_mattress"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_bed/mattresses/red_head_mattress"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/foot/classic_bed_foot"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/head/classic_bed_head"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/foot/classic_bed_foot_right"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/foot/classic_bed_foot_left"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/head/classic_bed_head_right"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/head/classic_bed_head_left"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/bunk/foot/classic_bed_foot_right"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/bunk/foot/classic_bed_foot_left"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/bunk/head/simple_bed_head"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/full/classic_bed"),
    };

    private static final ResourceLocation PARENT = ResourceLocation.parse("block/block");
    public static final ResourceLocation BED_MODEL_ID = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed");
    public static final List<ResourceLocation> BED_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                int i = 0;
                for (DyeColor dyeColor : DyeColor.values()) {
                    if (i > 15)
                        break;
                    add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_" + dyeColor.getName() + "_simple_bed"));
                    i++;
                }
            }
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                int i = 0;
                for (DyeColor dyeColor : DyeColor.values()) {
                    if (i > 15)
                        break;
                    add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_" + dyeColor.getName() + "_classic_bed"));
                    i++;
                }
            }
            add(BED_MODEL_ID);
        }
    };

    public Collection<Material> getTextureDependencies(Function<ResourceLocation, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        return Collections.emptyList();
    }

    public static Tuple<BakedModel, BakedModel> inventoryModels = new Tuple<>(null,null);
    @Nullable
    @Override
    public BakedModel bake(ModelBaker loader, Function<Material, TextureAtlasSprite> textureGetter, ModelState rotationContainer) {
        if (PFMRuntimeResources.modelCacheMap.containsKey(BED_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(BED_MODEL_ID).getCachedModelParts().containsKey(rotationContainer))
            return getBakedModel(BED_MODEL_ID, rotationContainer, PFMRuntimeResources.modelCacheMap.get(BED_MODEL_ID).getCachedModelParts().get(rotationContainer));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(BED_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(BED_MODEL_ID, new PFMBakedModelContainer());

        List<BakedModel> bakedModelList = new ArrayList<>();
        for (ResourceLocation modelPart : BED_MODEL_PARTS_BASE) {
            BakedModel model = loader.bake(modelPart, rotationContainer);
            bakedModelList.add(model);
            if (modelPart.getPath().contains("full")) {
                if (modelPart.getPath().contains("simple"))
                    inventoryModels.setA(model);
                else
                    inventoryModels.setB(model);
            }
        }

        PFMRuntimeResources.modelCacheMap.get(BED_MODEL_ID).getCachedModelParts().put(rotationContainer, bakedModelList);
        return getBakedModel(BED_MODEL_ID, rotationContainer, bakedModelList);
    }

    @ExpectPlatform
    public static BakedModel getBakedModel(ResourceLocation model, ModelState settings, List<BakedModel> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        for (ResourceLocation c : BED_MODEL_PARTS_BASE)
            resolver.resolve(c);
    }
}