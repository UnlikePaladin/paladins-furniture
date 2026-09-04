package com.unlikepaladin.pfm.client.forge;

import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.renderer.block.dispatch.SingleVariant;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.resources.Identifier;

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
        private final BiFunction<ResolvedModel, ModelBaker, BlockStateModel> bake;

        public SimpleUnbakedExtraModel(Identifier model, BiFunction<ResolvedModel, ModelBaker, BlockStateModel> bake) {
            this.model = model;
            this.bake = bake;
        }

        public static SimpleUnbakedExtraModel blockStateModel(Identifier model) {
            return blockStateModel(model, BlockModelRotation.IDENTITY);
        }

        public static SimpleUnbakedExtraModel blockStateModel(Identifier model, ModelState settings) {
            return new SimpleUnbakedExtraModel(model, (baked, baker) -> {
                TextureSlots textures = baked.getTopTextureSlots();
                return new SingleVariant(new SimpleModelWrapper(
                        baked.bakeTopGeometry(textures, baker, settings),
                        baked.getTopAmbientOcclusion(),
                        baked.resolveParticleMaterial(textures, baker)
                ));
            });
        }

        @Override
        public void resolveDependencies(ResolvableModel.Resolver resolver) {
            resolver.markDependency(model);
        }

        @Override
        public BlockStateModel bake(ModelBaker baker) {
            return bake.apply(baker.getModel(model), baker);
        }
    }

}