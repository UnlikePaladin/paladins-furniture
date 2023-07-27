package com.unlikepaladin.pfm.blocks.models.basicLamp.fabric;

import com.unlikepaladin.pfm.blocks.BasicLampBlock;
import com.unlikepaladin.pfm.blocks.blockentities.LampBlockEntity;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class FabricBasicLampModel extends AbstractBakedModel implements FabricBakedModel {
    private final List<String> modelParts;
    private final Map<WoodVariant, Map<String, BakedModel>> bakedModels;
    private final Map<WoodVariant, Sprite> textureMap;
    public FabricBasicLampModel(Map<WoodVariant, Sprite> textures, ModelBakeSettings settings, Map<WoodVariant, Map<String, BakedModel>> bakedModels, List<String> modelParts) {
        super(textures.get(WoodVariantRegistry.OAK), settings, new HashMap<>());
        this.modelParts = modelParts;
        this.textureMap = textures;
        this.bakedModels = bakedModels;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        WoodVariant variant = WoodVariantRegistry.OAK;
        BlockEntity entity = blockView.getBlockEntity(pos);
        int onOffset = state.get(Properties.LIT) ? 1 : 0;
        if (entity instanceof LampBlockEntity) {
            variant = ((LampBlockEntity) entity).getVariant();
        }
        boolean up = blockView.getBlockState(pos.up()).getBlock() instanceof BasicLampBlock;
        boolean down = blockView.getBlockState(pos.down()).getBlock() instanceof BasicLampBlock;
        if (up && down) {
            ((FabricBakedModel)bakedModels.get(variant).get(modelParts.get(1))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
        } else if (up) {
            ((FabricBakedModel)bakedModels.get(variant).get(modelParts.get(0))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
        } else if (down)
        {
            ((FabricBakedModel)bakedModels.get(variant).get(modelParts.get(3))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            ((FabricBakedModel)bakedModels.get(variant).get(modelParts.get(5+onOffset))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            ((FabricBakedModel)bakedModels.get(variant).get(modelParts.get(4))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
        }
        else {
            ((FabricBakedModel)bakedModels.get(variant).get(modelParts.get(4))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            ((FabricBakedModel)bakedModels.get(variant).get(modelParts.get(2))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            ((FabricBakedModel)bakedModels.get(variant).get(modelParts.get(5+onOffset))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
        }
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        WoodVariant variant = WoodVariantRegistry.OAK;
        if (stack.hasNbt()) {
            variant = WoodVariantRegistry.getVariant(Identifier.tryParse(stack.getSubNbt("BlockEntityTag").getString("variant")));
        }
        ((FabricBakedModel)bakedModels.get(variant).get(modelParts.get(4))).emitItemQuads(stack, randomSupplier, context);
        ((FabricBakedModel)bakedModels.get(variant).get(modelParts.get(2))).emitItemQuads(stack, randomSupplier, context);
        ((FabricBakedModel)bakedModels.get(variant).get(modelParts.get(5))).emitItemQuads(stack, randomSupplier, context);
    }

    @Override
    public Sprite getParticleSprite() {
        return bakedModels.get(WoodVariantRegistry.OAK).get(modelParts.get(4)).getParticleSprite();
    }

    @Override
    public ModelTransformation getTransformation() {
        return bakedModels.get(WoodVariantRegistry.OAK).get(modelParts.get(2)).getTransformation();
    }
}
