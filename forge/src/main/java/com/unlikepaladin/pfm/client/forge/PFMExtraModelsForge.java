package com.unlikepaladin.pfm.client.forge;

import net.minecraft.client.render.model.*;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class PFMExtraModelsForge {
    public static Map<Identifier, BlockStateModel.Unbaked> unbakedModels = new ConcurrentHashMap<>() ;


    public static void registerExtraModels(List<Identifier> models) {
        for (Identifier id : models) {
            unbakedModels.put(id, SimpleUnbakedExtraModel.blockStateModel(id));
        }
    }

    // Based on SimpleUnbakedModel from Fabric API's Model Loading API
    protected static final class SimpleUnbakedExtraModel implements BlockStateModel.Unbaked {
        private final Identifier model;
        private final BiFunction<BakedSimpleModel, Baker, BlockStateModel> bake;

        public SimpleUnbakedExtraModel(Identifier model, BiFunction<BakedSimpleModel, Baker, BlockStateModel> bake) {
            this.model = model;
            this.bake = bake;
        }

        public static SimpleUnbakedExtraModel blockStateModel(Identifier model) {
            return blockStateModel(model, ModelRotation.IDENTITY);
        }

        public static SimpleUnbakedExtraModel blockStateModel(Identifier model, ModelBakeSettings settings) {
            return new SimpleUnbakedExtraModel(model, (baked, baker) -> {
                ModelTextures textures = baked.getTextures();
                return new SimpleBlockStateModel(new GeometryBakedModel(
                        baked.bakeGeometry(textures, baker, settings),
                        baked.getAmbientOcclusion(),
                        baked.getParticleTexture(textures, baker)
                ));
            });
        }

        @Override
        public void resolve(ResolvableModel.Resolver resolver) {
            resolver.markDependency(model);
        }

        @Override
        public BlockStateModel bake(Baker baker) {
            return bake.apply(baker.getModel(model), baker);
        }
    }

}