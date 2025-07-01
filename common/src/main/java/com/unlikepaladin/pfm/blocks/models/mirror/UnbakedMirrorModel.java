package com.unlikepaladin.pfm.blocks.models.mirror;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.logTable.UnbakedLogTableModel;
import com.unlikepaladin.pfm.client.model.PFMUnbakedBlockStateModel;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.util.Identifier;

import java.util.*;

@Environment(EnvType.CLIENT)
public record UnbakedMirrorModel(ModelVariant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedMirrorModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(ModelVariant.MAP_CODEC.forGetter(UnbakedMirrorModel::variant))
                            .apply(instance, UnbakedMirrorModel::new));

    public static final Codec<UnbakedMirrorModel> CODEC = MAP_CODEC.codec();

    public static final String[] BASE_MODEL_PARTS = new String[] {"block/mirror/mirror_base", "block/mirror/mirror_top", "block/mirror/mirror_bottom", "block/mirror/mirror_left","block/mirror/mirror_right", "block/mirror/mirror_right_top", "block/mirror/mirror_left_top", "block/mirror/mirror_right_bottom", "block/mirror/mirror_left_bottom"};
    public static final Identifier[] DEFAULT_TEXTURES = new Identifier[] {Identifier.of("minecraft","block/white_concrete"), Identifier.of("minecraft","block/glass"), Identifier.of("pfm","block/mirror")};
    public static final Identifier[] MIRROR_MODEL_IDS = {Identifier.of(PaladinFurnitureMod.MOD_ID, "block/white_mirror"), Identifier.of(PaladinFurnitureMod.MOD_ID, "block/gray_mirror")};
    public static final Identifier MIRROR_ID = Identifier.of(PaladinFurnitureMod.MOD_ID, "block/mirror");

    public static final List<Identifier> ALL_MODEL_IDS = new ArrayList<>() {
        {
            for (String part : BASE_MODEL_PARTS) {
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, part));
            }
            for (String part : BASE_MODEL_PARTS) {
                part = part.replace("mirror", "gray_mirror");
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, part));
            }
        }
    };

    @Override
    public BlockStateModel bake(Baker baker){
        ModelBakeSettings settings = variant.modelState().asModelBakeSettings();

        Map<String,BlockModelPart> bakedModels = new LinkedHashMap<>();
        for (String modelPartName: BASE_MODEL_PARTS) {
            String part = modelPartName.replace("mirror", "gray_mirror");
            bakedModels.put(modelPartName, GeometryBakedModel.create(baker, Identifier.of(PaladinFurnitureMod.MOD_ID, part), settings));
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
