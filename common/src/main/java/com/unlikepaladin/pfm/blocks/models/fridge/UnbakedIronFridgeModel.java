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
public record UnbakedIronFridgeModel(ModelVariant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedIronFridgeModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(ModelVariant.MAP_CODEC.forGetter(UnbakedIronFridgeModel::variant))
                            .apply(instance, UnbakedIronFridgeModel::new));

    public static final Codec<UnbakedIronFridgeModel> CODEC = MAP_CODEC.codec();

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

    public static final List<Identifier> ALL_MODEL_IDS = new ArrayList<>() {
        {
            for (String part : FRIDGE_MODEL_PARTS_BASE) {
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, part));
            }
        }
    };

    public static final Identifier IRON_FRIDGE_ID = Identifier.of(PaladinFurnitureMod.MOD_ID, "block/iron_fridge");
    public static final List<Identifier> IRON_FRIDGE_MODEL_IDS = new ArrayList<>() { {
        add(IRON_FRIDGE_ID);
    }};



    @Override
    public BlockStateModel bake(Baker baker){
        ModelBakeSettings settings = variant.modelState().asModelBakeSettings();

        Map<String,BlockModelPart> bakedModels = new LinkedHashMap<>();
        for (String modelPart : FRIDGE_MODEL_PARTS_BASE) {
            bakedModels.put(modelPart, GeometryBakedModel.create(baker, Identifier.of(PaladinFurnitureMod.MOD_ID, modelPart), settings));
        }

        return getBakedModel(settings, bakedModels, FRIDGE_MODEL_PARTS_BASE);
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
