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
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Environment(EnvType.CLIENT)
public record UnbakedFridgeModel(ModelVariant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedFridgeModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(ModelVariant.MAP_CODEC.forGetter(UnbakedFridgeModel::variant))
                            .apply(instance, UnbakedFridgeModel::new));

    public static final Codec<UnbakedFridgeModel> CODEC = MAP_CODEC.codec();

    public static final List<String> FRIDGE_MODEL_PARTS_BASE = new ArrayList<>() {
        {
            add("block/white_fridge/fridge_single");
            add("block/white_fridge/fridge_top");
            add("block/white_fridge/fridge_middle");
            add("block/white_fridge/fridge_bottom");
            add("block/white_fridge/fridge");
            add("block/white_fridge/fridge_middle_freezer");
            add("block/white_fridge/fridge_single_open");
            add("block/white_fridge/fridge_top_open");
            add("block/white_fridge/fridge_middle_open");
            add("block/white_fridge/fridge_bottom_open");
            add("block/white_fridge/fridge_open");
            add("block/white_fridge/fridge_middle_freezer_open");
        }
    };

    public static final List<Identifier> ALL_MODEL_IDS = new ArrayList<>() {
        {
            for (String part : FRIDGE_MODEL_PARTS_BASE) {
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, part));
            }
            for (String part : FRIDGE_MODEL_PARTS_BASE) {
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, part.replaceAll("white", "gray")));
            }
        }
    };

    public static final Identifier FRIDGE_MODEL_ID = Identifier.of(PaladinFurnitureMod.MOD_ID, "block/fridge");
    public static final List<Identifier> FRIDGE_MODEL_IDS = new ArrayList<>() { {
        add(Identifier.of(PaladinFurnitureMod.MOD_ID, "block/white_fridge"));
        add(Identifier.of(PaladinFurnitureMod.MOD_ID, "block/gray_fridge"));
    }};

    @Nullable
    @Override
    public BlockStateModel bake(Baker baker){
        ModelBakeSettings settings = variant.modelState().asModelBakeSettings();
        Identifier id = variant.modelId();

        Map<String,BlockModelPart> bakedModels = new LinkedHashMap<>();
        for (String modelPart : FRIDGE_MODEL_PARTS_BASE) {
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
    public MapCodec<? extends BlockStateModel.Unbaked> getCodec() {
        return MAP_CODEC;
    }
}
