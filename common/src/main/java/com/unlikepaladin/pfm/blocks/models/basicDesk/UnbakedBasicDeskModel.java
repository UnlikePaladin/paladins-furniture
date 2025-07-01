package com.unlikepaladin.pfm.blocks.models.basicDesk;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.model.PFMUnbakedBlockStateModel;
import com.unlikepaladin.pfm.data.materials.StoneVariant;
import com.unlikepaladin.pfm.data.materials.StoneVariantRegistry;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.runtime.PFMBakedModelContainer;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public record UnbakedBasicDeskModel(ModelVariant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedBasicDeskModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(ModelVariant.MAP_CODEC.forGetter(UnbakedBasicDeskModel::variant))
                            .apply(instance, UnbakedBasicDeskModel::new));

    public static final Codec<UnbakedBasicDeskModel> CODEC = MAP_CODEC.codec();

    public static final Identifier[] BASIC_MODEL_PARTS_BASE = new Identifier[] {
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/basic_desk/basic_desk_base"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/basic_desk/basic_desk_north_west"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/basic_desk/basic_desk_north_east"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/basic_desk/basic_desk_south_west"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/basic_desk/basic_desk_south_east")
    };

    public static final Identifier TABLE_MODEL_ID = Identifier.of(PaladinFurnitureMod.MOD_ID, "block/desk_basic");
    public static final List<Identifier> MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_desk_basic"));
                if (variant.hasStripped())
                    add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.asString() + "_desk_basic"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_desk_basic"));
            }
            add(TABLE_MODEL_ID);
        }
    };

    @Override
    public void resolve(Resolver resolver) {
        for (Identifier c : BASIC_MODEL_PARTS_BASE)
            resolver.markDependency(c);
    }

    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public BlockStateModel bake(Baker baker){
        ModelBakeSettings settings = variant.modelState().asModelBakeSettings();
        ModelSettings itemSettings = ModelSettings.resolveSettings(baker, baker.getModel(BASIC_MODEL_PARTS_BASE[0]), baker.getModel(BASIC_MODEL_PARTS_BASE[0]).getTextures());

        if (PFMRuntimeResources.modelCacheMap.containsKey(TABLE_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(TABLE_MODEL_ID).getCachedModelParts().containsKey(settings))
            return getBakedModel(TABLE_MODEL_ID, settings, itemSettings, PFMRuntimeResources.modelCacheMap.get(TABLE_MODEL_ID).getCachedModelParts().get(settings));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(TABLE_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(TABLE_MODEL_ID, new PFMBakedModelContainer());

        List<BlockModelPart> bakedModelList = new ArrayList<>();
        for (Identifier modelPart : BASIC_MODEL_PARTS_BASE) {
            bakedModelList.add(GeometryBakedModel.create(baker, modelPart, settings));
        }

        PFMRuntimeResources.modelCacheMap.get(TABLE_MODEL_ID).getCachedModelParts().put(settings, bakedModelList);
        return getBakedModel(TABLE_MODEL_ID, settings, itemSettings, bakedModelList);
    }

    @ExpectPlatform
    public static BlockStateModel getBakedModel(Identifier modelId, ModelBakeSettings settings, ModelSettings itemSettings, List<BlockModelPart> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public MapCodec<? extends BlockStateModel.Unbaked> codec() {
        return MAP_CODEC;
    }
}