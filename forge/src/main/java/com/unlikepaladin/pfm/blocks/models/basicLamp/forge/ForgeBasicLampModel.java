package com.unlikepaladin.pfm.blocks.models.basicLamp.forge;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.blocks.BasicLampBlock;
import com.unlikepaladin.pfm.blocks.blockentities.LampBlockEntity;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ForgeBasicLampModel extends AbstractBakedModel {
    private final List<String> modelParts;
    private final Map<WoodVariant, Map<String, BakedModel>> bakedModels;
    private final Map<WoodVariant, Sprite> textureMap;
    public ForgeBasicLampModel(Map<WoodVariant, Sprite> textures, ModelBakeSettings settings, Map<WoodVariant, Map<String, BakedModel>> bakedModels, List<String> modelParts) {
        super(textures.get(WoodVariantRegistry.OAK), settings, new HashMap<>());
        this.modelParts = modelParts;
        this.textureMap = textures;
        this.bakedModels = bakedModels;
    }


    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();
    public static ModelProperty<WoodVariant> VARIANT = new ModelProperty<>();
    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        ModelDataMap.Builder builder = new ModelDataMap.Builder();
        WoodVariant variant = WoodVariantRegistry.OAK;
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof LampBlockEntity) {
            variant = ((LampBlockEntity) entity).getVariant();
        }
        BitSet set = new BitSet();
        set.set(0, world.getBlockState(pos.up()).getBlock() instanceof BasicLampBlock);
        set.set(1, world.getBlockState(pos.down()).getBlock() instanceof BasicLampBlock);
        builder.withInitial(CONNECTIONS, new ModelBitSetProperty(set));
        builder.withInitial(VARIANT, variant);
        return builder.build();
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        List<BakedQuad> quads = new ArrayList<>();
        if (state != null && extraData.getData(CONNECTIONS) != null && extraData.getData(CONNECTIONS).connections != null) {
            int onOffset = state.get(Properties.LIT) ? 1 : 0;
            WoodVariant variant = extraData.getData(VARIANT);
            BitSet set = extraData.getData(CONNECTIONS).connections;
            if (set.get(0) && set.get(1)) {
                quads.addAll(bakedModels.get(variant).get(modelParts.get(1)).getQuads(state, side, rand, extraData));
            } else if (set.get(0)) {
                quads.addAll(bakedModels.get(variant).get(modelParts.get(0)).getQuads(state, side, rand, extraData));
            } else if (set.get(1))
            {
                quads.addAll(bakedModels.get(variant).get(modelParts.get(3)).getQuads(state, side, rand, extraData));
                quads.addAll(bakedModels.get(variant).get(modelParts.get(5+onOffset)).getQuads(state, side, rand, extraData));
                quads.addAll(bakedModels.get(variant).get(modelParts.get(4)).getQuads(state, side, rand, extraData));
            }
            else {
                quads.addAll(bakedModels.get(variant).get(modelParts.get(4)).getQuads(state, side, rand, extraData));
                quads.addAll(bakedModels.get(variant).get(modelParts.get(2)).getQuads(state, side, rand, extraData));
                quads.addAll(bakedModels.get(variant).get(modelParts.get(5+onOffset)).getQuads(state, side, rand, extraData));
            }
        }
        return quads;
    }

    @Override
    public boolean isBuiltin() {
        return true;
    }

    @Override
    public Sprite getSprite() {
        return bakedModels.get(WoodVariantRegistry.OAK).get(modelParts.get(4)).getSprite();
    }

    @Override
    public ModelTransformation getTransformation() {
        return bakedModels.get(WoodVariantRegistry.OAK).get(modelParts.get(2)).getTransformation();
    }
}
