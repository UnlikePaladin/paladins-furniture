package com.unlikepaladin.pfm.blocks.models.fridge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.model.PFMUnbakedBlockStateModel;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.util.Identifier;

import java.util.*;

@Environment(EnvType.CLIENT)
public record UnbakedFreezerModel(ModelVariant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedFreezerModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(ModelVariant.MAP_CODEC.forGetter(UnbakedFreezerModel::variant))
                            .apply(instance, UnbakedFreezerModel::new));

    public static final Codec<UnbakedFreezerModel> CODEC = MAP_CODEC.codec();

    public static final List<String> FREEZER_MODEL_PARTS_BASE = new ArrayList<>() {
        {
            add("block/white_fridge/freezer_single");
            add("block/white_fridge/freezer");
            add("block/white_fridge/freezer_single_open");
            add("block/white_fridge/freezer_open");
        }
    };

    public static final List<Identifier> ALL_MODEL_IDS = new ArrayList<>() {
        {
            for (String part : FREEZER_MODEL_PARTS_BASE) {
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, part));
            }
            for (String part : FREEZER_MODEL_PARTS_BASE) {
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, part.replaceAll("white", "gray")));
            }
        }
    };

    public static final Identifier FREEZER_MODEL_ID = Identifier.of(PaladinFurnitureMod.MOD_ID, "block/freezer");

    public static final List<Identifier> FREEZER_MODEL_IDS = new ArrayList<>() { {
        add(Identifier.of(PaladinFurnitureMod.MOD_ID, "block/white_freezer"));
        add(Identifier.of(PaladinFurnitureMod.MOD_ID, "block/gray_freezer"));
    }};


    @Override
    public BlockStateModel bake(Baker baker){
        ModelBakeSettings settings = variant.modelState().asModelBakeSettings();
        Identifier id = variant.modelId();

        Map<String,BlockModelPart> bakedModels = new LinkedHashMap<>();
        for (String modelPart : FREEZER_MODEL_PARTS_BASE) {
            if (id.getPath().contains("gray"))
                modelPart = modelPart.replaceAll("white", "gray");
            bakedModels.put(modelPart, GeometryBakedModel.create(baker, Identifier.of(PaladinFurnitureMod.MOD_ID, modelPart), settings));
        }
        return getBakedModel(settings, bakedModels, bakedModels.keySet().stream().toList());
    }

    @ExpectPlatform
    public static BlockStateModel getBakedModel(ModelBakeSettings settings, Map<String,BlockModelPart> bakedModels, List<String> MODEL_PARTS) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolve(Resolver resolver) {
        for (Identifier c : ALL_MODEL_IDS)
            resolver.markDependency(c);
    }

    @Override
    public MapCodec<? extends BlockStateModel.Unbaked> codec() {
        return MAP_CODEC;
    }
}
